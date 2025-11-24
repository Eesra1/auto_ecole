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

    public Seance(int numero, LocalDate date, LocalTime heure, Moniteur moniteur, float prix, Condidat condidat) {
        this.numero = numero;
        this.date = date;
        this.heure = heure;
        this.moniteur = moniteur;
        this.prix = prix;
        this.condidat = condidat;
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

    public void setPrix(float prix) {
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
        return "Séance n°" + numero +
                " | Date : " + date +
                " | Heure : " + heure +
                " | Moniteur : " + (moniteur != null ? moniteur.getNom() : "Non assigné") +
                " | Candidat : " + (condidat != null ? condidat.getNom() : "Non assigné") +
                " | Prix : " + prix;
    }
}