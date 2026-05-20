package UI;

import Exeption.CondidatExption;
import entites.Condidat;
import entites.Permis;

import java.util.Scanner;


public class CondidatUI {
    static Scanner sc = new Scanner(System.in);

    public static Condidat creerCondidat() {

        try {
            System.out.print("Donner le nom du candidat : ");
            String nom = sc.nextLine().trim();

            System.out.print("Donner l'âge : ");
            int age = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Donner le numéro (8 chiffres) : ");
            int numero = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Donner le CIN (8 chiffres) : ");
            int cin = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Donner le mail : ");
            String mail = sc.nextLine().trim();

            // Choix du permis
            System.out.println("Choisir le type de permis :");
            Permis[] values = Permis.values();
            for (int i = 0; i < values.length; i++) {
                System.out.println((i + 1) + " - " + values[i]);
            }
            System.out.print("Votre choix (numéro) : ");
            int choix = Integer.parseInt(sc.nextLine().trim());
            if (choix < 1 || choix > values.length) {
                System.out.println("Choix de permis invalide.");
                return null;
            }
            Permis permis = values[choix - 1];

            // Création du candidat : montantApaye initialisé à 50 par le constructeur simplifié
            return new Condidat(nom, numero, cin, mail, age, permis);

        } catch (CondidatExption e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Erreur : format numérique invalide !");
        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
        }

        return null; // retour null si une erreur s'est produite
    }
}