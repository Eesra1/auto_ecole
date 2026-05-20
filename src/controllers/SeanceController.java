package controllers;

import entites.Moniteur;
import entites.Condidat;
import services.SeanceService;
import Exeption.CondidatExption;
import Exeption.MoniteurException;

import java.util.Scanner;

/**
 * Controller console pour gérer le menu des séances.
 * S'appuie sur SeanceService pour la logique métier.
 */
public class SeanceController {

    public static void seancemenu(Moniteur moniteurCourant, Condidat condidatCourant) {
        Scanner sc = new Scanner(System.in);
        int choix = -1;

        while (choix != 0) {
            System.out.println("\n===== MENU GESTION DES SEANCES =====");
            System.out.println("1 - Créer une nouvelle séance");
            System.out.println("2 - Modifier une séance (par CIN candidat + date)");
            System.out.println("3 - Supprimer une séance (par CIN candidat + date + heure)"); // modifié
            System.out.println("4 - Chercher des séances (par CIN candidat + date)");
            System.out.println("5 - Afficher toutes les séances");
            System.out.println("6 - Afficher nombre de séances par moniteur");
            System.out.println("7 - Afficher nombre de séances prises par un candidat (par CIN)");
            System.out.println("8 - Créer une séance d'examen (rapide)");
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
                    // Gestion des exceptions pour l'ajout de séance
                    try {
                        SeanceService.ajouterSeanceInteractive();
                    } catch (CondidatExption e) {
                        System.out.println("Erreur Candidat : " + e.getMessage());
                    } catch (MoniteurException e) {
                        System.out.println("Erreur Moniteur : " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Erreur inattendue : " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("Entrer CIN du candidat : ");
                        int cinMod = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Entrer la date actuelle (YYYY-MM-DD) : ");
                        String dateMod = sc.nextLine().trim();
                        System.out.print("Nouvelle date (laissez vide pour garder) : ");
                        String newDate = sc.nextLine().trim();
                        if (newDate.isEmpty()) newDate = null;
                        System.out.print("Nouvelle heure (HH:MM) (laissez vide pour garder) : ");
                        String newHeure = sc.nextLine().trim();
                        if (newHeure.isEmpty()) newHeure = null;
                        System.out.print("Nouveau prix (laisser vide pour garder) : ");
                        String prixStr = sc.nextLine().trim();
                        float newPrix = -1f;
                        if (!prixStr.isEmpty()) {
                            try {
                                newPrix = Float.parseFloat(prixStr);
                            } catch (Exception e) {
                                newPrix = -1f;
                            }
                        }
                        SeanceService.modifierSeanceParCinEtDate(cinMod, dateMod, newDate, newHeure, newPrix);
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : Format de nombre invalide !");
                    } catch (Exception e) {
                        System.out.println("Erreur lors de la modification : " + e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.print("Entrer CIN du candidat : ");
                        int cinSup = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Entrer la date de la séance à supprimer (YYYY-MM-DD) : ");
                        String dateSup = sc.nextLine().trim();
                        System.out.print("Entrer l'heure de la séance à supprimer (HH:MM) : ");
                        String heureSup = sc.nextLine().trim();
                        SeanceService.supprimerSeancesParCinDateHeure(cinSup, dateSup, heureSup);
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : Format de CIN invalide !");
                    } catch (Exception e) {
                        System.out.println("Erreur lors de la suppression : " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        System.out.print("Entrer CIN du candidat : ");
                        int cinCherch = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Entrer la date des séances à rechercher (YYYY-MM-DD) : ");
                        String dateCherch = sc.nextLine().trim();
                        SeanceService.afficherSeancesParCinEtDate(cinCherch, dateCherch);
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : Format de CIN invalide !");
                    } catch (Exception e) {
                        System.out.println("Erreur lors de la recherche : " + e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        SeanceService.afficherTous();
                    } catch (Exception e) {
                        System.out.println("Erreur lors de l'affichage : " + e.getMessage());
                    }
                    break;

                case 6:
                    try {
                        SeanceService.afficherNbSeancesParMoniteur();
                    } catch (Exception e) {
                        System.out.println("Erreur lors du calcul des statistiques : " + e.getMessage());
                    }
                    break;

                case 7:
                    try {
                        System.out.print("Entrer CIN du candidat : ");
                        int cinCount = Integer.parseInt(sc.nextLine().trim());
                        SeanceService.afficherNbSeancesParCondidat(cinCount);
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : Format de CIN invalide !");
                    } catch (Exception e) {
                        System.out.println("Erreur lors du comptage : " + e.getMessage());
                    }
                    break;

                case 8:
                    // Nouvelle option : création rapide d'une séance d'examen
                    try {
                        SeanceService.ajouterSeanceExamenInteractive(moniteurCourant, condidatCourant);
                    } catch (CondidatExption e) {
                        System.out.println("Erreur Candidat : " + e.getMessage());
                    } catch (MoniteurException e) {
                        System.out.println("Erreur Moniteur : " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Erreur inattendue : " + e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Fermeture du menu...");
                    break;

                default:
                    System.out.println("Choix invalide !");
            }
        }

        // Ne pas fermer Scanner(System.in) ici pour éviter de fermer System.in globalement.
    }
}