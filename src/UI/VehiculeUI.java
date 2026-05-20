package UI;

import entites.*;
import java.time.LocalDate;
import java.util.Scanner;

public class VehiculeUI {
    static Scanner sc = new Scanner(System.in);

    public static Vehicule creerVehicule() {
        try {
            System.out.println("\n=== AJOUT D'UN VÉHICULE ===");

            System.out.print("Donner l'immatriculation : ");
            String immatriculation = sc.nextLine().trim();

            System.out.print("Donner la marque : ");
            String marque = sc.nextLine().trim();

            System.out.print("Donner le modèle : ");
            String modele = sc.nextLine().trim();

            System.out.print("Donner la date de mise en service (AAAA-MM-JJ) : ");
            LocalDate dateMiseService = LocalDate.parse(sc.nextLine().trim());

            System.out.println("\nType de véhicule :");
            System.out.println("1. Moto");
            System.out.println("2. Voiture");
            System.out.println("3. Camion");
            System.out.println("4. Autobus");
            System.out.print("Votre choix : ");
            int type = sc.nextInt();
            sc.nextLine(); // vider le buffer

            switch (type) {
                case 1:
                    return creerMoto(immatriculation, marque, modele, dateMiseService);
                case 2:
                    return creerVoiture(immatriculation, marque, modele, dateMiseService);
                case 3:
                    return creerCamion(immatriculation, marque, modele, dateMiseService);
                case 4:
                    return creerAutobus(immatriculation, marque, modele, dateMiseService);
                default:
                    System.out.println(" Type de véhicule invalide !");
                    return null;
            }

        } catch (Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
            sc.nextLine(); // vider buffer en cas d'erreur de saisie
        }

        return null;
    }

    private static Moto creerMoto(String immatriculation, String marque, String modele, LocalDate dateMiseService) {
        try {
            System.out.print("Donner la cylindrée (cm³) : ");
            int cylindree = sc.nextInt();
            sc.nextLine(); // vider le buffer

            return new Moto(immatriculation, marque, modele, dateMiseService, cylindree);

        } catch (Exception e) {
            System.out.println(" Erreur création moto : " + e.getMessage());
            return null;
        }
    }

    private static Voiture creerVoiture(String immatriculation, String marque, String modele, LocalDate dateMiseService) {
        try {
            System.out.print("Donner le nombre de places : ");
            int nombrePlaces = sc.nextInt();
            sc.nextLine(); // vider le buffer

            System.out.print("Donner le type de carburant : ");
            String carburant = sc.nextLine().trim();

            return new Voiture(immatriculation, marque, modele, dateMiseService, nombrePlaces, carburant);

        } catch (Exception e) {
            System.out.println(" Erreur création voiture : " + e.getMessage());
            return null;
        }
    }

    private static Camion creerCamion(String immatriculation, String marque, String modele, LocalDate dateMiseService) {
        try {
            System.out.print("Donner le poids total (tonnes) : ");
            double poidsTotal = sc.nextDouble();
            sc.nextLine(); // vider le buffer

            return new Camion(immatriculation, marque, modele, dateMiseService, poidsTotal);

        } catch (Exception e) {
            System.out.println(" Erreur création camion : " + e.getMessage());
            return null;
        }
    }

    private static Autobus creerAutobus(String immatriculation, String marque, String modele, LocalDate dateMiseService) {
        try {
            System.out.print("Donner la capacité passagers : ");
            int capacitePassagers = sc.nextInt();
            sc.nextLine(); // vider le buffer

            return new Autobus(immatriculation, marque, modele, dateMiseService, capacitePassagers);

        } catch (Exception e) {
            System.out.println(" Erreur création autobus : " + e.getMessage());
            return null;
        }
    }

    public static void afficherVehicule(Vehicule vehicule) {
        if (vehicule == null) {
            System.out.println(" Véhicule non trouvé !");
            return;
        }

        System.out.println("\n=== FICHE VÉHICULE ===");
        System.out.println("Immatriculation : " + vehicule.getImmatriculation());
        System.out.println("Marque/Modèle : " + vehicule.getMarque() + " " + vehicule.getModele());
        System.out.println("Type : " + vehicule.getType());
        System.out.println("Date mise service : " + vehicule.getDateMiseService());
        System.out.println("Kilométrage : " + vehicule.getKilometrageTotal() + " km");
        System.out.println("Prochain entretien : " + vehicule.getKmProchainEntretien() + " km");
        System.out.println("État : " + (vehicule.isEnService() ? "🟢 En service" : " Hors service"));

        // Informations spécifiques
        if (vehicule instanceof Moto moto) {
            System.out.println("Cylindrée : " + moto.getCylindree() + " cm³");
        } else if (vehicule instanceof Voiture voiture) {
            System.out.println("Nombre de places : " + voiture.getNombrePlaces());
            System.out.println("Carburant : " + voiture.getCarburant());
        } else if (vehicule instanceof Camion camion) {
            System.out.println("Poids total : " + camion.getPoidsTotal() + " tonnes");
        } else if (vehicule instanceof Autobus autobus) {
            System.out.println("Capacité passagers : " + autobus.getCapacitePassagers());
        }

        // Alertes
        if (vehicule.besoinEntretien()) {
            System.out.println("⚠️  ATTENTION : Véhicule a besoin d'entretien !");
        }
        if (vehicule.echeanceProche()) {
            System.out.println("⚠️  ATTENTION : Échéances proches !");
        }
    }

    public static String demanderImmatriculation() {
        System.out.print("Donner l'immatriculation du véhicule : ");
        return sc.nextLine().trim();
    }

    public static double demanderKilometrage() {
        System.out.print("Donner le kilométrage à ajouter : ");
        double km = sc.nextDouble();
        sc.nextLine(); // vider le buffer
        return km;
    }

    public static Maintenance creerMaintenance() {
        try {
            System.out.print("Donner la description de la maintenance : ");
            String description = sc.nextLine().trim();

            System.out.print("Donner le coût : ");
            double cout = sc.nextDouble();
            sc.nextLine(); // vider le buffer

            // Pour l'instant on retourne une maintenance basique
            // Le véhicule sera associé plus tard dans le service
            return new Maintenance(description, LocalDate.now(), cout, null);

        } catch (Exception e) {
            System.out.println(" Erreur création maintenance : " + e.getMessage());
            return null;
        }
    }
}
