package entites;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Compte {

    // Stockage du revenu total par mois
    private Map<YearMonth, Double> revenusParMois = new HashMap<>();

    // Stockage des dépenses total par mois
    private Map<YearMonth, Double> depensesParMois = new HashMap<>();

    // Stockage des détails des revenus par mois (catégorie → montant)
    private Map<YearMonth, Map<String, Double>> detailsRevenusParMois = new HashMap<>();

    // Stockage des détails des dépenses par mois (catégorie → montant)
    private Map<YearMonth, Map<String, Double>> detailsDepensesParMois = new HashMap<>();


    /** Ajouter un revenu à un mois donné */
    public void ajouterRevenuSeance(double montant, LocalDate date) {
        YearMonth mois = YearMonth.from(date);
        revenusParMois.put(mois, revenusParMois.getOrDefault(mois, 0.0) + montant);
    }

    /** Ajouter une dépense à un mois donné */
    public void ajouterDepenseSalaire(double montant, LocalDate date) {
        YearMonth mois = YearMonth.from(date);
        depensesParMois.put(mois, depensesParMois.getOrDefault(mois, 0.0) + montant);
    }

    /** Ajouter un détail revenu (catégorie + montant) */
    public void ajouterDetailRevenuMois(YearMonth mois, String categorie, double montant) {
        detailsRevenusParMois.putIfAbsent(mois, new HashMap<>());
        detailsRevenusParMois.get(mois).put(categorie,
                detailsRevenusParMois.get(mois).getOrDefault(categorie, 0.0) + montant);
    }

    /** Ajouter un détail dépense (catégorie + montant) */
    public void ajouterDetailDepenseMois(YearMonth mois, String categorie, double montant) {
        detailsDepensesParMois.putIfAbsent(mois, new HashMap<>());
        detailsDepensesParMois.get(mois).put(categorie,
                detailsDepensesParMois.get(mois).getOrDefault(categorie, 0.0) + montant);
    }


    /** Retourner revenu total par mois */
    public Map<YearMonth, Double> getRevenusParMois() {
        return revenusParMois;
    }

    /** Retourner dépenses total par mois */
    public Map<YearMonth, Double> getDepensesParMois() {
        return depensesParMois;
    }

    /** ✅ Méthode ajoutée : retourner les détails revenus */
    public Map<YearMonth, Map<String, Double>> getDetailsRevenus() {
        return detailsRevenusParMois;
    }

    /** ✅ Méthode ajoutée : retourner les détails dépenses */
    public Map<YearMonth, Map<String, Double>> getDetailsDepenses() {
        return detailsDepensesParMois;
    }

    /** Retourner détails d’un mois donné - revenus */
    public Map<String, Double> getDetailsRevenusMois(YearMonth mois) {
        return detailsRevenusParMois.getOrDefault(mois, new HashMap<>());
    }

    /** Retourner détails d’un mois donné - dépenses */
    public Map<String, Double> getDetailsDepensesMois(YearMonth mois) {
        return detailsDepensesParMois.getOrDefault(mois, new HashMap<>());
    }


    /** Calculer le total des revenus */
    public double getRevenusTotal() {
        return revenusParMois.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    /** Calculer le total des dépenses */
    public double getDepensesTotal() {
        return depensesParMois.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}
