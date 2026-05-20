package repositories;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import entites.Moniteur;


public class MoniteurRepositorie {

    private static final String FILE_PATH = "moniteurs.json";


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
            return new JSONArray();
        }
    }

    /** Sauvegarder dans fichier JSON */
    private static void saveArray(JSONArray array) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static boolean addMoniteur(Moniteur m) {

        // Vérifier si le CIN existe déjà chez un candidat (empêche conflit entre types)
        if (CondidatRepositorie.chercherParCin(m.getCin()) != null) {
            return false;
        }

        JSONArray arr = loadArray();

        // Vérifier si le moniteur existe déjà via CIN
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (obj.getInt("cin") == m.getCin()) {
                return false; // existe déjà
            }
        }

        // Objet JSON
        JSONObject obj = new JSONObject();
        obj.put("nom", m.getNom());
        obj.put("mail", m.getMail());
        obj.put("numero", m.getNumero());
        obj.put("cin", m.getCin());

        arr.put(obj);
        saveArray(arr);

        return true;
    }


    /** Afficher tous les moniteurs */
    public static JSONArray afficherTous() {
        return loadArray();
    }


    /** Chercher un moniteur par CIN */
    public static JSONObject chercherParCin(int cin) {
        JSONArray arr = loadArray();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (obj.getInt("cin") == cin) {
                return obj;
            }
        }

        return null;
    }


    /** Supprimer un moniteur par CIN */
    public static boolean supprimerParCin(int cin) {
        JSONArray arr = loadArray();

        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).getInt("cin") == cin) {
                arr.remove(i);
                saveArray(arr);
                return true;
            }
        }

        return false;
    }


    /** Modifier email + numéro du moniteur */
    public static boolean modifierMoniteur(int cin, String nouveauMail, int nouveauNumero) {

        JSONArray arr = loadArray();
        boolean trouve = false;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (obj.getInt("cin") == cin) {
                obj.put("mail", nouveauMail);
                obj.put("numero", nouveauNumero);
                trouve = true;
                break;
            }
        }

        if (trouve) {
            saveArray(arr);
        }

        return retrouveSafe(trouve);
    }

    // small helper to avoid accidental refactor issues
    private static boolean retrouveSafe(boolean trouve) {
        return trouve;
    }

}