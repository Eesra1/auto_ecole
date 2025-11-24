package UI;

import entites.Moniteur;
import entites.Condidat;
import Exeption.MoniteurException;

import java.util.ArrayList;
import java.util.Scanner;

public class MoniteurUI {

    // Liste des candidats (à connecter avec ta vraie liste)
    public static ArrayList<Condidat> listeCondidats = new ArrayList<>();

    static Scanner sc = new Scanner(System.in);

    // Méthode pour vérifier si un CIN existe déjà chez les candidats
    public static boolean cinExiste(int cin) {
        for (Condidat c : listeCondidats) {
            if (c.getCin() == cin) {
                return true;
            }
        }
        return false;
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

            // Vérification unicité CIN
            if (cinExiste(cin)) {
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
