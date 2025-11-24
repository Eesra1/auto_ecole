package UI;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import entites.Condidat;
import entites.Moniteur;
import entites.SeanceCode;
import entites.Seance;

public class SceanceUI {
    public static Seance saisirSeanceCode(Moniteur m, Condidat c) {
        Scanner sc = new Scanner(System.in);

        System.out.println("==== Saisie d'une nouvelle séance de code pour " + c.getNom() + " ====");

        // Numéro auto-incrémenté selon le nombre de séance du candidat
        int numero = c.getNbSC() + 1;
        System.out.println("Numéro automatique attribué à cette séance : " + numero);

        System.out.print("Date (AAAA-MM-JJ) : ");
        String dateStr = sc.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        System.out.print("Heure de début (HH:MM) : ");
        String heureStr = sc.nextLine();
        LocalTime heure = LocalTime.parse(heureStr);

        System.out.print("Prix de la séance : ");
        float prix = sc.nextFloat();
        sc.nextLine(); // évacuer le retour à la ligne

        // Création de l'objet
        Seance seance = new SeanceCode(numero, date, heure, m, prix, c);

        // Incrément du nombre de séances du candidat
        c.setNbSC(c.getNbSC() + 1);

        // Mise à jour du montant à payer
        c.setMontantApaye(c.getMontantApaye() + prix);

        System.out.println("Séance créée : " + seance);

        // sc.close(); // pas ici !

        return seance;
    }
}