package entites;

public class Person {
    protected String nom,mail;
    protected int numero,cin;

    public Person(String nom,  int numero, int cin ,String mail) {
        this.nom = nom;
        this.mail = mail;
        this.numero = numero;
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }
}
