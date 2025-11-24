package entites;
import Exeption.CondidatExption;
public class Condidat  extends Person{
    private int nbSC,age ;

    private float montantPaye,montantApaye;

    public Condidat(String nom,int numero, int cin, String mail,int age,  int nbSSc,  float montantPaye, float montantApaye) throws CondidatExption {
        super(nom, numero, cin, mail);
        if (age<18){
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

        this.age=age;
        this.nbSC = nbSSc;
        this.montantPaye = montantPaye;
        this.montantApaye = montantApaye;

    }
    public Condidat(String nom, int numero, int cin, String mail ,int age ) throws CondidatExption {
        this(nom, numero, cin, mail, age, 0, 0f, 0f); // appelle le constructeur principal avec des 0
    }

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
}
