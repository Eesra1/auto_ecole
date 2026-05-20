package repositories;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import entites.Condidat;


public class CondidatRepositorie {
    private static final String FILE_PATH = "condidats.json";

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
        // DEBUG log pour vérifier quel repository écrit quel fichier

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean addCondidat(Condidat c) {
        // Vérifier si le CIN existe déjà chez un moniteur (empêche conflit entre types)
        if (MoniteurRepositorie.chercherParCin(c.getCin()) != null) {
            return false;
        }

        JSONArray arr = loadArray();

        // Vérifier si le candidat existe déjà (même CIN)
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("cin", -1) == c.getCin()) {
                return false; // déjà existant → ne pas ajouter
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("nom", c.getNom());
        obj.put("numero", c.getNumero());
        obj.put("cin", c.getCin());
        obj.put("mail", c.getMail());
        obj.put("age", c.getAge());
        obj.put("nbSC", c.getNbSC());
        obj.put("montantPaye", c.getMontantPaye());
        obj.put("montantApaye", c.getMontantApaye());
        obj.put("permis", c.getPermis() != null ? c.getPermis().name() : JSONObject.NULL);

        arr.put(obj);

        saveArray(arr);
        return true;
    }

    public static JSONArray afficherTous() {
        return loadArray();
    }

    public static JSONObject chercherParCin(int cin) {
        JSONArray arr = loadArray();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("cin", -1) == cin) {
                return obj;
            }
        }
        return null;
    }

    public static boolean supprimerParCin(int cin) {
        JSONArray arr = loadArray();
        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).optInt("cin", -1) == cin) {
                arr.remove(i);
                saveArray(arr);
                return true;
            }
        }
        return false;
    }

    public static boolean modifierCondidat(int cin, String nouveauMail, int nouvelAge) {
        JSONArray arr = loadArray();
        boolean trouve = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("cin", -1) == cin) {
                obj.put("mail", nouveauMail);
                obj.put("age", nouvelAge);
                trouve = true;
                break;
            }
        }
        if (trouve) saveArray(arr);
        return trouve;
    }

    public static boolean updateCondidat(Condidat c) {
        JSONArray arr = loadArray();
        boolean trouve = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.optInt("cin", -1) == c.getCin()) {
                obj.put("nom", c.getNom());
                obj.put("numero", c.getNumero());
                obj.put("mail", c.getMail());
                obj.put("age", c.getAge());
                obj.put("nbSC", c.getNbSC());
                obj.put("montantPaye", c.getMontantPaye());
                obj.put("montantApaye", c.getMontantApaye());
                obj.put("permis", c.getPermis() != null ? c.getPermis().name() : JSONObject.NULL);
                trouve = true;
                break;
            }
        }
        if (trouve) saveArray(arr);
        return trouve;
    }
}