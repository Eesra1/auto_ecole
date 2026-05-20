package repositories;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import entites.Seance;
import entites.SeanceCode;
import entites.SeanceConduite;
import entites.SeanceExamen;

/**
 * Repository pour gérer la persistence des séances dans "seances.json".
 * Contient addSeance (détecte type code/conduite/examen), méthodes de recherche pour disponibilité,
 * et méthodes utilitaires de lecture/sauvegarde.
 */
public class SeanceRepositorie {
    private static final String FILE_PATH = "seances.json";

    private static JSONArray loadArray() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) sb.append((char) c);
            if (sb.toString().isEmpty()) return new JSONArray();
            return new JSONArray(sb.toString());
        } catch (IOException e) {
            return new JSONArray();
        }
    }

    private static void saveArray(JSONArray array) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne le prochain numéro unique pour une séance (max(numero) + 1).
     * Utiliser cette méthode pour garantir l'unicité globale du numéro.
     */
    public static int getNextNumero() {
        JSONArray arr = loadArray();
        int max = 0;
        for (int i = 0; i < arr.length(); i++) {
            int n = arr.getJSONObject(i).optInt("numero", 0);
            if (n > max) max = n;
        }
        return max + 1;
    }

    /**
     * Ajoute une séance au fichier JSON.
     * - empêche duplication de "numero" (global)
     * - empêche ajout si moniteur déjà occupé à cette date/heure (sécurité côté repository)
     * - enregistre un champ "type" = "code" | "conduite" | "examen"
     *
     * @param s Seance (SeanceCode ou SeanceConduite ou SeanceExamen)
     * @return true si ajouté, false si numéro déjà existant ou moniteur occupé
     */
    public static boolean addSeance(Seance s) {
        JSONArray arr = loadArray();

        // vérifier numéro en conflit (global)
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("numero", -1) == s.getNumero()) {
                return false; // numéro en conflit
            }
        }

        // vérification de disponibilité moniteur (repository-level safety)
        int cinMon = s.getMoniteur() != null ? s.getMoniteur().getCin() : -1;
        if (cinMon != -1) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.optInt("moniteur_cin", -2) == cinMon &&
                        s.getDate().toString().equals(obj.optString("date", "")) &&
                        obj.optString("heure", "").startsWith(s.getHeure().toString())) {
                    // moniteur occupé
                    return false;
                }
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("numero", s.getNumero());
        obj.put("date", s.getDate().toString());
        obj.put("heure", s.getHeure().toString());
        obj.put("moniteur_cin", s.getMoniteur() != null ? s.getMoniteur().getCin() : JSONObject.NULL);
        obj.put("condidat_cin", s.getCondidat() != null ? s.getCondidat().getCin() : JSONObject.NULL);
        obj.put("prix", s.getPrix());

        if (s instanceof SeanceCode) {
            obj.put("type", "code");
        } else if (s instanceof SeanceConduite) {
            obj.put("type", "conduite");
            SeanceConduite sc = (SeanceConduite) s;
            if (sc.getVehicule() != null && sc.getVehicule().getImmatriculation() != null) {
                obj.put("vehicule_immatriculation", sc.getVehicule().getImmatriculation());
            } else {
                obj.put("vehicule_immatriculation", JSONObject.NULL);
            }
        } else if (s instanceof SeanceExamen) {
            obj.put("type", "examen");
            SeanceExamen se = (SeanceExamen) s;
            obj.put("examen_type", se.getExamenType().toString().toLowerCase()); // "code" ou "conduite"
        } else {
            obj.put("type", "autre");
        }

        arr.put(obj);
        saveArray(arr);
        return true;
    }

    /** Retourne toutes les séances */
    public static JSONArray afficherTous() {
        return loadArray();
    }

    /**
     * Cherche séances pour un moniteur à date+heure exacts (string match).
     * @param moniteurCin CIN du moniteur
     * @param date "YYYY-MM-DD"
     * @param heure "HH:MM" (matches beginning of stored heure)
     * @return JSONArray des séances trouvées
     */
    public static JSONArray chercherParMoniteurEtDateHeure(int moniteurCin, String date, String heure) {
        JSONArray arr = loadArray();
        JSONArray res = new JSONArray();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("moniteur_cin", -1) == moniteurCin &&
                    date.equals(obj.optString("date", "")) &&
                    obj.optString("heure", "").startsWith(heure)) {
                res.put(obj);
            }
        }
        return res;
    }

    /**
     * Cherche séances utilisant le véhicule donné à date+heure exacts.
     * @param immat immatriculation
     * @param date "YYYY-MM-DD"
     * @param heure "HH:MM"
     * @return JSONArray des séances trouvées
     */
    public static JSONArray chercherParVehiculeEtDateHeure(String immat, String date, String heure) {
        JSONArray arr = loadArray();
        JSONArray res = new JSONArray();
        if (immat == null) return res;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String v = obj.optString("vehicule_immatriculation", "");
            if (immat.equals(v) &&
                    date.equals(obj.optString("date", "")) &&
                    obj.optString("heure", "").startsWith(heure)) {
                res.put(obj);
            }
        }
        return res;
    }

    /**
     * Cherche toutes les séances d'un candidat pour une date donnée.
     * @param cinCondidat CIN du candidat
     * @param date date "YYYY-MM-DD"
     * @return JSONArray résultat (0..n)
     */
    public static JSONArray chercherParCinEtDate(int cinCondidat, String date) {
        JSONArray arr = loadArray();
        JSONArray res = new JSONArray();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("condidat_cin", -1) == cinCondidat &&
                    date.equals(obj.optString("date", ""))) {
                res.put(obj);
            }
        }
        return res;
    }

    /**
     * Cherche toutes les séances d'un candidat pour une date donnée.
     * (Alias pour chercherParCinEtDate)
     */
    public static JSONArray chercherParCandidatEtDate(int cinCandidat, String date) {
        return chercherParCinEtDate(cinCandidat, date);
    }

    /**
     * Supprime toutes les séances qui correspondent au CIN du candidat et à la date donnée.
     * @return true si au moins une suppression effectuée
     */
    public static boolean supprimerParCinEtDate(int cinCondidat, String date) {
        JSONArray arr = loadArray();
        boolean removed = false;
        for (int i = arr.length() - 1; i >= 0; i--) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("condidat_cin", -1) == cinCondidat &&
                    date.equals(obj.optString("date", ""))) {
                arr.remove(i);
                removed = true;
            }
        }
        if (removed) saveArray(arr);
        return removed;
    }

    /**
     * Nouvelle méthode : Supprimer une/les séance(s) qui correspondent au CIN du candidat, à la date donnée et à l'heure (ou début d'heure) donnée.
     * Exemple : heure = "14:30" supprimera les entrées dont "heure" commence par "14:30".
     * @return true si au moins une suppression effectuée
     */
    public static boolean supprimerParCinDateHeure(int cinCondidat, String date, String heure) {
        if (heure == null) return false;
        JSONArray arr = loadArray();
        boolean removed = false;
        for (int i = arr.length() - 1; i >= 0; i--) {
            JSONObject obj = arr.getJSONObject(i);
            String heureObj = obj.optString("heure", "");
            if (obj.optInt("condidat_cin", -1) == cinCondidat &&
                    date.equals(obj.optString("date", "")) &&
                    heureObj.startsWith(heure)) {
                arr.remove(i);
                removed = true;
            }
        }
        if (removed) saveArray(arr);
        return removed;
    }

    /**
     * Modifie toutes les séances correspondant au CIN du candidat et à la date donnée.
     * Si nouvelleDate / nouvelleHeure sont null ou vides, conservent les valeurs existantes.
     * Si nouveauPrix < 0 => prix conservé.
     */
    public static boolean modifierSeanceParCinEtDate(int cinCondidat, String date, String nouvelleDate, String nouvelleHeure, float nouveauPrix) {
        JSONArray arr = loadArray();
        boolean trouve = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("condidat_cin", -1) == cinCondidat &&
                    date.equals(obj.optString("date", ""))) {
                if (nouvelleDate != null && !nouvelleDate.isEmpty()) obj.put("date", nouvelleDate);
                if (nouvelleHeure != null && !nouvelleHeure.isEmpty()) obj.put("heure", nouvelleHeure);
                if (nouveauPrix >= 0f) obj.put("prix", nouveauPrix);
                trouve = true;
            }
        }
        if (trouve) saveArray(arr);
        return trouve;
    }

    /**
     * Modifier une séance par numéro
     */
    public static boolean modifierSeance(int numero, String newDate, String newHeure, float newPrix) {
        JSONArray arr = loadArray();
        boolean trouve = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("numero", -1) == numero) {
                if (newDate != null && !newDate.isEmpty()) obj.put("date", newDate);
                if (newHeure != null && !newHeure.isEmpty()) obj.put("heure", newHeure);
                if (newPrix >= 0f) obj.put("prix", newPrix);
                trouve = true;
                break; // On suppose que le numéro est unique
            }
        }
        if (trouve) saveArray(arr);
        return trouve;
    }

    /** Supprimer une séance par numéro */
    public static boolean supprimerParNumero(int numero) {
        JSONArray arr = loadArray();
        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).optInt("numero", -1) == numero) {
                arr.remove(i);
                saveArray(arr);
                return true;
            }
        }
        return false;
    }

    /**
     * Supprimer une séance par numéro (alias pour supprimerParNumero)
     */
    public static boolean supprimerSeance(int numero) {
        return supprimerParNumero(numero);
    }

    /**
     * Compter le nombre de séances par moniteur
     */
    public static JSONArray compterSeancesParMoniteur() {
        JSONArray arr = loadArray();
        JSONArray stats = new JSONArray();

        // Créer un map pour compter par CIN moniteur
        Map<Integer, Integer> countMap = new HashMap<>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            int cinMoniteur = obj.optInt("moniteur_cin", -1);
            if (cinMoniteur != -1) {
                countMap.put(cinMoniteur, countMap.getOrDefault(cinMoniteur, 0) + 1);
            }
        }

        // Convertir le map en JSONArray
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            JSONObject stat = new JSONObject();
            stat.put("cinMoniteur", entry.getKey());
            stat.put("nbSeances", entry.getValue());
            stats.put(stat);
        }

        return stats;
    }

    /**
     * Compter le nombre de séances d'un candidat
     */
    public static int compterSeancesParCandidat(int cinCandidat) {
        JSONArray arr = loadArray();
        int count = 0;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("condidat_cin", -1) == cinCandidat) {
                count++;
            }
        }

        return count;
    }
}