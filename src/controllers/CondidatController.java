package controllers;

import UI.CondidatUI;
import entites.Condidat;
import repositories.CondidatRepositorie;
import services.CondidatService;


import java.util.Scanner;


public class CondidatController {
    public static void condidatmenu() {

        Scanner sc = new Scanner(System.in);
        int choix = -1;

        while (choix != 0) {

            System.out.println("\n===== MENU GESTION DES CANDIDATS =====");
            System.out.println("1 - Créer un nouveau candidat");
            System.out.println("2 - Modifier un candidat");
            System.out.println("3 - Supprimer un candidat");
            System.out.println("4 - Chercher un candidat par CIN");
            System.out.println("5 - Afficher tous les candidats");
            System.out.println("6 - Enregistrer un paiement d'un candidat"); // nouvelle option
            System.out.println("0 - Quitter");
            System.out.print("Votre choix : ");

            try {
                choix = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Entrée invalide !");
                continue;
            }

            switch (choix) {

                case 1:
                    System.out.println("\n--- Création d'un candidat ---");
                    Condidat c = CondidatUI.creerCondidat();
                    if (c != null) {
                        CondidatService.ajouterCondidat(c);
                    }
                    break;

                case 2:
                    System.out.println("\n--- Modifier un candidat ---");
                    try {
                        System.out.print("Entrer le CIN du candidat : ");
                        int cinModif = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Nouveau mail : ");
                        String mail = sc.nextLine().trim();

                        System.out.print("Nouvel âge : ");
                        int age = Integer.parseInt(sc.nextLine().trim());

                        CondidatService.modifierCondidat(cinModif, mail, age);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Format numérique invalide !");
                    }
                    break;

                case 3:
                    System.out.println("\n--- Suppression d'un candidat ---");
                    try {
                        System.out.print("Entrer le CIN à supprimer : ");
                        int cinSup = Integer.parseInt(sc.nextLine().trim());
                        CondidatService.supprimerCondidat(cinSup);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Format numérique invalide !");
                    }
                    break;

                case 4:
                    System.out.println("\n--- Recherche d'un candidat ---");
                    try {
                        System.out.print("Entrer le CIN : ");
                        int cinCherch = Integer.parseInt(sc.nextLine().trim());
                        CondidatService.afficherCondidatParCin(cinCherch);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Format numérique invalide !");
                    }
                    break;

                case 5:
                    System.out.println("\n--- Liste des candidats ---");
                    CondidatService.afficherTous();
                    break;

                case 6:
                    System.out.println("\n--- Enregistrer un paiement ---");
                    try {
                        System.out.print("Entrer le CIN du candidat : ");
                        int cinPaiement = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Entrer le montant payé (DT) : ");
                        float montant = Float.parseFloat(sc.nextLine().trim());
                        CondidatService.payerCondidat(cinPaiement, montant);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Format numérique invalide !");
                    }
                    break;

                case 0:
                    System.out.println("Fermeture du menu...");
                    break;

                default:
                    System.out.println("Choix invalide !");
            }
        }
    }
}