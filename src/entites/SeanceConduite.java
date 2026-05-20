package entites;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceConduite extends Seance {
    private Vehicule vehicule;
    private static final float PRIX_FIXE = 25.0f;

    public SeanceConduite(int numero, LocalDate date, LocalTime heure, Moniteur moniteur, Vehicule vehicule, Condidat condidat) {
        super(numero, date, heure, moniteur, condidat);
        this.vehicule = vehicule;
        // prix fixé à 25 DT pour la séance de conduite
        setPrix(PRIX_FIXE);
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    @Override
    public String toString() {
        return "Séance de Conduite : " + super.toString() + ", vehicule=" + (vehicule != null ? vehicule.getImmatriculation() : "null");
    }
}