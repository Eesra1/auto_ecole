package UI;

import services.CompteService;
import java.time.LocalDate;
import java.util.Scanner;

public class CompteUI {
    static Scanner sc = new Scanner(System.in);

    public static void ajouterRevenuSeance() {
        try {
            System.out.print("Donner le montant de la séance : ");
            double montant = sc.nextDouble();
            sc.nextLine();

            System.out.print("Donner la date (AAAA-MM-JJ) : ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            CompteService.ajouterRevenuSeance(montant, date);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    public static void ajouterRevenuExamen() {
        try {
            System.out.print("Donner le montant de l'examen : ");
            double montant = sc.nextDouble();
            sc.nextLine();

            System.out.print("Donner la date (AAAA-MM-JJ) : ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            CompteService.ajouterRevenuExamen(montant, date);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    public static void ajouterRevenuInscription() {
        try {
            System.out.print("Donner le montant de l'inscription : ");
            double montant = sc.nextDouble();
            sc.nextLine();

            System.out.print("Donner la date (AAAA-MM-JJ) : ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            CompteService.ajouterRevenuInscription(montant, date);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    public static void ajouterDepenseSalaire() {
        try {
            System.out.print("Donner le montant du salaire : ");
            double montant = sc.nextDouble();
            sc.nextLine();

            System.out.print("Donner la date (AAAA-MM-JJ) : ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            CompteService.ajouterDepenseSalaire(montant, date);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    public static void ajouterDepenseMaintenance() {
        try {
            System.out.print("Donner le montant de la maintenance : ");
            double montant = sc.nextDouble();
            sc.nextLine();

            System.out.print("Donner la date (AAAA-MM-JJ) : ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            CompteService.ajouterDepenseMaintenance(montant, date);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    public static void ajouterDepenseCarburant() {
        try {
            System.out.print("Donner le montant du carburant : ");
            double montant = sc.nextDouble();
            sc.nextLine();

            System.out.print("Donner la date (AAAA-MM-JJ) : ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            CompteService.ajouterDepenseCarburant(montant, date);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    public static void ajouterDepenseAssurance() {
        try {
            System.out.print("Donner le montant de l'assurance : ");
            double montant = sc.nextDouble();
            sc.nextLine();

            System.out.print("Donner la date (AAAA-MM-JJ) : ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            CompteService.ajouterDepenseAssurance(montant, date);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    public static void afficherBilanMois() {
        try {
            System.out.print("Donner l'année : ");
            int annee = sc.nextInt();

            System.out.print("Donner le mois (1-12) : ");
            int mois = sc.nextInt();
            sc.nextLine();

            CompteService.afficherBilanMois(annee, mois);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    public static void afficherDetailsMois() {
        try {
            System.out.print("Donner l'année : ");
            int annee = sc.nextInt();

            System.out.print("Donner le mois (1-12) : ");
            int mois = sc.nextInt();
            sc.nextLine();

            CompteService.afficherDetailsMois(annee, mois);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }

    // ✅ Méthodes ajoutées pour le controller :

    public static void afficherBilanComplet() {
        CompteService.afficherBilanComplet();
    }

    public static void afficherStatistiques() {
        CompteService.afficherStatistiques();
    }

    public static void genererRapportPeriode() {
        try {
            System.out.println("--- début de la période ---");
            System.out.print("Donner l'année début : ");
            int anneeDebut = sc.nextInt();
            System.out.print("Donner le mois début : ");
            int moisDebut = sc.nextInt();
            sc.nextLine();

            System.out.println("--- fin de la période ---");
            System.out.print("Donner l'année fin : ");
            int anneeFin = sc.nextInt();
            System.out.print("Donner le mois fin : ");
            int moisFin = sc.nextInt();
            sc.nextLine();

            CompteService.genererRapportPeriode(anneeDebut, moisDebut, anneeFin, moisFin);

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine();
        }
    }
}
