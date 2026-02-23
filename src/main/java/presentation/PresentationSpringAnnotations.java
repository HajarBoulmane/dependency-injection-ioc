package presentation;

import metier.IMetier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"dao", "metier"})
public class PresentationSpringAnnotations {
    public static void main(String[] args) {
        // Créer le contexte Spring
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PresentationSpringAnnotations.class);

        // Récupérer le bean Metier
        IMetier metier = context.getBean(IMetier.class);

        // Appeler la méthode calcul
        System.out.println("Résultat Spring Annotations : " + metier.calcul());

        context.close();
    }
}