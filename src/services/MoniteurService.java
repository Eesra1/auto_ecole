package services;

import entites.Moniteur;
import org.json.JSONObject;
import org.json.JSONArray;
import repositories.MoniteurRepositorie;

public class MoniteurService {

    /** Afficher un moniteur par CIN */
    public static void afficherMoniteurParCin(int cin) {

        JSONObject obj = MoniteurRepositorie.chercherParCin(cin);

        if (obj == null) {
            System.out.println("❌ Moniteur n'existe pas !");
            return;
        }

        System.out.println("✔ Moniteur trouvé : ");
        System.out.println("Nom : " + obj.getString("nom"));
        System.out.println("Numéro : " + obj.getInt("numero"));
        System.out.println("CIN : " + obj.getInt("cin"));
        System.out.println("Mail : " + obj.getString("mail"));
    }


    /** Supprimer un moniteur */
    public static void supprimerMoniteur(int cin) {

        boolean resultat = MoniteurRepositorie.supprimerParCin(cin);

        if (resultat) {
            System.out.println("✔ Moniteur supprimé !");
        } else {
            System.out.println("❌ Aucun moniteur trouvé avec ce CIN.");
        }
    }


    /** Afficher tous les moniteurs */
    public static void afficherTous() {

        JSONArray arr = MoniteurRepositorie.afficherTous();

        if (arr.isEmpty()) {
            System.out.println("Aucun moniteur trouvé.");
            return;
        }

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            System.out.println("----- Moniteur " + (i + 1) + " -----");
            System.out.println("Nom : " + obj.getString("nom"));
            System.out.println("Numéro : " + obj.getInt("numero"));
            System.out.println("CIN : " + obj.getInt("cin"));
            System.out.println("Mail : " + obj.getString("mail"));
            System.out.println("--------------------------------");
        }
    }


    /** Modifier un moniteur si il existe */
    public static void modifierMoniteur(int cin, String mail, int numero) {

        JSONObject obj = MoniteurRepositorie.chercherParCin(cin);

        if (obj == null) {
            System.out.println("❌ Aucun moniteur trouvé avec ce CIN.");
            return;
        }

        MoniteurRepositorie.modifierMoniteur(cin, mail, numero);
        System.out.println("✔ Moniteur modifié !");
    }


    /** Ajouter un moniteur */
    public static void ajouterMoniteur(Moniteur m) {

        boolean added = MoniteurRepositorie.addMoniteur(m);

        if (added) {
            System.out.println("✔ Moniteur ajouté avec succès !");
        } else {
            System.out.println("❌ Ce moniteur existe déjà (CIN en double).");
        }
    }

}
