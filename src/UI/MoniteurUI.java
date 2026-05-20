package UI;

import entites.Moniteur;
import Exeption.MoniteurException;
import repositories.CondidatRepositorie;

import java.util.Scanner;


public class MoniteurUI {

    static Scanner sc = new Scanner(System.in);

    // Méthode pour vérifier si un CIN existe déjà chez les candidats (via repository)
    public static boolean cinExisteChezCondidats(int cin) {
        return CondidatRepositorie.chercherParCin(cin) != null;
    }

    // Méthode finale de création du moniteur
    public static Moniteur creerMoniteur() {
        try {
            System.out.print("Entrez le nom du moniteur : ");
            String nom = sc.nextLine();

            System.out.print("Entrez le mail du moniteur : ");
            String mail = sc.nextLine();

            System.out.print("Entrez le numéro (8 chiffres) : ");
            int numero = sc.nextInt();

            System.out.print("Entrez le CIN (8 chiffres et unique) : ");
            int cin = sc.nextInt();
            sc.nextLine(); // vider buffer

            // Vérification unicité CIN chez les candidats
            if (cinExisteChezCondidats(cin)) {
                System.out.println("❌ Erreur : ce CIN existe déjà chez un candidat !");
                System.out.println("Création du moniteur annulée.");
                return null;
            }

            // Création du moniteur si tout est correct
            Moniteur m = new Moniteur(nom, mail, numero, cin);
            System.out.println("✔ Moniteur créé avec succès !");
            return m;

        } catch (MoniteurException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            System.out.println("Création du moniteur annulée.");
            return null;

        } catch (Exception e) {
            System.out.println("❌ Erreur : entrée invalide !");
            return null;
        }
    }
}