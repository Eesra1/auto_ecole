package entites;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Vehicule {
    protected String immatriculation;
    protected String marque;
    protected String modele;
    protected LocalDate dateMiseService;
    protected double kilometrageTotal;
    protected double kmProchainEntretien;
    protected LocalDate dateVignette;
    protected LocalDate dateVisiteTechnique;
    protected LocalDate dateAssurance;
    protected boolean enService;
    protected List<Maintenance> maintenances;

    public Vehicule(String immatriculation, String marque, String modele,
                    LocalDate dateMiseService) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.dateMiseService = dateMiseService;
        this.kilometrageTotal = 0;
        this.kmProchainEntretien = 10000; // Par défaut
        this.enService = true;
        this.maintenances = new ArrayList<>();

        // Initialiser les dates d'échéance
        LocalDate maintenant = LocalDate.now();
        this.dateVignette = maintenant.plusYears(1);
        this.dateVisiteTechnique = maintenant.plusYears(1);
        this.dateAssurance = maintenant.plusYears(1);
    }


    public abstract String getType();


    public boolean besoinEntretien() {
        return kilometrageTotal >= kmProchainEntretien;
    }


    public void ajouterKilometrage(double km) {
        this.kilometrageTotal += km;
    }


    public void effectuerEntretien(String description, double cout) {
        // Maintenance constructor expects a String as the 4th parameter (e.g. 'preuve'),
        // so pass an empty string here. For repairs we set preuve after creating the object.
        Maintenance entretien = new Maintenance("Entretien: " + description,
                LocalDate.now(), cout, "");
        maintenances.add(entretien);
        this.kmProchainEntretien = kilometrageTotal + 10000; // Réinitialise le compteur
    }


    public void effectuerReparation(String description, double cout, String preuve) {
        // Same: pass empty string for the constructor, then set the preuve.
        Maintenance reparation = new Maintenance("Réparation: " + description,
                LocalDate.now(), cout, "");
        reparation.setPreuve(preuve);
        maintenances.add(reparation);
    }


    public boolean echeanceProche() {
        LocalDate maintenant = LocalDate.now();
        LocalDate dansSixMois = maintenant.plusMonths(6);

        return dateVignette.isBefore(dansSixMois) ||
                dateVisiteTechnique.isBefore(dansSixMois) ||
                dateAssurance.isBefore(dansSixMois);
    }

    // Calcule l'âge du véhicule en années
    public int getAge() {
        return LocalDate.now().getYear() - dateMiseService.getYear();
    }


    public String getImmatriculation() { return immatriculation; }
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public LocalDate getDateMiseService() { return dateMiseService; }
    public double getKilometrageTotal() { return kilometrageTotal; }
    public double getKmProchainEntretien() { return kmProchainEntretien; }
    public LocalDate getDateVignette() { return dateVignette; }
    public LocalDate getDateVisiteTechnique() { return dateVisiteTechnique; }
    public LocalDate getDateAssurance() { return dateAssurance; }
    public boolean isEnService() { return enService; }
    public List<Maintenance> getMaintenances() { return maintenances; }

    public void setKmProchainEntretien(double kmProchainEntretien) {
        this.kmProchainEntretien = kmProchainEntretien;
    }
    public void setDateVignette(LocalDate dateVignette) {
        this.dateVignette = dateVignette;
    }
    public void setDateVisiteTechnique(LocalDate dateVisiteTechnique) {
        this.dateVisiteTechnique = dateVisiteTechnique;
    }
    public void setDateAssurance(LocalDate dateAssurance) {
        this.dateAssurance = dateAssurance;
    }
    public void setEnService(boolean enService) {
        this.enService = enService;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s (%s) - %.0f km",
                marque, modele, immatriculation, getType(), kilometrageTotal);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicule vehicule = (Vehicule) obj;
        return immatriculation.equals(vehicule.immatriculation);
    }

    @Override
    public int hashCode() {
        return immatriculation.hashCode();
    }
}