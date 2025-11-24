package controllers;

import UI.MoniteurUI;
import entites.Moniteur;
import services.MoniteurService;

import java.util.Scanner;

public class MoniteurController {

    public static void moniteurMenu() {

        Scanner sc = new Scanner(System.in);
        int choix = -1;

        while (choix != 0) {

            System.out.println("\n===== MENU GESTION DES MONITEURS =====");
            System.out.println("1 - Créer un nouveau moniteur");
            System.out.println("2 - Modifier un moniteur");
            System.out.println("3 - Supprimer un moniteur");
            System.out.println("4 - Chercher un moniteur par CIN");
            System.out.println("5 - Afficher tous les moniteurs");
            System.out.println("0 - Quitter");
            System.out.print("Votre choix : ");

            choix = sc.nextInt();
            sc.nextLine(); // vider buffer

            switch (choix) {

                case 1:
                    System.out.println("\n--- Création d'un moniteur ---");
                    Moniteur m = MoniteurUI.creerMoniteur();
                    if (m != null) {
                        MoniteurService.ajouterMoniteur(m);
                    }
                    break;

                case 2:
                    System.out.println("\n--- Modifier un moniteur ---");
                    System.out.print("Entrer le CIN du moniteur : ");
                    int cinModif = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nouveau mail : ");
                    String mail = sc.nextLine();

                    System.out.print("Nouveau numéro : ");
                    int numero = sc.nextInt();

                    MoniteurService.modifierMoniteur(cinModif, mail, numero);
                    break;

                case 3:
                    System.out.println("\n--- Suppression d'un moniteur ---");
                    System.out.print("Entrer le CIN à supprimer : ");
                    int cinSup = sc.nextInt();

                    MoniteurService.supprimerMoniteur(cinSup);
                    break;

                case 4:
                    System.out.println("\n--- Recherche d'un moniteur ---");
                    System.out.print("Entrer le CIN : ");
                    int cinCherch = sc.nextInt();

                    MoniteurService.afficherMoniteurParCin(cinCherch);
                    break;

                case 5:
                    System.out.println("\n--- Liste des moniteurs ---");
                    MoniteurService.afficherTous();
                    break;

                case 0:
                    System.out.println("Fermeture du menu des moniteurs...");
                    break;

                default:
                    System.out.println("❌ Choix invalide !");
            }
        }
    }
}
