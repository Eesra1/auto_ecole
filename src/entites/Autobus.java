package entites;

import java.time.LocalDate;

public class Autobus extends Vehicule {
    private int capacitePassagers;

    public Autobus(String immatriculation, String marque, String modele,
                   LocalDate dateMiseService, int capacitePassagers) {
        super(immatriculation, marque, modele, dateMiseService);
        this.capacitePassagers = capacitePassagers;
    }

    @Override
    public String getType() {
        return "Autobus";
    }


    public int getCapacitePassagers() {
        return capacitePassagers;
    }

    public void setCapacitePassagers(int capacitePassagers) {
        this.capacitePassagers = capacitePassagers;
    }

    @Override
    public String toString() {
        return String.format("Autobus %s %s - %s (%d places) - %.0f km",
                marque, modele, immatriculation, capacitePassagers, kilometrageTotal);
    }
}
