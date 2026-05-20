package repositories;

import org.json.JSONArray;
import org.json.JSONObject;
import entites.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class VehiculeRepository {
    private static final String FILE_PATH = "vehicules.json";
    private static final String MAINTENANCE_FILE_PATH = "maintenances.json";

    /** Charger le fichier JSON des véhicules */
    private static JSONArray loadVehiculesArray() {
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

    /** Charger le fichier JSON des maintenances */
    private static JSONArray loadMaintenancesArray() {
        try (FileReader reader = new FileReader(MAINTENANCE_FILE_PATH)) {
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

    /** Sauvegarder le JSONArray dans le fichier véhicules */
    private static void saveVehiculesArray(JSONArray array) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(array.toString(4)); // format indenté
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Sauvegarder le JSONArray dans le fichier maintenances */
    private static void saveMaintenancesArray(JSONArray array) {
        try (FileWriter writer = new FileWriter(MAINTENANCE_FILE_PATH)) {
            writer.write(array.toString(4)); // format indenté
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean addVehicule(Vehicule v) {
        JSONArray arr = loadVehiculesArray();

        // Vérifier si le véhicule existe déjà (même immatriculation)
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getString("immatriculation").equals(v.getImmatriculation())) {
                return false; // déjà existant → ne pas ajouter
            }
        }

        // Création de l'objet JSON
        JSONObject obj = new JSONObject();
        obj.put("immatriculation", v.getImmatriculation());
        obj.put("marque", v.getMarque());
        obj.put("modele", v.getModele());
        obj.put("type", v.getType());
        obj.put("dateMiseService", v.getDateMiseService().toString());
        obj.put("kilometrageTotal", v.getKilometrageTotal());
        obj.put("kmProchainEntretien", v.getKmProchainEntretien());
        obj.put("dateVignette", v.getDateVignette().toString());
        obj.put("dateVisiteTechnique", v.getDateVisiteTechnique().toString());
        obj.put("dateAssurance", v.getDateAssurance().toString());
        obj.put("enService", v.isEnService());

        // Ajouter les attributs spécifiques selon le type
        if (v instanceof Moto moto) {
            obj.put("cylindree", moto.getCylindree());
        } else if (v instanceof Voiture voiture) {
            obj.put("nombrePlaces", voiture.getNombrePlaces());
            obj.put("carburant", voiture.getCarburant());
        } else if (v instanceof Camion camion) {
            obj.put("poidsTotal", camion.getPoidsTotal());
        } else if (v instanceof Autobus autobus) {
            obj.put("capacitePassagers", autobus.getCapacitePassagers());
        }

        // Ajout au tableau
        arr.put(obj);

        // Sauvegarde
        saveVehiculesArray(arr);

        return true; // ajouté avec succès
    }

    public static JSONArray afficherTous() {
        return loadVehiculesArray();
    }

    public static JSONObject chercherParImmatriculation(String immatriculation) {
        JSONArray arr = loadVehiculesArray();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getString("immatriculation").equals(immatriculation)) {
                return obj; // renvoie le véhicule trouvé
            }
        }

        return null; // pas trouvé
    }

    public static boolean supprimerParImmatriculation(String immatriculation) {
        JSONArray arr = loadVehiculesArray();

        for (int i = 0; i < arr.length(); i++) {
            if (arr.getJSONObject(i).getString("immatriculation").equals(immatriculation)) {
                arr.remove(i);      // supprimer
                saveVehiculesArray(arr);     // sauvegarder
                return true;        // supprimé
            }
        }

        return false; // non trouvé
    }

    public static boolean modifierVehicule(String immatriculation, double nouveauKilometrage, boolean enService) {
        JSONArray arr = loadVehiculesArray();
        boolean trouve = false;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getString("immatriculation").equals(immatriculation)) {
                obj.put("kilometrageTotal", nouveauKilometrage);
                obj.put("enService", enService);
                trouve = true;
                break;
            }
        }

        if (trouve) {
            saveVehiculesArray(arr);   // sauvegarder les modifications
        }

        return trouve;  // true si le véhicule existe, false sinon
    }

    public static boolean ajouterKilometrage(String immatriculation, double kilometres) {
        JSONArray arr = loadVehiculesArray();
        boolean trouve = false;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getString("immatriculation").equals(immatriculation)) {
                double ancienKm = obj.getDouble("kilometrageTotal");
                obj.put("kilometrageTotal", ancienKm + kilometres);
                trouve = true;
                break;
            }
        }

        if (trouve) {
            saveVehiculesArray(arr);
        }

        return trouve;
    }

    public static JSONArray vehiculesBesoinEntretien() {
        JSONArray arr = loadVehiculesArray();
        JSONArray result = new JSONArray();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            double kmTotal = obj.getDouble("kilometrageTotal");
            double kmProchainEntretien = obj.getDouble("kmProchainEntretien");

            if (kmTotal >= kmProchainEntretien) {
                result.put(obj);
            }
        }

        return result;
    }

    public static JSONArray vehiculesAvecEcheances() {
        JSONArray arr = loadVehiculesArray();
        JSONArray result = new JSONArray();
        LocalDate aujourdhui = LocalDate.now();
        LocalDate dansUnMois = aujourdhui.plusMonths(1);

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            LocalDate dateVignette = LocalDate.parse(obj.getString("dateVignette"));
            LocalDate dateVisiteTechnique = LocalDate.parse(obj.getString("dateVisiteTechnique"));
            LocalDate dateAssurance = LocalDate.parse(obj.getString("dateAssurance"));

            if (dateVignette.isBefore(dansUnMois) ||
                    dateVisiteTechnique.isBefore(dansUnMois) ||
                    dateAssurance.isBefore(dansUnMois)) {
                result.put(obj);
            }
        }

        return result;
    }

    public static boolean ajouterMaintenance(String immatriculation, String description, double cout) {
        JSONArray arr = loadMaintenancesArray();

        // Création de l'objet maintenance
        JSONObject obj = new JSONObject();
        obj.put("immatriculation", immatriculation);
        obj.put("description", description);
        obj.put("cout", cout);
        obj.put("date", LocalDate.now().toString());

        // Ajout au tableau
        arr.put(obj);

        // Sauvegarde
        saveMaintenancesArray(arr);

        return true;
    }

    public static JSONArray getHistoriqueMaintenance(String immatriculation) {
        JSONArray arr = loadMaintenancesArray();
        JSONArray result = new JSONArray();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getString("immatriculation").equals(immatriculation)) {
                result.put(obj);
            }
        }

        return result;
    }
}
