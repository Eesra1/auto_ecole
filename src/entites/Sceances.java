package entites;

public class Sceances {
    // Jours de la semaine (lundi=0, ..., samedi=5)
    public static final String[] JOURS = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
    // 5 créneaux horaires
    public static final String[] CRENEAUX = {"10h-11h", "11h-12h", "14h-15h", "15h-16h", "16h-17h"};

    private Seance[][] matrice;

    public Sceances() {
        matrice = new Seance[CRENEAUX.length][JOURS.length]; // 5 x 6
    }
    public Seance getSeance(int creneauIndex, int jourIndex) {
        if (creneauIndex < 0 || creneauIndex >= CRENEAUX.length ||
                jourIndex < 0 || jourIndex >= JOURS.length) {
            throw new IllegalArgumentException("Indice de jour ou créneau invalide");
        }
        return matrice[creneauIndex][jourIndex];
    }
}