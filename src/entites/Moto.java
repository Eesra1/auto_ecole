package entites;

import java.time.LocalDate;

public class Moto extends Vehicule {
    private int cylindree;

    public Moto(String immatriculation, String marque, String modele,
                LocalDate dateMiseService, int cylindree) {
        super(immatriculation, marque, modele, dateMiseService);
        this.cylindree = cylindree;
    }

    public int getCylindree() {
        return cylindree;
    }

    public void setCylindree(int cylindree) {
        this.cylindree = cylindree;
    }

    @Override
    public String getType() {
        return "Moto";
    }
}

