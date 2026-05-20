package services;

import entites.*;
import repositories.VehiculeRepository;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;

public class VehiculeService {
    private static VehiculeRepository vehiculeRepository = new VehiculeRepository();

    public static void afficherVehiculeParImmatriculation(String immatriculation) {
        JSONObject obj = vehiculeRepository.chercherParImmatriculation(immatriculation);

        if (obj == null) {
            System.out.println("❌ Véhicule n'existe pas !");
            return;
        }

        System.out.println("✔ Véhicule trouvé : ");
        System.out.println("Immatriculation : " + obj.getString("immatriculation"));
        System.out.println("Marque : " + obj.getString("marque"));
        System.out.println("Modèle : " + obj.getString("modele"));
        System.out.println("Type : " + obj.getString("type"));
        System.out.println("Date mise service : " + obj.getString("dateMiseService"));
        System.out.println("Kilométrage : " + obj.getDouble("kilometrageTotal") + " km");
        System.out.println("Prochain entretien : " + obj.getDouble("kmProchainEntretien") + " km");
        System.out.println("En service : " + obj.getBoolean("enService"));

        // Informations spécifiques
        if (obj.getString("type").equals("Moto")) {
            System.out.println("Cylindrée : " + obj.getInt("cylindree") + " cm³");
        } else if (obj.getString("type").equals("Voiture")) {
            System.out.println("Nombre places : " + obj.getInt("nombrePlaces"));
            System.out.println("Carburant : " + obj.getString("carburant"));
        } else if (obj.getString("type").equals("Camion")) {
            System.out.println("Poids total : " + obj.getDouble("poidsTotal") + " tonnes");
        } else if (obj.getString("type").equals("Autobus")) {
            System.out.println("Capacité passagers : " + obj.getInt("capacitePassagers"));
        }
    }

    public static void supprimerVehicule(String immatriculation) {
        boolean resultat = vehiculeRepository.supprimerParImmatriculation(immatriculation);

        if (resultat) {
            System.out.println("✔ Véhicule supprimé !");
        } else {
            System.out.println("❌ Aucun véhicule trouvé avec cette immatriculation.");
        }
    }

    public static void afficherTousVehicules() {
        JSONArray arr = vehiculeRepository.afficherTous();

        if (arr.isEmpty()) {
            System.out.println("Aucun véhicule trouvé.");
            return;
        }

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            System.out.println("----- Véhicule " + (i + 1) + " -----");
            System.out.println("Immatriculation : " + obj.getString("immatriculation"));
            System.out.println("Marque/Modèle : " + obj.getString("marque") + " " + obj.getString("modele"));
            System.out.println("Type : " + obj.getString("type"));
            System.out.println("Kilométrage : " + obj.getDouble("kilometrageTotal") + " km");
            System.out.println("En service : " + (obj.getBoolean("enService") ? "🟢 OUI" : "🔴 NON"));
            System.out.println("--------------------------------");
        }
    }

    public static void afficherVehiculesBesoinEntretien() {
        JSONArray arr = vehiculeRepository.vehiculesBesoinEntretien();

        if (arr.isEmpty()) {
            System.out.println("✅ Aucun véhicule besoin d'entretien.");
            return;
        }

        System.out.println("⚠️  VÉHICULES BESOIN D'ENTRETIEN :");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            double kmRestant = obj.getDouble("kmProchainEntretien") - obj.getDouble("kilometrageTotal");

            System.out.println("🔧 " + obj.getString("immatriculation") + " - " +
                    obj.getString("marque") + " " + obj.getString("modele") +
                    " (reste: " + kmRestant + " km)");
        }
    }

    public static void afficherVehiculesAvecEcheances() {
        JSONArray arr = vehiculeRepository.vehiculesAvecEcheances();

        if (arr.isEmpty()) {
            System.out.println("✅ Aucune échéance proche.");
            return;
        }

        System.out.println("⚠️  VÉHICULES AVEC ÉCHÉANCES PROCHEs :");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            System.out.println("📅 " + obj.getString("immatriculation") + " - " +
                    obj.getString("marque") + " " + obj.getString("modele"));
        }
    }

    public static void modifierVehicule(String immatriculation, double nouveauKilometrage, boolean enService) {
        JSONObject obj = vehiculeRepository.chercherParImmatriculation(immatriculation);

        if (obj == null) {
            System.out.println("❌ Aucun véhicule trouvé avec cette immatriculation.");
            return;
        }

        vehiculeRepository.modifierVehicule(immatriculation, nouveauKilometrage, enService);
        System.out.println("✔ Véhicule modifié !");
    }

    public static void ajouterVehicule(Vehicule vehicule) {
        boolean added = vehiculeRepository.addVehicule(vehicule);

        if (added) {
            System.out.println("✔ Véhicule ajouté avec succès !");
        } else {
            System.out.println("❌ Ce véhicule existe déjà (immatriculation en double).");
        }
    }

    public static void ajouterKilometrage(String immatriculation, double kilometres) {
        boolean resultat = vehiculeRepository.ajouterKilometrage(immatriculation, kilometres);

        if (resultat) {
            System.out.println("✔ Kilométrage ajouté avec succès !");
        } else {
            System.out.println("❌ Véhicule non trouvé.");
        }
    }

    public static void effectuerMaintenance(String immatriculation, String description, double cout) {
        boolean resultat = vehiculeRepository.ajouterMaintenance(immatriculation, description, cout);

        if (resultat) {
            System.out.println("✔ Maintenance enregistrée avec succès !");
        } else {
            System.out.println("❌ Véhicule non trouvé.");
        }
    }

    public static void afficherHistoriqueMaintenance(String immatriculation) {
        JSONArray arr = vehiculeRepository.getHistoriqueMaintenance(immatriculation);

        if (arr == null || arr.isEmpty()) {
            System.out.println("❌ Véhicule non trouvé ou aucune maintenance enregistrée.");
            return;
        }

        System.out.println("📊 HISTORIQUE MAINTENANCE pour " + immatriculation + " :");
        double total = 0;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            System.out.println("📅 " + obj.getString("date") + " - " +
                    obj.getString("description") + " - " +
                    obj.getDouble("cout") + " €");
            total += obj.getDouble("cout");
        }
        System.out.println("💰 TOTAL DÉPENSÉ : " + total + " €");
    }
}
