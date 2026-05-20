package entites;

import java.time.LocalDate;

public class Camion extends Vehicule {
    private double poidsTotal; // en tonnes

    public Camion(String immatriculation, String marque, String modele,
                  LocalDate dateMiseService, double poidsTotal) {
        super(immatriculation, marque, modele, dateMiseService);
        this.poidsTotal = poidsTotal;
    }

    @Override
    public String getType() {
        return "Camion";
    }


    public double getPoidsTotal() {
        return poidsTotal;
    }

    public void setPoidsTotal(double poidsTotal) {
        this.poidsTotal = poidsTotal;
    }

    @Override
    public String toString() {
        return String.format("Camion %s %s - %s (%.1f tonnes) - %.0f km",
                marque, modele, immatriculation, poidsTotal, kilometrageTotal);
    }
}
