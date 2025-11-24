package services;

import entites.Seance;
import org.json.JSONObject;
import org.json.JSONArray;
import repositories.SeanceRepositorie;

public class SeanceService {
    /** Affiche la séance à partir de son numéro */
    public static void afficherSeanceParNumero(int numero) {
        JSONObject obj = SeanceRepositorie.chercherParNumero(numero);

        if (obj == null) {
            System.out.println("❌ Séance n'existe pas !");
            return;
        }

        System.out.println("✔ Séance trouvée : ");
        System.out.println("Numéro : " + obj.getInt("numero"));
        System.out.println("Date : " + obj.getString("date"));
        System.out.println("Heure : " + obj.getString("heure"));
        System.out.println("Moniteur CIN : " + obj.getInt("moniteur_cin"));
        System.out.println("Candidat CIN : " + obj.getInt("condidat_cin"));
        System.out.println("Prix : " + obj.getFloat("prix"));
        System.out.println("Type : " + obj.getString("type"));
        if (obj.has("vehicule_immatriculation") && !obj.isNull("vehicule_immatriculation")) {
            System.out.println("Véhicule : " + obj.getString("vehicule_immatriculation"));
        }
    }

    public static void supprimerSeance(int numero) {
        boolean resultat = SeanceRepositorie.supprimerParNumero(numero);
        if (resultat) {
            System.out.println("✔ Séance supprimée !");
        } else {
            System.out.println("❌ Aucune séance trouvée avec ce numéro.");
        }
    }

    public static void afficherTous() {
        JSONArray arr = SeanceRepositorie.afficherTous();
        if (arr.isEmpty()) {
            System.out.println("Aucune séance trouvée.");
            return;
        }
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            System.out.println("----- Séance " + (i + 1) + " -----");
            System.out.println("Numéro : " + obj.getInt("numero"));
            System.out.println("Type : " + obj.getString("type"));
            System.out.println("Date : " + obj.getString("date"));
            System.out.println("Heure : " + obj.getString("heure"));
            System.out.println("Moniteur CIN : " + obj.getInt("moniteur_cin"));
            System.out.println("Candidat CIN : " + obj.getInt("condidat_cin"));
            System.out.println("Prix : " + obj.getFloat("prix"));
            if (obj.has("vehicule_immatriculation") && !obj.isNull("vehicule_immatriculation")) {
                System.out.println("Véhicule : " + obj.getString("vehicule_immatriculation"));
            }
            System.out.println("--------------------------------");
        }
    }

    /** Modifier une séance si elle existe */
    public static void modifierSeance(int numero, String nouvelleDate, String nouvelleHeure, float nouveauPrix) {
        JSONObject obj = SeanceRepositorie.chercherParNumero(numero);

        if (obj == null) {
            System.out.println("❌ Aucune séance trouvée avec ce numéro.");
            return;
        }

        SeanceRepositorie.modifierSeance(numero, nouvelleDate, nouvelleHeure, nouveauPrix);
        System.out.println("✔ Séance modifiée !");
    }

    public static void ajouterSeance(Seance s) {
        boolean added = SeanceRepositorie.addSeance(s);
        if (added) {
            System.out.println("✔ Séance ajoutée avec succès !");
        } else {
            System.out.println("❌ Une séance avec ce numéro existe déjà.");
        }
    }
}