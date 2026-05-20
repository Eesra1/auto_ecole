package entites;

import java.time.LocalDate;

public class Voiture extends Vehicule {
    private int nombrePlaces;
    private String carburant; // Essence/Diesel/Électrique

    public Voiture(String immatriculation, String marque, String modele,
                   LocalDate dateMiseService, int nombrePlaces, String carburant) {
        super(immatriculation, marque, modele, dateMiseService);
        this.nombrePlaces = nombrePlaces;
        this.carburant = carburant;
    }

    @Override
    public String getType() {
        return "Voiture";
    }


    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public String getCarburant() {
        return carburant;
    }

    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }

    @Override
    public String toString() {
        return String.format("Voiture %s %s - %s (%d places, %s) - %.0f km",
                marque, modele, immatriculation, nombrePlaces, carburant, kilometrageTotal);
    }
}
