package entites;

import Exeption.CondidatExption;

/**
 * Représentation d'un candidat.
 * Maintenant avec le champ Permis et montantApaye initialisé par défaut à 50 DT (si constructeur simple utilisé).
 */
public class Condidat extends Person {
    private int nbSC;
    private int age;
    private float montantPaye;
    private float montantApaye;
    private Permis permis;

    /**
     * Constructeur principal : tous les champs.
     * montantApaye doit être fourni quand on veut rétablir depuis la persistence.
     */
    public Condidat(String nom, int numero, int cin, String mail, int age,
                    int nbSSc, float montantPaye, float montantApaye, Permis permis) throws CondidatExption {
        super(nom, numero, cin, mail);

        if (age < 18) {
            throw new CondidatExption("Âge invalide : le candidat doit avoir au moins 18 ans !");
        }

        // ----- Vérification numéro (8 chiffres) -----
        if (String.valueOf(numero).length() != 8) {
            throw new CondidatExption("Numéro invalide : il doit contenir exactement 8 chiffres !");
        }

        // ----- Vérification CIN (8 chiffres) -----
        if (String.valueOf(cin).length() != 8) {
            throw new CondidatExption("CIN invalide : il doit contenir exactement 8 chiffres !");
        }

        // ----- Vérification mail -----
        if (!mail.endsWith("@gmail.com")) {
            throw new CondidatExption("Email invalide : il doit se terminer par @gmail.com !");
        }

        this.age = age;
        this.nbSC = nbSSc;
        this.montantPaye = montantPaye;
        this.montantApaye = montantApaye;
        this.permis = permis;
    }

    /**
     * Constructeur simplifié utilisé à la création : initialise montantPaye = 0 et montantApaye = 50 (acompte initial).
     */
    public Condidat(String nom, int numero, int cin, String mail, int age, Permis permis) throws CondidatExption {
        this(nom, numero, cin, mail, age, 0, 0f, 50f, permis);
    }

    // Getters / setters
    public int getNbSC() {
        return nbSC;
    }

    public void setNbSC(int nbSC) {
        this.nbSC = nbSC;
    }

    public float getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(float montantPaye) {
        this.montantPaye = montantPaye;
    }

    public float getMontantApaye() {
        return montantApaye;
    }

    public void setMontantApaye(float montantApaye) {
        this.montantApaye = montantApaye;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Permis getPermis() {
        return permis;
    }

    public void setPermis(Permis permis) {
        this.permis = permis;
    }
}