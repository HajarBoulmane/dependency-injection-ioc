package presentation;

import dao.DaoImpl;
import metier.MetierImpl;

public class PresentationStatique {
    public static void main(String[] args) {
        DaoImpl dao = new DaoImpl();           // Création statique
        MetierImpl metier = new MetierImpl(); // Injection par constructeur
        System.out.println(metier.calcul());
    }
}