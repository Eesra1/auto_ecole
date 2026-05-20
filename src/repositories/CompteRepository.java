package repositories;

import entites.Compte;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.YearMonth;
import java.util.Map;

public class CompteRepository {
    private static final String FILE_PATH = "compte.json";

    /** Charger le fichier JSON (ou retourner un tableau vide) */
    private static JSONArray loadArray() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            if (sb.toString().isEmpty()) return new JSONArray();
            return new JSONArray(sb.toString());
        } catch (IOException e) {
            return new JSONArray(); // fichier introuvable = liste vide
        }
    }

    /** Sauvegarder le JSONArray dans le fichier JSON */
    private static void saveArray(JSONArray array) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(array.toString(4)); // format indenté
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Sauvegarder le compte dans le fichier JSON */
    public static void sauvegarderCompte(Compte compte) {
        JSONArray arr = new JSONArray();

        // Revenu total par mois
        JSONObject revenusObj = new JSONObject();
        revenusObj.put("type", "revenus");
        JSONObject revenusData = new JSONObject();
        compte.getRevenusParMois().forEach((mois, montant) ->
                revenusData.put(mois.toString(), montant));
        revenusObj.put("data", revenusData);
        arr.put(revenusObj);

        // Dépenses total par mois
        JSONObject depensesObj = new JSONObject();
        depensesObj.put("type", "depenses");
        JSONObject depensesData = new JSONObject();
        compte.getDepensesParMois().forEach((mois, montant) ->
                depensesData.put(mois.toString(), montant));
        depensesObj.put("data", depensesData);
        arr.put(depensesObj);

        // Détails des revenus
        JSONObject detailsRevenusObj = new JSONObject();
        detailsRevenusObj.put("type", "detailsRevenus");
        JSONObject detailsRevenusData = new JSONObject();
        Map<YearMonth, Map<String, Double>> detailsRevenus = compte.getDetailsRevenus();
        for (YearMonth mois : detailsRevenus.keySet()) {
            JSONObject moisDetails = new JSONObject();
            compte.getDetailsRevenusMois(mois)
                    .forEach(moisDetails::put);
            detailsRevenusData.put(mois.toString(), moisDetails);
        }
        detailsRevenusObj.put("data", detailsRevenusData);
        arr.put(detailsRevenusObj);

        // Détails des dépenses
        JSONObject detailsDepensesObj = new JSONObject();
        detailsDepensesObj.put("type", "detailsDepenses");
        JSONObject detailsDepensesData = new JSONObject();
        Map<YearMonth, Map<String, Double>> detailsDepenses = compte.getDetailsDepenses();
        for (YearMonth mois : detailsDepenses.keySet()) {
            JSONObject moisDetails = new JSONObject();
            compte.getDetailsDepensesMois(mois)
                    .forEach(moisDetails::put);
            detailsDepensesData.put(mois.toString(), moisDetails);
        }
        detailsDepensesObj.put("data", detailsDepensesData);
        arr.put(detailsDepensesObj);

        saveArray(arr);
    }

    /** Charger le compte depuis le fichier JSON */
    public static Compte chargerCompte() {
        JSONArray arr = loadArray();
        Compte compte = new Compte();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String type = obj.getString("type");
            JSONObject data = obj.getJSONObject("data");

            switch (type) {
                case "revenus":
                    for (String key : data.keySet()) {
                        YearMonth mois = YearMonth.parse(key);
                        double montant = data.getDouble(key);
                        compte.ajouterRevenuSeance(montant, mois.atDay(1));
                    }
                    break;

                case "depenses":
                    for (String key : data.keySet()) {
                        YearMonth mois = YearMonth.parse(key);
                        double montant = data.getDouble(key);
                        compte.ajouterDepenseSalaire(montant, mois.atDay(1));
                    }
                    break;

                case "detailsRevenus":
                    for (String key : data.keySet()) {
                        YearMonth mois = YearMonth.parse(key);
                        JSONObject moisDetails = data.getJSONObject(key);
                        for (String cat : moisDetails.keySet()) {
                            double montant = moisDetails.getDouble(cat);
                            compte.ajouterDetailRevenuMois(mois, cat, montant);
                        }
                    }
                    break;

                case "detailsDepenses":
                    for (String key : data.keySet()) {
                        YearMonth mois = YearMonth.parse(key);
                        JSONObject moisDetails = data.getJSONObject(key);
                        for (String cat : moisDetails.keySet()) {
                            double montant = moisDetails.getDouble(cat);
                            compte.ajouterDetailDepenseMois(mois, cat, montant);
                        }
                    }
                    break;
            }
        }

        return compte;
    }

    /** Vérifier si le fichier compte existe */
    public static boolean compteExiste() {
        return !loadArray().isEmpty();
    }

    /** Réinitialiser le compte (vider toutes les données) */
    public static void reinitialiserCompte() {
        saveArray(new JSONArray());
        System.out.println("✔ Compte réinitialisé !");
    }

    /** Obtenir le solde total du compte */
    public static double getSoldeTotal() {
        Compte compte = chargerCompte();
        double totalRevenus = compte.getRevenusTotal();
        double totalDepenses = compte.getDepensesTotal();
        return totalRevenus - totalDepenses;
    }
}
