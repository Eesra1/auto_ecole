package controllers;

import UI.CompteUI;
import services.CompteService;
import java.util.Scanner;

public class CompteController {
    public static void compteMenu() {

        Scanner sc = new Scanner(System.in);
        int choix = -1;

        while (choix != 0) {

            System.out.println("\n===== SUIVI COMPTABLE =====");
            System.out.println("1 - Ajouter revenu séance");
            System.out.println("2 - Ajouter revenu examen");
            System.out.println("3 - Ajouter revenu inscription");
            System.out.println("4 - Ajouter dépense salaire");
            System.out.println("5 - Ajouter dépense maintenance");
            System.out.println("6 - Ajouter dépense carburant");
            System.out.println("7 - Ajouter dépense assurance");
            System.out.println("8 - Bilan d'un mois spécifique");
            System.out.println("9 - Détails d'un mois");
            System.out.println("10 - Bilan complet (tous les mois)");
            System.out.println("11 - Statistiques comptables");
            System.out.println("12 - Rapport sur une période");
            System.out.println("0 - Quitter");
            System.out.print("Votre choix : ");

            choix = sc.nextInt();
            sc.nextLine(); // vider buffer

            switch (choix) {

                case 1:
                    System.out.println("\n--- Ajouter un revenu séance ---");
                    CompteUI.ajouterRevenuSeance();
                    break;

                case 2:
                    System.out.println("\n--- Ajouter un revenu examen ---");
                    CompteUI.ajouterRevenuExamen();
                    break;

                case 3:
                    System.out.println("\n--- Ajouter un revenu inscription ---");
                    CompteUI.ajouterRevenuInscription();
                    break;

                case 4:
                    System.out.println("\n--- Ajouter une dépense salaire ---");
                    CompteUI.ajouterDepenseSalaire();
                    break;

                case 5:
                    System.out.println("\n--- Ajouter une dépense maintenance ---");
                    CompteUI.ajouterDepenseMaintenance();
                    break;

                case 6:
                    System.out.println("\n--- Ajouter une dépense carburant ---");
                    CompteUI.ajouterDepenseCarburant();
                    break;

                case 7:
                    System.out.println("\n--- Ajouter une dépense assurance ---");
                    CompteUI.ajouterDepenseAssurance();
                    break;

                case 8:
                    System.out.println("\n--- Bilan d'un mois spécifique ---");
                    CompteUI.afficherBilanMois();
                    break;

                case 9:
                    System.out.println("\n--- Détails d'un mois ---");
                    CompteUI.afficherDetailsMois();
                    break;

                case 10:
                    System.out.println("\n--- Bilan complet ---");
                    CompteUI.afficherBilanComplet();
                    break;

                case 11:
                    System.out.println("\n--- Statistiques comptables ---");
                    CompteUI.afficherStatistiques();
                    break;

                case 12:
                    System.out.println("\n--- Rapport sur une période ---");
                    CompteUI.genererRapportPeriode();
                    break;

                case 0:
                    System.out.println("Fermeture du menu comptable...");
                    break;

                default:
                    System.out.println(" Choix invalide !");
            }
        }
    }
}
