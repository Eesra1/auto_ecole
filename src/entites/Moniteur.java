package entites;

import Exeption.MoniteurException;

public class Moniteur extends Person {

    public Moniteur(String nom, String mail, int numero, int cin) throws MoniteurException {
        super(nom, numero, cin, mail);

        // Vérification email
        if (!mail.endsWith("@gmail.com")) {
            throw new MoniteurException("Email invalide : le mail doit se terminer par @gmail.com");
        }

        // Vérification CIN (8 chiffres)
        if (String.valueOf(cin).length() != 8) {
            throw new MoniteurException("CIN invalide : il doit contenir exactement 8 chiffres.");
        }

        // Vérification numéro (8 chiffres)
        if (String.valueOf(numero).length() != 8) {
            throw new MoniteurException("Numéro invalide : il doit contenir exactement 8 chiffres.");
        }
    }
}
