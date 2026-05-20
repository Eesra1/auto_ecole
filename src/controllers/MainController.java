package controllers;

import java.util.Scanner;

public class MainController {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choix = -1;

        do {
            System.out.println("====================================");
            System.out.println("         MENU PRINCIPAL AUTO-ÉCOLE");
            System.out.println("====================================");
            System.out.println("1 - Gérer les Candidats");
            System.out.println("2 - Gérer les Moniteurs");
            System.out.println("3 - Gérer les Séances ");
            System.out.println("4 - Gérer le Parc Véhicules");
            System.out.println("5 - Suivi comptable (Gestion du compte)");
            System.out.println("6 - Quitter");
            System.out.println("====================================");
            System.out.print("Votre choix : ");

            try {
                choix = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Entrée invalide ! Veuillez entrer un nombre.");
                sc.nextLine();
                continue;
            }

            switch (choix) {
                case 1:
                    System.out.println("\n Ouverture du module Candidats...");
                    CondidatController.condidatmenu();
                    break;

                case 2:
                    System.out.println("\n👨‍🏫 Ouverture du module Moniteurs...");
                    MoniteurController.moniteurMenu();
                    break;

                case 3:
                    System.out.println("\n Ouverture du module Séances...");
                    SeanceController.seancemenu(null, null);
                    break;

                case 4:
                    System.out.println("\n Ouverture du module Véhicules...");
                    VehiculeController.vehiculeMenu();
                    break;

                case 5:
                    System.out.println("\n Ouverture du module comptable...");
                    CompteController.compteMenu();
                    break;

                case 6:
                    System.out.println(" Au revoir ! Merci d'avoir utilisé notre application.");
                    break;

                default:
                    System.out.println(" Choix invalide ! Veuillez choisir entre 1 et 6.");
            }

            System.out.println();

        } while (choix != 6);

        sc.close();
    }
}
