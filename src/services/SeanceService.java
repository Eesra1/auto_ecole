package services;

import UI.SceanceUI;
import entites.Condidat;
import entites.Moniteur;
import entites.Seance;
import entites.SeanceCode;
import entites.SeanceConduite;
import entites.SeanceExamen;
import entites.Vehicule;
import entites.Type;
import entites.Permis;

import org.json.JSONArray;
import org.json.JSONObject;
import repositories.SeanceRepositorie;
import repositories.CondidatRepositorie;
import repositories.MoniteurRepositorie;
import repositories.VehiculeRepository;

import Exeption.CondidatExption;
import Exeption.MoniteurException;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SeanceService {

    private static final Scanner sc = new Scanner(System.in);


    public static void ajouterSeanceInteractive() throws CondidatExption, MoniteurException {
        System.out.println("Quel type de séance ? 1 = CODE, 2 = CONDUITE, 3 = EXAMEN");
        String type = sc.nextLine().trim();
        if (!"1".equals(type) && !"2".equals(type) && !"3".equals(type)) {
            System.out.println("Type invalide.");
            return;
        }

        // Choix candidat
        JSONArray candidatsJson = CondidatRepositorie.afficherTous();
        if (candidatsJson.isEmpty()) {
            System.out.println("Aucun candidat enregistré.");
            return;
        }
        System.out.println("Liste des candidats :");
        for (int i = 0; i < candidatsJson.length(); i++) {
            JSONObject o = candidatsJson.getJSONObject(i);
            System.out.println((i + 1) + " - " + o.optString("nom") + " (CIN: " + o.optInt("cin") + ")");
        }
        System.out.print("Entrer le CIN du candidat choisi : ");
        int cinCand;
        try {
            cinCand = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("CIN invalide.");
            return;
        }
        JSONObject candObj = CondidatRepositorie.chercherParCin(cinCand);
        if (candObj == null) {
            System.out.println("Candidat introuvable.");
            return;
        }

        // Choix moniteur
        JSONArray moniteursJson = MoniteurRepositorie.afficherTous();
        if (moniteursJson.isEmpty()) {
            System.out.println("Aucun moniteur enregistré.");
            return;
        }
        System.out.println("Liste des moniteurs :");
        for (int i = 0; i < moniteursJson.length(); i++) {
            JSONObject o = moniteursJson.getJSONObject(i);
            System.out.println((i + 1) + " - " + o.optString("nom") + " (CIN: " + o.optInt("cin") + ")");
        }
        System.out.print("Entrer le CIN du moniteur choisi : ");
        int cinMon;
        try {
            cinMon = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("CIN invalide.");
            return;
        }
        JSONObject monObj = MoniteurRepositorie.chercherParCin(cinMon);
        if (monObj == null) {
            System.out.println("Moniteur introuvable.");
            return;
        }

        // Si conduite, choix véhicule
        Vehicule vehicule = null;
        String vehImmat = null;
        if ("2".equals(type)) {
            JSONArray vehJson = VehiculeRepository.afficherTous();
            if (vehJson.isEmpty()) {
                System.out.println("Aucun véhicule enregistré.");
                return;
            }
            System.out.println("Liste des véhicules :");
            for (int i = 0; i < vehJson.length(); i++) {
                JSONObject o = vehJson.getJSONObject(i);
                System.out.println((i + 1) + " - " + o.optString("immatriculation") + " (" + o.optString("modele", "") + ")");
            }
            System.out.print("Entrer l'immatriculation du véhicule choisi : ");
            vehImmat = sc.nextLine().trim();
            JSONObject vObj = VehiculeRepository.chercherParImmatriculation(vehImmat);
            if (vObj == null) {
                System.out.println("Véhicule introuvable.");
                return;
            }

            // Build lightweight Vehicule instance
            String imm = vObj.optString("immatriculation", vehImmat);
            String marque = vObj.optString("marque", "");
            String modele = vObj.optString("modele", "");
            LocalDate dateMise = LocalDate.now();
            String dms = vObj.optString("dateMiseService", "");
            if (!dms.isEmpty()) {
                try {
                    dateMise = LocalDate.parse(dms);
                } catch (Exception ignored) { /* keep default */ }
            }
            final JSONObject vObjFinal = vObj;
            vehicule = new Vehicule(imm, marque, modele, dateMise) {
                @Override
                public String getType() {
                    return vObjFinal.optString("type", "CONDUITE");
                }
            };
        }

        // Pour examen : demander s'il s'agit d'un examen de code ou d'un examen de conduite
        Type examenType = null;
        if ("3".equals(type)) {
            System.out.println("Quel type d'examen ? 1 = CODE, 2 = CONDUITE");
            String t = sc.nextLine().trim();
            if ("1".equals(t)) examenType = Type.Code;
            else if ("2".equals(t)) examenType = Type.Conduite;
            else {
                System.out.println("Type d'examen invalide.");
                return;
            }
        }

        // Demande date+heure via UI
        LocalDateTime dt = SceanceUI.ajouterSeance();
        if (dt == null) return;
        LocalDate date = dt.toLocalDate();
        LocalTime heure = dt.toLocalTime();

        // Nouvelle validation : empêcher réservation si la date est antérieure à aujourd'hui
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            System.out.println("La date de la séance ne peut pas être antérieure à la date actuelle.");
            return;
        }
        // Optionnel : empêcher réservation si même jour mais heure passée
        LocalTime nowTime = LocalTime.now();
        if (date.equals(today) && heure.isBefore(nowTime)) {
            System.out.println("L'heure de la séance est déjà passée aujourd'hui.");
            return;
        }

        // Vérifier disponibilité moniteur (service-level check)
        JSONArray conflictMon = SeanceRepositorie.chercherParMoniteurEtDateHeure(cinMon, date.toString(), heure.toString());
        if (conflictMon.length() > 0) {
            System.out.println("Le moniteur est déjà occupé à cette date/heure.");
            return;
        }

        // Pour conduite : vérifier véhicule disponible (service-level check)
        if ("2".equals(type)) {
            JSONArray conflictVeh = SeanceRepositorie.chercherParVehiculeEtDateHeure(vehImmat, date.toString(), heure.toString());
            if (conflictVeh.length() > 0) {
                System.out.println("Le véhicule est déjà utilisé à cette date/heure.");
                return;
            }
        }

        // Reconstruction des entités à partir des JSON (inclut Permis si présent)
        String permisStr = candObj.optString("permis", "");
        Permis permis = Permis.A; // fallback par défaut
        if (permisStr != null && !permisStr.isEmpty() && !"null".equalsIgnoreCase(permisStr)) {
            try {
                permis = Permis.valueOf(permisStr);
            } catch (Exception ignored) {
                // garde la valeur par défaut
            }
        }

        Condidat candidat = new Condidat(
                candObj.optString("nom"),
                candObj.optInt("numero"),
                candObj.optInt("cin"),
                candObj.optString("mail"),
                candObj.optInt("age"),
                candObj.optInt("nbSC", 0),
                (float) candObj.optDouble("montantPaye", 0.0),
                (float) candObj.optDouble("montantApaye", 50.0),
                permis
        );

        Moniteur moniteur = new Moniteur(
                monObj.optString("nom"),
                monObj.optString("mail"),
                monObj.optInt("numero"),
                monObj.optInt("cin")
        );

        // numéro : utiliser un numéro global unique pour éviter les conflits
        int numero = SeanceRepositorie.getNextNumero();

        Seance seance;
        if ("1".equals(type)) {
            seance = new SeanceCode(numero, date, heure, moniteur, candidat);
        } else if ("2".equals(type)) {
            seance = new SeanceConduite(numero, date, heure, moniteur, vehicule, candidat);
        } else {
            // examen
            seance = new SeanceExamen(numero, date, heure, moniteur, candidat, examenType);
        }

        // Persister la séance (repository vérifie encore la disponibilité et le numéro)
        boolean added = SeanceRepositorie.addSeance(seance);
        if (!added) {
            System.out.println("Impossible d'ajouter la séance (numéro en conflit ou moniteur occupé).");
            return;
        }

        // Mettre à jour candidat en mémoire et en persistance
        candidat.setNbSC(candidat.getNbSC() + 1);
        candidat.setMontantApaye(candidat.getMontantApaye() + seance.getPrix());
        persistUpdatedCondidat(candidat);

        System.out.println("✔ Séance ajoutée avec succès !");
        System.out.println(seance);
    }


    public static void ajouterSeanceExamenInteractive(Moniteur moniteurCourant, Condidat condidatCourant) throws CondidatExption, MoniteurException {
        System.out.println("Création d'une séance d'examen");

        JSONObject candObj = null;
        Condidat candidat = null;
        if (condidatCourant != null) {
            // utiliser le candidat courant
            candidat = condidatCourant;
            System.out.println("Candidat courant utilisé : " + candidat.getNom() + " (CIN: " + candidat.getCin() + ")");
        } else {
            // selection candidat
            JSONArray candidatsJson = CondidatRepositorie.afficherTous();
            if (candidatsJson.isEmpty()) {
                System.out.println("Aucun candidat enregistré.");
                return;
            }
            System.out.println("Liste des candidats :");
            for (int i = 0; i < candidatsJson.length(); i++) {
                JSONObject o = candidatsJson.getJSONObject(i);
                System.out.println((i + 1) + " - " + o.optString("nom") + " (CIN: " + o.optInt("cin") + ")");
            }
            System.out.print("Entrer le CIN du candidat choisi : ");
            int cinCand;
            try {
                cinCand = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("CIN invalide.");
                return;
            }
            candObj = CondidatRepositorie.chercherParCin(cinCand);
            if (candObj == null) {
                System.out.println("Candidat introuvable.");
                return;
            }

            // construire Condidat depuis JSON (avec permis)
            String permisStr = candObj.optString("permis", "");
            Permis permis = Permis.A;
            if (permisStr != null && !permisStr.isEmpty() && !"null".equalsIgnoreCase(permisStr)) {
                try {
                    permis = Permis.valueOf(permisStr);
                } catch (Exception ignored) { }
            }

            candidat = new Condidat(
                    candObj.optString("nom"),
                    candObj.optInt("numero"),
                    candObj.optInt("cin"),
                    candObj.optString("mail"),
                    candObj.optInt("age"),
                    candObj.optInt("nbSC", 0),
                    (float) candObj.optDouble("montantPaye", 0.0),
                    (float) candObj.optDouble("montantApaye", 50.0),
                    permis
            );
        }

        JSONObject monObj = null;
        Moniteur moniteur = null;
        if (moniteurCourant != null) {
            moniteur = moniteurCourant;
            System.out.println("Moniteur courant utilisé : " + moniteur.getNom() + " (CIN: " + moniteur.getCin() + ")");
        } else {
            JSONArray moniteursJson = MoniteurRepositorie.afficherTous();
            if (moniteursJson.isEmpty()) {
                System.out.println("Aucun moniteur enregistré.");
                return;
            }
            System.out.println("Liste des moniteurs :");
            for (int i = 0; i < moniteursJson.length(); i++) {
                JSONObject o = moniteursJson.getJSONObject(i);
                System.out.println((i + 1) + " - " + o.optString("nom") + " (CIN: " + o.optInt("cin") + ")");
            }
            System.out.print("Entrer le CIN du moniteur choisi : ");
            int cinMon;
            try {
                cinMon = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("CIN invalide.");
                return;
            }
            monObj = MoniteurRepositorie.chercherParCin(cinMon);
            if (monObj == null) {
                System.out.println("Moniteur introuvable.");
                return;
            }
            moniteur = new Moniteur(
                    monObj.optString("nom"),
                    monObj.optString("mail"),
                    monObj.optInt("numero"),
                    monObj.optInt("cin")
            );
        }

        // Type d'examen
        System.out.println("Quel type d'examen ? 1 = CODE (45 DT), 2 = CONDUITE (150 DT)");
        String t = sc.nextLine().trim();
        Type examenType;
        if ("1".equals(t)) examenType = Type.Code;
        else if ("2".equals(t)) examenType = Type.Conduite;
        else {
            System.out.println("Type d'examen invalide.");
            return;
        }

        // Si examen conduite, choisir véhicule
        Vehicule vehicule = null;
        String vehImmat = null;
        if (examenType == Type.Conduite) {
            JSONArray vehJson = VehiculeRepository.afficherTous();
            if (vehJson.isEmpty()) {
                System.out.println("Aucun véhicule enregistré.");
                return;
            }
            System.out.println("Liste des véhicules :");
            for (int i = 0; i < vehJson.length(); i++) {
                JSONObject o = vehJson.getJSONObject(i);
                System.out.println((i + 1) + " - " + o.optString("immatriculation") + " (" + o.optString("modele", "") + ")");
            }
            System.out.print("Entrer l'immatriculation du véhicule choisi : ");
            vehImmat = sc.nextLine().trim();
            JSONObject vObj = VehiculeRepository.chercherParImmatriculation(vehImmat);
            if (vObj == null) {
                System.out.println("Véhicule introuvable.");
                return;
            }

            String imm = vObj.optString("immatriculation", vehImmat);
            String marque = vObj.optString("marque", "");
            String modele = vObj.optString("modele", "");
            LocalDate dateMise = LocalDate.now();
            String dms = vObj.optString("dateMiseService", "");
            if (!dms.isEmpty()) {
                try {
                    dateMise = LocalDate.parse(dms);
                } catch (Exception ignored) { }
            }
            final JSONObject vObjFinal = vObj;
            vehicule = new Vehicule(imm, marque, modele, dateMise) {
                @Override
                public String getType() {
                    return vObjFinal.optString("type", "CONDUITE");
                }
            };
        }

        // Demande date+heure via UI
        LocalDateTime dt = SceanceUI.ajouterSeance();
        if (dt == null) return;
        LocalDate date = dt.toLocalDate();
        LocalTime heure = dt.toLocalTime();

        // Nouvelle validation : empêcher réservation si la date est antérieure à aujourd'hui
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            System.out.println("La date de la séance ne peut pas être antérieure à la date actuelle.");
            return;
        }
        // Optionnel : empêcher réservation si même jour mais heure passée
        LocalTime nowTime = LocalTime.now();
        if (date.equals(today) && heure.isBefore(nowTime)) {
            System.out.println("L'heure de la séance est déjà passée aujourd'hui.");
            return;
        }

        // Vérifier disponibilité moniteur
        JSONArray conflictMon = SeanceRepositorie.chercherParMoniteurEtDateHeure(moniteur.getCin(), date.toString(), heure.toString());
        if (conflictMon.length() > 0) {
            System.out.println("Le moniteur est déjà occupé à cette date/heure.");
            return;
        }

        // Vérifier disponibilité véhicule si nécessaire
        if (examenType == Type.Conduite && vehImmat != null) {
            JSONArray conflictVeh = SeanceRepositorie.chercherParVehiculeEtDateHeure(vehImmat, date.toString(), heure.toString());
            if (conflictVeh.length() > 0) {
                System.out.println("Le véhicule est déjà utilisé à cette date/heure.");
                return;
            }
        }

        // numéro global unique
        int numero = SeanceRepositorie.getNextNumero();

        Seance seance = new SeanceExamen(numero, date, heure, moniteur, candidat, examenType);

        boolean added = SeanceRepositorie.addSeance(seance);
        if (!added) {
            System.out.println("Impossible d'ajouter la séance (numéro en conflit ou moniteur occupé).");
            return;
        }

        // Mise à jour candidat
        candidat.setNbSC(candidat.getNbSC() + 1);
        candidat.setMontantApaye(candidat.getMontantApaye() + seance.getPrix());
        persistUpdatedCondidat(candidat);

        System.out.println("✔ Séance d'examen ajoutée avec succès !");
        System.out.println(seance);
    }

    private static void persistUpdatedCondidat(Condidat candidat) {
        try {
            Method m2 = CondidatRepositorie.class.getMethod("updateCondidat", Condidat.class);
            m2.invoke(null, candidat);
            return;
        } catch (NoSuchMethodException nsme) {
            // fallback to delete+add below
        } catch (Throwable t) {
            // fallback
        }

        // Fallback: supprimer + ajouter
        CondidatRepositorie.supprimerParCin(candidat.getCin());
        boolean readd = CondidatRepositorie.addCondidat(candidat);
        if (!readd) {
            System.out.println("⚠️ Échec de la persistance de la mise à jour du candidat (fallback).");
        }
    }


    public static void modifierSeanceParCinEtDate(int cinCandidat, String date, String newDate, String newHeure, float newPrix) {
        try {
            JSONArray seances = SeanceRepositorie.chercherParCandidatEtDate(cinCandidat, date);
            if (seances.length() == 0) {
                System.out.println("Aucune séance trouvée pour ce CIN et date.");
                return;
            }

            for (int i = 0; i < seances.length(); i++) {
                JSONObject seanceJson = seances.getJSONObject(i);
                int numero = seanceJson.getInt("numero");

                // Récupérer les données actuelles
                String dateActuelle = seanceJson.getString("date");
                String heureActuelle = seanceJson.getString("heure");
                float prixActuel = (float) seanceJson.getDouble("prix");

                // Appliquer les modifications
                String dateFinale = (newDate != null) ? newDate : dateActuelle;
                String heureFinale = (newHeure != null) ? newHeure : heureActuelle;
                float prixFinal = (newPrix >= 0) ? newPrix : prixActuel;

                // Mettre à jour dans le repository
                SeanceRepositorie.modifierSeance(numero, dateFinale, heureFinale, prixFinal);
            }

            System.out.println("Séance(s) modifiée(s) avec succès !");

        } catch (Exception e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }


    public static void supprimerSeancesParCinEtDate(int cinCandidat, String date) {
        try {
            JSONArray seances = SeanceRepositorie.chercherParCandidatEtDate(cinCandidat, date);
            if (seances.length() == 0) {
                System.out.println("Aucune séance trouvée pour ce CIN et date.");
                return;
            }

            int count = 0;
            for (int i = 0; i < seances.length(); i++) {
                JSONObject seanceJson = seances.getJSONObject(i);
                int numero = seanceJson.getInt("numero");
                if (SeanceRepositorie.supprimerSeance(numero)) {
                    count++;
                }
            }

            System.out.println(count + " séance(s) supprimée(s) avec succès !");

        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }


    public static void supprimerSeancesParCinDateHeure(int cinCandidat, String date, String heure) {
        try {
            if (heure == null || heure.trim().isEmpty()) {
                System.out.println("Heure invalide fournie.");
                return;
            }
            boolean removed = SeanceRepositorie.supprimerParCinDateHeure(cinCandidat, date, heure.trim());
            if (!removed) {
                System.out.println("Aucune séance trouvée pour ce CIN, cette date et cette heure.");
                return;
            }
            System.out.println("Séance(s) supprimée(s) avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }


    public static void afficherSeancesParCinEtDate(int cinCandidat, String date) {
        try {
            JSONArray seances = SeanceRepositorie.chercherParCandidatEtDate(cinCandidat, date);
            if (seances.length() == 0) {
                System.out.println("Aucune séance trouvée pour ce CIN et date.");
                return;
            }

            System.out.println("\n=== Séances trouvées ===");
            for (int i = 0; i < seances.length(); i++) {
                JSONObject seance = seances.getJSONObject(i);
                System.out.println("Séance " + (i + 1) + ":");
                System.out.println("  Numéro: " + seance.getInt("numero"));
                System.out.println("  Date: " + seance.getString("date"));
                System.out.println("  Heure: " + seance.getString("heure"));
                System.out.println("  Prix: " + seance.getDouble("prix"));
                System.out.println("  Type: " + seance.getString("type"));
                System.out.println("  CIN Moniteur: " + seance.getInt("moniteur_cin"));
                if (seance.has("vehicule_immatriculation") && !seance.isNull("vehicule_immatriculation")) {
                    System.out.println("  Véhicule: " + seance.getString("vehicule_immatriculation"));
                }
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
        }
    }

    /**
     * Afficher toutes les séances
     */
    public static void afficherTous() {
        try {
            JSONArray seances = SeanceRepositorie.afficherTous();
            if (seances.length() == 0) {
                System.out.println("Aucune séance enregistrée.");
                return;
            }

            System.out.println("\n=== Toutes les séances ===");
            for (int i = 0; i < seances.length(); i++) {
                JSONObject seance = seances.getJSONObject(i);
                System.out.println("Séance " + (i + 1) + ":");
                System.out.println("  Numéro: " + seance.getInt("numero"));
                System.out.println("  Date: " + seance.getString("date"));
                System.out.println("  Heure: " + seance.getString("heure"));
                System.out.println("  Prix: " + seance.getDouble("prix"));
                System.out.println("  Type: " + seance.getString("type"));
                System.out.println("  CIN Candidat: " + seance.getInt("condidat_cin"));
                System.out.println("  CIN Moniteur: " + seance.getInt("moniteur_cin"));
                if (seance.has("vehicule_immatriculation") && !seance.isNull("vehicule_immatriculation")) {
                    System.out.println("  Véhicule: " + seance.getString("vehicule_immatriculation"));
                }
                if (seance.has("examen_type") && !seance.isNull("examen_type")) {
                    System.out.println("  Type d'examen: " + seance.getString("examen_type"));
                }
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de l'affichage : " + e.getMessage());
        }
    }


    public static void afficherNbSeancesParMoniteur() {
        try {
            JSONArray stats = SeanceRepositorie.compterSeancesParMoniteur();
            if (stats.length() == 0) {
                System.out.println("Aucune statistique disponible.");
                return;
            }

            System.out.println("\n=== Nombre de séances par moniteur ===");
            for (int i = 0; i < stats.length(); i++) {
                JSONObject stat = stats.getJSONObject(i);
                System.out.println("Moniteur CIN " + stat.getInt("cinMoniteur") +
                        " : " + stat.getInt("nbSeances") + " séance(s)");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des statistiques : " + e.getMessage());
        }
    }


    public static void afficherNbSeancesParCondidat(int cinCandidat) {
        try {
            int nbSeances = SeanceRepositorie.compterSeancesParCandidat(cinCandidat);
            System.out.println("Le candidat avec CIN " + cinCandidat +
                    " a " + nbSeances + " séance(s)");

        } catch (Exception e) {
            System.out.println("Erreur lors du comptage : " + e.getMessage());
        }
    }
}