package repositories;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import entites.Seance;
import entites.SeanceCode;
import entites.SeanceConduite;

// Ici, on suppose que SéanceCode et SéanceConduite sont des sous-classes de Seance

public class SeanceRepositorie {
    private static final String FILE_PATH = "seances.json";

    /** Charger le fichier JSON (ou retourner un tableau vide) */
    private static JSONArray loadArray() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            if (sb.toString().isEmpty()) return new JSONArray();
            return new JSONArray(sb.toString());
        } catch (IOException e) {
            return new JSONArray(); // fichier introuvable = liste vide
        }
    }

    /** Sauvegarder le JSONArray dans le fichier JSON */
    private static void saveArray(JSONArray array) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(array.toString(4)); // format indenté
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Ajouter une séance (numéro unique dans le fichier) */
    public static boolean addSeance(Seance s) {
        JSONArray arr = loadArray();

        // Vérifier si la séance existe déjà (même numéro)
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getInt("numero") == s.getNumero()) {
                return false;
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("numero", s.getNumero());
        obj.put("date", s.getDate().toString());
        obj.put("heure", s.getHeure().toString());
        obj.put("moniteur_cin", s.getMoniteur().getCin());
        obj.put("prix", s.getPrix());
        obj.put("condidat_cin", s.getCondidat().getCin());

        // Pour différencier code/conduite, on peut utiliser instanceof
        if (s instanceof SeanceCode) {
            obj.put("type", "code");
        } else if (s instanceof SeanceConduite) {
            obj.put("type", "conduite");
            SeanceConduite sc = (SeanceConduite) s;
            if (sc.getVehicule() != null) {
                obj.put("vehicule_immatriculation", sc.getVehicule().getImmatriculation());
            } else {
                obj.put("vehicule_immatriculation", JSONObject.NULL);
            }
        } else {
            obj.put("type", "autre");
        }

        arr.put(obj);
        saveArray(arr);

        return true;
    }

    /** Retourner toutes les séances */
    public static JSONArray afficherTous() {
        return loadArray();
    }

    /** Chercher une séance par numéro */
    public static JSONObject chercherParNumero(int numero) {
        JSONArray arr = loadArray();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getInt("numero") == numero) {
                return obj;
            }
        }
        return null; // pas trouvé
    }

    /** Supprimer une séance par numéro */
    public static boolean supprimerParNumero(int numero) {
        JSONArray arr = loadArray();
        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).getInt("numero") == numero) {
                arr.remove(i);
                saveArray(arr);
                return true;
            }
        }
        return false;
    }

    /** Modifier certaines infos d'une séance (date, heure, prix) */
    public static boolean modifierSeance(int numero, String nouvelleDate, String nouvelleHeure, float nouveauPrix) {
        JSONArray arr = loadArray();
        boolean trouve = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getInt("numero") == numero) {
                obj.put("date", nouvelleDate);
                obj.put("heure", nouvelleHeure);
                obj.put("prix", nouveauPrix);
                trouve = true;
                break;
            }
        }
        if (trouve) {
            saveArray(arr);
        }
        return trouve;
    }
}