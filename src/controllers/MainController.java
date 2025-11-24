package controllers;

import java.util.Scanner;

public class MainController {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choix = -1;

        do {
            System.out.println("====================================");
            System.out.println("         MENU PRINCIPAL");
            System.out.println("====================================");
            System.out.println("1 - Gérer les Candidats");
            System.out.println("2 - Gérer les Moniteurs");
            System.out.println("3 - Quitter");
            System.out.println("====================================");
            System.out.print("Votre choix : ");

            try {
                choix = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Entrée invalide !");
                sc.nextLine(); // vider buffer
                continue;
            }

            switch (choix) {
                case 1:
                    CondidatController.condidatmenu();
                    break;

                case 2:
                    MoniteurController.moniteurMenu();
                    break;

                case 3:
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Choix invalide !");
            }

        } while (choix != 3);

        sc.close();
    }
}
