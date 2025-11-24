package entites;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceCode extends Seance {

    public SeanceCode(int numero, LocalDate date, LocalTime heure, Moniteur moniteur, float prix, Condidat condidat) {
        super(numero, date, heure, moniteur, prix, condidat);
    }

    @Override
    public String toString() {
        return "Séance de Code : " + super.toString();
    }
}