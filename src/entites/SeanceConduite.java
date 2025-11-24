package entites;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceConduite extends Seance {
    private Vehicule vehicule;

    public SeanceConduite(int numero, LocalDate date, LocalTime heure, Moniteur moniteur, Vehicule vehicule, float prix, Condidat condidat) {
        super(numero, date, heure, moniteur, prix, condidat);
        this.vehicule = vehicule;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Véhicule : " + (vehicule != null ? vehicule.getImmatriculation() : "Non assigné");
    }
}