package services;

import entites.Compte;
import repositories.CompteRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class CompteService {

    /** Afficher le bilan d'un mois donné */
    public static void afficherBilanMois(int annee, int mois) {
        Compte compte = CompteRepository.chargerCompte();
        YearMonth yearMonth = YearMonth.of(annee, mois);

        double revenus = compte.getRevenusParMois().getOrDefault(yearMonth, 0.0);
        double depenses = compte.getDepensesParMois().getOrDefault(yearMonth, 0.0);
        double benefice = revenus - depenses;

        System.out.println("\n=== BILAN COMPTABLE " + yearMonth + " ===");
        System.out.printf("Revenus  : %.2f €\n", revenus);
        System.out.printf("Dépenses : %.2f €\n", depenses);
        System.out.printf("Bénéfice : %.2f €\n", benefice);

        if (benefice > 0) {
            System.out.println("✅ Bénéfice positif");
        } else if (benefice < 0) {
            System.out.println("❌ Déficit");
        } else {
            System.out.println("⚖️  Équilibre");
        }
    }

    /** Afficher le bilan complet trié par mois */
    public static void afficherBilanComplet() {
        Compte compte = CompteRepository.chargerCompte();
        Map<YearMonth, Double> revenus = compte.getRevenusParMois();
        Map<YearMonth, Double> depenses = compte.getDepensesParMois();

        if (revenus.isEmpty() && depenses.isEmpty()) {
            System.out.println("Aucune donnée comptable disponible.");
            return;
        }

        System.out.println("\n=== BILAN COMPTABLE COMPLET ===");
        System.out.printf("%-12s %-12s %-12s %-12s\n", "MOIS", "REVENUS", "DÉPENSES", "BÉNÉFICE");
        System.out.println("------------------------------------------------");

        Set<YearMonth> tousLesMois = new HashSet<>();
        tousLesMois.addAll(revenus.keySet());
        tousLesMois.addAll(depenses.keySet());

        List<YearMonth> moisTries = new ArrayList<>(tousLesMois);
        Collections.sort(moisTries);

        double totalRevenus = 0, totalDepenses = 0, totalBenefice = 0;

        for (YearMonth mois : moisTries) {
            double rev = revenus.getOrDefault(mois, 0.0);
            double dep = depenses.getOrDefault(mois, 0.0);
            double ben = rev - dep;

            System.out.printf("%-12s %-12.2f %-12.2f %-12.2f\n", mois, rev, dep, ben);

            totalRevenus += rev;
            totalDepenses += dep;
            totalBenefice += ben;
        }

        System.out.println("------------------------------------------------");
        System.out.printf("%-12s %-12.2f %-12.2f %-12.2f\n", "TOTAL", totalRevenus, totalDepenses, totalBenefice);
    }

    /** Afficher les détails d'un mois donné */
    public static void afficherDetailsMois(int annee, int mois) {
        Compte compte = CompteRepository.chargerCompte();
        YearMonth yearMonth = YearMonth.of(annee, mois);

        Map<String, Double> detailsRevenus = compte.getDetailsRevenusMois(yearMonth);
        Map<String, Double> detailsDepenses = compte.getDetailsDepensesMois(yearMonth);

        if (detailsRevenus.isEmpty() && detailsDepenses.isEmpty()) {
            System.out.println("Aucune donnée détaillée pour " + yearMonth);
            return;
        }

        System.out.println("\n=== DÉTAILS COMPTABLES " + yearMonth + " ===");

        if (!detailsRevenus.isEmpty()) {
            System.out.println("\n--- REVENUS ---");
            detailsRevenus.forEach((type, montant) ->
                    System.out.printf("  %-15s : %8.2f €\n", type, montant));
        }

        if (!detailsDepenses.isEmpty()) {
            System.out.println("\n--- DÉPENSES ---");
            detailsDepenses.forEach((type, montant) ->
                    System.out.printf("  %-15s : %8.2f €\n", type, montant));
        }

        double revenus = compte.getRevenusParMois().getOrDefault(yearMonth, 0.0);
        if (revenus > 0) {
            double ratio = (compte.getDepensesParMois().getOrDefault(yearMonth, 0.0) / revenus) * 100;
            System.out.printf("\nRatio dépenses/revenus : %.1f%%\n", ratio);
        }
    }

    // ---------- AJOUTS ------------

    public static void ajouterRevenuSeance(double montant, LocalDate date) {
        Compte compte = CompteRepository.chargerCompte();
        compte.ajouterRevenuSeance(montant, date);
        CompteRepository.sauvegarderCompte(compte);
        System.out.println("✔ Revenu séance ajouté avec succès !");
    }

    public static void ajouterRevenuExamen(double montant, LocalDate date) {
        Compte compte = CompteRepository.chargerCompte();
        compte.ajouterDetailRevenuMois(YearMonth.from(date), "Examen", montant);
        CompteRepository.sauvegarderCompte(compte);
        System.out.println("✔ Revenu examen ajouté avec succès !");
    }

    public static void ajouterRevenuInscription(double montant, LocalDate date) {
        Compte compte = CompteRepository.chargerCompte();
        compte.ajouterDetailRevenuMois(YearMonth.from(date), "Inscription", montant);
        CompteRepository.sauvegarderCompte(compte);
        System.out.println("✔ Revenu inscription ajouté avec succès !");
    }

    public static void ajouterDepenseSalaire(double montant, LocalDate date) {
        Compte compte = CompteRepository.chargerCompte();
        compte.ajouterDepenseSalaire(montant, date);
        CompteRepository.sauvegarderCompte(compte);
        System.out.println("✔ Dépense salaire ajoutée avec succès !");
    }

    public static void ajouterDepenseMaintenance(double montant, LocalDate date) {
        Compte compte = CompteRepository.chargerCompte();
        compte.ajouterDetailDepenseMois(YearMonth.from(date), "Maintenance", montant);
        CompteRepository.sauvegarderCompte(compte);
        System.out.println("✔ Dépense maintenance ajoutée avec succès !");
    }

    public static void ajouterDepenseCarburant(double montant, LocalDate date) {
        Compte compte = CompteRepository.chargerCompte();
        compte.ajouterDetailDepenseMois(YearMonth.from(date), "Carburant", montant);
        CompteRepository.sauvegarderCompte(compte);
        System.out.println("✔ Dépense carburant ajoutée avec succès !");
    }

    public static void ajouterDepenseAssurance(double montant, LocalDate date) {
        Compte compte = CompteRepository.chargerCompte();
        compte.ajouterDetailDepenseMois(YearMonth.from(date), "Assurance", montant);
        CompteRepository.sauvegarderCompte(compte);
        System.out.println("✔ Dépense assurance ajoutée avec succès !");
    }

    /** Afficher des statistiques */
    public static void afficherStatistiques() {
        Compte compte = CompteRepository.chargerCompte();

        Optional<Map.Entry<YearMonth, Double>> meilleurRev =
                compte.getRevenusParMois().entrySet().stream().max(Map.Entry.comparingByValue());

        Optional<YearMonth> meilleurBen =
                compte.getRevenusParMois().keySet().stream()
                        .max(Comparator.comparing(m ->
                                compte.getRevenusParMois().getOrDefault(m, 0.0)
                                        - compte.getDepensesParMois().getOrDefault(m, 0.0)));

        double moyenneRevenus =
                compte.getRevenusParMois().values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        System.out.println("\n=== STATISTIQUES COMPTABLES ===");

        meilleurRev.ifPresent(e ->
                System.out.printf("Meilleur mois (revenus) : %s (%.2f €)\n", e.getKey(), e.getValue()));

        meilleurBen.ifPresent(m ->
                System.out.printf("Meilleur mois (bénéfice) : %s (%.2f €)\n", m,
                        compte.getRevenusParMois().getOrDefault(m, 0.0)
                                - compte.getDepensesParMois().getOrDefault(m, 0.0)));

        System.out.printf("Moyenne revenus : %.2f €\n", moyenneRevenus);
    }
    /** Générer un rapport pour une période (mois/année début → fin) */
    public static void genererRapportPeriode(int anneeDebut, int moisDebut, int anneeFin, int moisFin) {
        Compte compte = CompteRepository.chargerCompte();

        YearMonth debut = YearMonth.of(anneeDebut, moisDebut);
        YearMonth fin = YearMonth.of(anneeFin, moisFin);

        if (debut.isAfter(fin)) {
            System.out.println("❌ La période de début doit être avant la fin !");
            return;
        }

        System.out.println("\n=== RAPPORT COMPTABLE DE " + debut + " À " + fin + " ===");
        System.out.printf("%-12s %-12s %-12s %-12s\n", "MOIS", "REVENUS", "DÉPENSES", "BÉNÉFICE");
        System.out.println("------------------------------------------------");

        double totalRevenus = 0, totalDepenses = 0, totalBenefice = 0;

        YearMonth courant = debut;
        while (!courant.isAfter(fin)) {
            double rev = compte.getRevenusParMois().getOrDefault(courant, 0.0);
            double dep = compte.getDepensesParMois().getOrDefault(courant, 0.0);
            double ben = rev - dep;

            System.out.printf("%-12s %-12.2f %-12.2f %-12.2f\n", courant, rev, dep, ben);

            totalRevenus += rev;
            totalDepenses += dep;
            totalBenefice += ben;

            courant = courant.plusMonths(1);
        }

        System.out.println("------------------------------------------------");
        System.out.printf("%-12s %-12.2f %-12.2f %-12.2f\n", "TOTAL", totalRevenus, totalDepenses, totalBenefice);
    }


}
