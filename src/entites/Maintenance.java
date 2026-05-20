package entites;

import java.time.LocalDate;

public class Maintenance {
    private String description;
    private LocalDate date;
    private double cout;
    private String preuve; // Chemin vers le scan de la facture
    private String type; // Entretien, Réparation, Vidange, etc.
    private String immatriculationVehicule;

    public Maintenance(String description, LocalDate date, double cout, String immatriculationVehicule) {
        this.description = description;
        this.date = date;
        this.cout = cout;
        this.immatriculationVehicule = immatriculationVehicule;
        this.type = "Entretien"; // Par défaut
    }

    public Maintenance(String description, LocalDate date, double cout,
                       String immatriculationVehicule, String type) {
        this.description = description;
        this.date = date;
        this.cout = cout;
        this.immatriculationVehicule = immatriculationVehicule;
        this.type = type;
    }

    // GETTERS
    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getCout() {
        return cout;
    }

    public String getPreuve() {
        return preuve;
    }

    public String getType() {
        return type;
    }

    public String getImmatriculationVehicule() {
        return immatriculationVehicule;
    }

    // SETTERS
    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public void setPreuve(String preuve) {
        this.preuve = preuve;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImmatriculationVehicule(String immatriculationVehicule) {
        this.immatriculationVehicule = immatriculationVehicule;
    }

    @Override
    public String toString() {
        return String.format("Maintenance [%s] - %s - %.2f€ - %s",
                date, description, cout, type);
    }
}
