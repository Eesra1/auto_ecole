package entites;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Représente une séance de type examen (examen de code ou examen de conduite).
 * Prix fixés :
 *   - examen code : 45 DT
 *   - examen conduite : 150 DT
 */
public class SeanceExamen extends Seance {

    private Type examenType;
    private static final float PRIX_EXAMEN_CODE = 45.0f;
    private static final float PRIX_EXAMEN_CONDUITE = 150.0f;

    public SeanceExamen(int numero, LocalDate date, LocalTime heure, Moniteur moniteur, Condidat condidat, Type examenType) {
        super(numero, date, heure, moniteur, condidat);
        this.examenType = examenType;
        if (examenType == Type.Code) {
            setPrix(PRIX_EXAMEN_CODE);
        } else {
            setPrix(PRIX_EXAMEN_CONDUITE);
        }
    }

    public Type getExamenType() {
        return examenType;
    }

    public void setExamenType(Type examenType) {
        this.examenType = examenType;
        if (examenType == Type.Code) {
            setPrix(PRIX_EXAMEN_CODE);
        } else {
            setPrix(PRIX_EXAMEN_CONDUITE);
        }
    }

    @Override
    public String toString() {
        return "Séance Examen (" + examenType + ") : " + super.toString();
    }
}