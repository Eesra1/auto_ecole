package entites;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Seance {
    private int numero;
    private LocalDate date;
    private LocalTime heure;
    private Moniteur moniteur;
    private float prix;
    private Condidat condidat; // Ajout du candidat

    /**
     * Constructeur historique (reste disponible si utilisé ailleurs).
     */
    public Seance(int numero, LocalDate date, LocalTime heure, Moniteur moniteur, float prix, Condidat condidat) {
        this.numero = numero;
        this.date = date;
        this.heure = heure;
        this.moniteur = moniteur;
        this.prix = prix;
        this.condidat = condidat;
    }

    /**
     * Nouveau constructeur sans prix : les sous-classes fixeront le prix.
     */
    public Seance(int numero, LocalDate date, LocalTime heure, Moniteur moniteur, Condidat condidat) {
        this(numero, date, heure, moniteur, 0f, condidat);
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public Moniteur getMoniteur() {
        return moniteur;
    }

    public void setMoniteur(Moniteur moniteur) {
        this.moniteur = moniteur;
    }

    public float getPrix() {
        return prix;
    }

    /**
     * Protégé pour que seules les sous-classes puissent fixer le prix.
     */
    protected void setPrix(float prix) {
        this.prix = prix;
    }

    public Condidat getCondidat() {
        return condidat;
    }

    public void setCondidat(Condidat condidat) {
        this.condidat = condidat;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "numero=" + numero +
                ", date=" + date +
                ", heure=" + heure +
                ", moniteur=" + (moniteur != null ? moniteur.getNom() : "null") +
                ", prix=" + prix + " DT" +
                ", condidat=" + (condidat != null ? condidat.getNom() : "null") +
                '}';
    }
}