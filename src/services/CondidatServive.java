package services;
import entites.Condidat;
import org.json.JSONObject;
import org.json.JSONArray;
import repositories.CondidatRepositorie;

public class CondidatServive {
    public static void afficherCondidatParCin(int cin) {

        JSONObject obj = CondidatRepositorie.chercherParCin(cin);

        if (obj == null) {
            System.out.println("❌ Condidat n'existe pas !");
            return;
        }

        System.out.println("✔ Condidat trouvé : ");
        System.out.println("Nom : " + obj.getString("nom"));
        System.out.println("Numéro : " + obj.getInt("numero"));
        System.out.println("CIN : " + obj.getInt("cin"));
        System.out.println("Mail : " + obj.getString("mail"));
        System.out.println("Age : " + obj.getInt("age"));
        System.out.println("Nombre SC : " + obj.getInt("nbSC"));
        System.out.println("Montant Payé : " + obj.getFloat("montantPaye"));
        System.out.println("Montant à payer : " + obj.getFloat("montantApaye"));
    }
    public static void supprimerCondidat(int cin) {

        boolean resultat = CondidatRepositorie.supprimerParCin(cin);

        if (resultat) {
            System.out.println("✔ Condidat supprimé !");
        } else {
            System.out.println("❌ Aucun condidat trouvé avec ce CIN.");
        }
    }
    public static void afficherTous() {

        JSONArray arr = CondidatRepositorie.afficherTous();

        if (arr.isEmpty()) {
            System.out.println("Aucun candidat trouvé.");
            return;
        }

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            System.out.println("----- Condidat " + (i + 1) + " -----");
            System.out.println("Nom : " + obj.getString("nom"));
            System.out.println("Numéro : " + obj.getInt("numero"));
            System.out.println("CIN : " + obj.getInt("cin"));
            System.out.println("Mail : " + obj.getString("mail"));
            System.out.println("Age : " + obj.getInt("age"));
            System.out.println("Nombre SC : " + obj.getInt("nbSC"));
            System.out.println("Montant Payé : " + obj.getFloat("montantPaye"));
            System.out.println("Montant à Payer : " + obj.getFloat("montantApaye"));
            System.out.println("--------------------------------");
        }
    }
    /** Modifier un candidat si il existe */
    public static void modifierCondidat(int cin, String mail, int age) {

        JSONObject obj = CondidatRepositorie.chercherParCin(cin);

        if (obj == null) {
            System.out.println("❌ Aucun candidat trouvé avec ce CIN.");
            return;
        }

        CondidatRepositorie.modifierCondidat(cin, mail, age);
        System.out.println("✔ Candidat modifié !");
    }
    public static void ajouterCondidat(Condidat c) {

        boolean added = CondidatRepositorie.addCondidat(c);

        if (added) {
            System.out.println("✔ Candidat ajouté avec succès !");
        } else {
            System.out.println("❌ Ce candidat existe déjà (CIN en double).");
        }
    }

}
