package UI;
import Exeption.CondidatExption;
import entites.Condidat;

import java.util.Scanner;


public class CondidatUI {
    static Scanner sc = new Scanner(System.in);
    public static Condidat creerCondidat() {

        try {
            System.out.print("Donner le nom du candidat : ");
            String nom = sc.nextLine().trim();

            System.out.print("Donner l'âge : ");
            int age = sc.nextInt();

            System.out.print("Donner le numéro (8 chiffres) : ");
            int numero = sc.nextInt();

            System.out.print("Donner le CIN (8 chiffres) : ");
            int cin = sc.nextInt();

            sc.nextLine(); // vider le buffer
            System.out.print("Donner le mail : ");
            String mail = sc.nextLine().trim();

            // Création du candidat : le constructeur va lancer une exception si invalide
            return new Condidat(nom, numero, cin, mail, age);

        } catch (CondidatExption e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            sc.nextLine(); // vider buffer en cas d’erreur de saisie
        }

        return null; // retour null si une erreur s'est produite
    }


}
