package presentation;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresentationSpringXML {
    public static void main(String[] args) {
        // Charger le fichier XML Spring
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Récupérer le bean Metier
        IMetier metier = (IMetier) context.getBean("metier");

        // Appeler la méthode calcul
        System.out.println("Résultat Spring XML : " + metier.calcul());
    }
}