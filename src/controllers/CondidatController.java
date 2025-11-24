package controllers;
import UI.CondidatUI;
import entites.Condidat;
import repositories.CondidatRepositorie;
import services.CondidatServive;

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
            System.out.println("0 - Quitter");
            System.out.print("Votre choix : ");

            choix = sc.nextInt();
            sc.nextLine(); // vider buffer

            switch (choix) {

                case 1:
                    System.out.println("\n--- Création d'un candidat ---");
                    Condidat c = CondidatUI.creerCondidat();
                    if (c != null) {
                        CondidatServive.ajouterCondidat(c);
                    }
                    break;

                case 2:
                    System.out.println("\n--- Modifier un candidat ---");
                    System.out.print("Entrer le CIN du candidat : ");
                    int cinModif = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nouveau mail : ");
                    String mail = sc.nextLine();

                    System.out.print("Nouvel âge : ");
                    int age = sc.nextInt();

                    CondidatServive.modifierCondidat(cinModif, mail, age);
                    break;

                case 3:
                    System.out.println("\n--- Suppression d'un candidat ---");
                    System.out.print("Entrer le CIN à supprimer : ");
                    int cinSup = sc.nextInt();

                    CondidatServive.supprimerCondidat(cinSup);
                    break;

                case 4:
                    System.out.println("\n--- Recherche d'un candidat ---");
                    System.out.print("Entrer le CIN : ");
                    int cinCherch = sc.nextInt();

                    CondidatServive.afficherCondidatParCin(cinCherch);
                    break;

                case 5:
                    System.out.println("\n--- Liste des candidats ---");
                    CondidatServive.afficherTous();
                    break;

                case 0:
                    System.out.println("Fermeture du menu...");
                    break;

                default:
                    System.out.println("❌ Choix invalide !");
            }
        }
    }


}
