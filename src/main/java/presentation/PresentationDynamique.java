package presentation;

import dao.IDao;
import metier.MetierImpl;

public class PresentationDynamique {
    public static void main(String[] args) throws Exception {
        String daoClassName = "dao.DaoImpl"; // Classe donnée en chaîne
        IDao dao = (IDao) Class.forName(daoClassName)
                .getDeclaredConstructor().newInstance();
        MetierImpl metier = new MetierImpl(); // Injection par constructeur
        System.out.println(metier.calcul());
    }
}