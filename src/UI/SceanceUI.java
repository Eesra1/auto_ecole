package UI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;


public class SceanceUI {


    public static LocalDateTime ajouterSeance() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Date de la séance (YYYY-MM-DD) : ");
        String dateStr = sc.nextLine().trim();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Date invalide : " + e.getMessage());
            return null;
        }

        System.out.print("Heure de la séance (HH:MM) : ");
        String heureStr = sc.nextLine().trim();
        LocalTime heure;
        try {
            heure = LocalTime.parse(heureStr);
        } catch (Exception e) {
            System.out.println("Heure invalide : " + e.getMessage());
            return null;
        }

        return LocalDateTime.of(date, heure);
    }
}