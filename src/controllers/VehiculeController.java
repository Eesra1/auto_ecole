package controllers;

import UI.VehiculeUI;
import entites.Vehicule;
import repositories.VehiculeRepository;
import services.VehiculeService;

import java.util.Scanner;

public class VehiculeController {
    public static void vehiculeMenu() {

        Scanner sc = new Scanner(System.in);
        int choix = -1;

        while (choix != 0) {

            System.out.println("\n===== MENU GESTION DES VÉHICULES =====");
            System.out.println("1 - Ajouter un véhicule");
            System.out.println("2 - Modifier un véhicule");
            System.out.println("3 - Supprimer un véhicule");
            System.out.println("4 - Chercher un véhicule par immatriculation");
            System.out.println("5 - Afficher tous les véhicules");
            System.out.println("6 - Véhicules besoin d'entretien");
            System.out.println("7 - Véhicules avec échéances proches");
            System.out.println("8 - Ajouter du kilométrage");
            System.out.println("9 - Effectuer une maintenance");
            System.out.println("10 - Historique des maintenances");
            System.out.println("0 - Quitter");
            System.out.print("Votre choix : ");

            choix = sc.nextInt();
            sc.nextLine(); // vider buffer

            switch (choix) {

                case 1:
                    System.out.println("\n--- Ajout d'un véhicule ---");
                    Vehicule v = VehiculeUI.creerVehicule();
                    if (v != null) {
                        VehiculeService.ajouterVehicule(v);
                    }
                    break;

                case 2:
                    System.out.println("\n--- Modifier un véhicule ---");
                    System.out.print("Entrer l'immatriculation du véhicule : ");
                    String immatriculationModif = sc.nextLine();

                    System.out.print("Nouveau kilométrage : ");
                    double nouveauKm = sc.nextDouble();

                    System.out.print("En service (true/false) : ");
                    boolean enService = sc.nextBoolean();

                    VehiculeService.modifierVehicule(immatriculationModif, nouveauKm, enService);
                    break;

                case 3:
                    System.out.println("\n--- Suppression d'un véhicule ---");
                    System.out.print("Entrer l'immatriculation à supprimer : ");
                    String immatriculationSup = sc.nextLine();

                    VehiculeService.supprimerVehicule(immatriculationSup);
                    break;

                case 4:
                    System.out.println("\n--- Recherche d'un véhicule ---");
                    System.out.print("Entrer l'immatriculation : ");
                    String immatriculationCherch = sc.nextLine();

                    VehiculeService.afficherVehiculeParImmatriculation(immatriculationCherch);
                    break;

                case 5:
                    System.out.println("\n--- Liste des véhicules ---");
                    VehiculeService.afficherTousVehicules();
                    break;

                case 6:
                    System.out.println("\n--- Véhicules besoin d'entretien ---");
                    VehiculeService.afficherVehiculesBesoinEntretien();
                    break;

                case 7:
                    System.out.println("\n--- Véhicules avec échéances proches ---");
                    VehiculeService.afficherVehiculesAvecEcheances();
                    break;

                case 8:
                    System.out.println("\n--- Ajouter du kilométrage ---");
                    System.out.print("Entrer l'immatriculation : ");
                    String immatriculationKm = sc.nextLine();

                    System.out.print("Kilométrage à ajouter : ");
                    double kmAjouter = sc.nextDouble();
                    sc.nextLine(); // vider buffer

                    VehiculeService.ajouterKilometrage(immatriculationKm, kmAjouter);
                    break;

                case 9:
                    System.out.println("\n--- Effectuer une maintenance ---");
                    System.out.print("Entrer l'immatriculation : ");
                    String immatriculationMaint = sc.nextLine();

                    System.out.print("Description de la maintenance : ");
                    String description = sc.nextLine();

                    System.out.print("Coût de la maintenance : ");
                    double cout = sc.nextDouble();
                    sc.nextLine(); // vider buffer

                    VehiculeService.effectuerMaintenance(immatriculationMaint, description, cout);
                    break;

                case 10:
                    System.out.println("\n--- Historique des maintenances ---");
                    System.out.print("Entrer l'immatriculation : ");
                    String immatriculationHist = sc.nextLine();

                    VehiculeService.afficherHistoriqueMaintenance(immatriculationHist);
                    break;

                case 0:
                    System.out.println("Fermeture du menu...");
                    break;

                default:
                    System.out.println(" Choix invalide !");
            }
        }
    }
}

