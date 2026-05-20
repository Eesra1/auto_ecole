package services;

import entites.Condidat;
import entites.Permis;
import org.json.JSONObject;
import org.json.JSONArray;
import repositories.CondidatRepositorie;
import repositories.MoniteurRepositorie;
import Exeption.CondidatExption;


public class CondidatService {
    public static void afficherCondidatParCin(int cin) {

        JSONObject obj = CondidatRepositorie.chercherParCin(cin);

        if (obj == null) {
            System.out.println(" Condidat n'existe pas !");
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
        if (obj.has("permis") && !obj.isNull("permis")) {
            System.out.println("Permis : " + obj.getString("permis"));
        }
    }
    public static void supprimerCondidat(int cin) {

        boolean resultat = CondidatRepositorie.supprimerParCin(cin);

        if (resultat) {
            System.out.println("✔ Condidat supprimé !");
        } else {
            System.out.println(" Aucun condidat trouvé avec ce CIN.");
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
            if (obj.has("permis") && !obj.isNull("permis")) {
                System.out.println("Permis : " + obj.getString("permis"));
            }
            System.out.println("--------------------------------");
        }
    }
    /** Modifier un candidat si il existe */
    public static void modifierCondidat(int cin, String mail, int age) {

        JSONObject obj = CondidatRepositorie.chercherParCin(cin);

        if (obj == null) {
            System.out.println(" Aucun candidat trouvé avec ce CIN.");
            return;
        }

        CondidatRepositorie.modifierCondidat(cin, mail, age);
        System.out.println("✔ Candidat modifié !");
    }

    public static void ajouterCondidat(Condidat c) {

        // Vérifier si CIN existe déjà chez un moniteur pour afficher message explicite
        if (MoniteurRepositorie.chercherParCin(c.getCin()) != null) {
            System.out.println(" Impossible d'ajouter : ce CIN est déjà utilisé par un moniteur.");
            return;
        }

        boolean added = CondidatRepositorie.addCondidat(c);

        if (added) {
            System.out.println("✔ Candidat ajouté avec succès !");
        } else {
            System.out.println(" Ce candidat existe déjà (CIN en double) ou CIN utilisé par un moniteur.");
        }
    }

    /**
     * Enregistre un paiement pour le candidat identifié par son CIN.
     * - ajoute le montant au montantPaye
     * - décrémente montantApaye (ne devient pas négatif)
     */
    public static void payerCondidat(int cin, float montant) {
        if (montant <= 0f) {
            System.out.println("Montant invalide (doit être > 0).");
            return;
        }

        JSONObject obj = CondidatRepositorie.chercherParCin(cin);
        if (obj == null) {
            System.out.println(" Aucun candidat trouvé avec ce CIN.");
            return;
        }

        // Lire les champs existants
        String nom = obj.optString("nom");
        int numero = obj.optInt("numero");
        String mail = obj.optString("mail");
        int age = obj.optInt("age");
        int nbSC = obj.optInt("nbSC", 0);
        float montantPaye = (float) obj.optDouble("montantPaye", 0.0);
        float montantApaye = (float) obj.optDouble("montantApaye", 0.0);
        String permisStr = obj.optString("permis", "");
        Permis permis = null;
        if (permisStr != null && !permisStr.isEmpty() && !"null".equalsIgnoreCase(permisStr)) {
            try {
                permis = Permis.valueOf(permisStr);
            } catch (Exception ignored) { /* keep null */ }
        }

        try {
            // Construire l'objet Condidat pour utiliser updateCondidat
            Condidat candidat = new Condidat(nom, numero, cin, mail, age, nbSC, montantPaye, montantApaye, permis);

            // Appliquer le paiement
            float nouveauMontantPaye = candidat.getMontantPaye() + montant;
            float nouveauMontantApaye = candidat.getMontantApaye() - montant;
            if (nouveauMontantApaye < 0f) nouveauMontantApaye = 0f;

            candidat.setMontantPaye(nouveauMontantPaye);
            candidat.setMontantApaye(nouveauMontantApaye);

            boolean updated = CondidatRepositorie.updateCondidat(candidat);
            if (updated) {
                System.out.println("✔ Paiement enregistré : " + montant + " DT");
                System.out.println("Montant payé total : " + candidat.getMontantPaye() + " DT");
                System.out.println("Montant restant à payer : " + candidat.getMontantApaye() + " DT");
            } else {
                System.out.println(" Échec de la mise à jour du candidat.");
            }
        } catch (CondidatExption e) {
            System.out.println(" Erreur données candidat : " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Erreur inattendue : " + e.getMessage());
        }
    }

}