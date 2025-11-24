package repositories;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import entites.Condidat;
public class CondidatRepositorie {
    private static final String FILE_PATH = "condidats.json";

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



    public static boolean addCondidat(Condidat c) {

        JSONArray arr = loadArray();

        // Vérifier si le candidat existe déjà (même CIN)
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (obj.getInt("cin") == c.getCin()) {
                return false; // déjà existant → ne pas ajouter
            }
        }

        // Création de l'objet JSON
        JSONObject obj = new JSONObject();
        obj.put("nom", c.getNom());
        obj.put("numero", c.getNumero());
        obj.put("cin", c.getCin());
        obj.put("mail", c.getMail());
        obj.put("age", c.getAge());
        obj.put("nbSC", c.getNbSC());
        obj.put("montantPaye", c.getMontantPaye());
        obj.put("montantApaye", c.getMontantApaye());

        // Ajout au tableau
        arr.put(obj);

        // Sauvegarde
        saveArray(arr);

        return true; // ajouté avec succès
    }

    public static JSONArray afficherTous() {
        return loadArray();
    }

    public static JSONObject chercherParCin(int cin) {
        JSONArray arr = loadArray();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (obj.getInt("cin") == cin) {
                return obj; // renvoie le candidat trouvé
            }
        }

        return null; // pas trouvé
    }
    public static boolean supprimerParCin(int cin) {
        JSONArray arr = loadArray();

        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).getInt("cin") == cin) {
                arr.remove(i);      // supprimer
                saveArray(arr);     // sauvegarder
                return true;        // supprimé
            }
        }

        return false; // non trouvé
    }

    public static boolean modifierCondidat(int cin, String nouveauMail, int nouvelAge) {

        JSONArray arr = loadArray();
        boolean trouve = false;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (obj.getInt("cin") == cin) {
                obj.put("mail", nouveauMail);
                obj.put("age", nouvelAge);
                trouve = true;
                break;
            }
        }

        if (trouve) {
            saveArray(arr);   // sauvegarder les modifications
        }

        return trouve;  // true si le candidat existe, false sinon
    }

}




