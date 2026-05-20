package entites;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceCode extends Seance {

    private static final float PRIX_FIXE = 15.0f;

    public SeanceCode(int numero, LocalDate date, LocalTime heure, Moniteur moniteur, Condidat condidat) {
        super(numero, date, heure, moniteur, condidat);
        // prix fixé à 15 DT pour la séance de code
        setPrix(PRIX_FIXE);
    }

    @Override
    public String toString() {
        return "Séance de Code : " + super.toString();
    }
}