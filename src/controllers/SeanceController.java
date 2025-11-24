package controllers;

import UI.SceanceUI;
import entites.Moniteur;
import entites.Condidat;
import entites.Seance;
import entites.SeanceCode;
import entites.SeanceConduite;
import repositories.SeanceRepositorie;
import services.SeanceService;

import java.util.Scanner;

public class SeanceController {

    public static void seancemenu(Moniteur moniteur, Condidat condidat) {
        Scanner sc = new Scanner(System.in);
        int choix = -1;

        while (choix != 0) {
            System.out.println("\n===== MENU GESTION DES SEANCES =====");
            System.out.println("1 - Créer une nouvelle séance (code)");
            System.out.println("2 - Modifier une séance");
            System.out.println("3 - Supprimer une séance");
            System.out.println("4 - Chercher une séance par numéro");
            System.out.println("5 - Afficher toutes les séances");
            System.out.println("0 - Quitter");
            System.out.print("Votre choix : ");

            choix = sc.nextInt();
            sc.nextLine(); // vider buffer

            switch (choix) {
                case 1:
                    System.out.println("\n--- Création d'une séance de code ---");
                    Seance seance = SceanceUI.saisirSeanceCode(moniteur, condidat);
                    if (seance != null) {
                        SeanceService.ajouterSeance(seance);
                    }
                    break;

                case 2:
                    System.out.println("\n--- Modifier une séance ---");
                    System.out.print("Entrer le numéro de la séance à modifier : ");
                    int numModif = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nouvelle date (AAAA-MM-JJ) : ");
                    String nouvelleDate = sc.nextLine();

                    System.out.print("Nouvelle heure (HH:MM) : ");
                    String nouvelleHeure = sc.nextLine();

                    System.out.print("Nouveau prix : ");
                    float nouveauPrix = sc.nextFloat();

                    SeanceService.modifierSeance(numModif, nouvelleDate, nouvelleHeure, nouveauPrix);
                    break;

                case 3:
                    System.out.println("\n--- Suppression d'une séance ---");
                    System.out.print("Entrer le numéro de la séance à supprimer : ");
                    int numSup = sc.nextInt();

                    SeanceService.supprimerSeance(numSup);
                    break;

                case 4:
                    System.out.println("\n--- Recherche d'une séance ---");
                    System.out.print("Entrer le numéro de la séance : ");
                    int numCherch = sc.nextInt();

                    SeanceService.afficherSeanceParNumero(numCherch);
                    break;

                case 5:
                    System.out.println("\n--- Liste des séances ---");
                    SeanceService.afficherTous();
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