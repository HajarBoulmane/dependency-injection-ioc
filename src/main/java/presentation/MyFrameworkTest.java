package presentation;

import framework.context.AnnotationApplicationContext;
import framework.context.Context;
import metier.IMetier;

public class MyFrameworkTest {
    public static void main(String[] args) {
        // Scan your packages just like Spring's @ComponentScan
        Context context = new AnnotationApplicationContext("dao", "metier");

        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Result: " + metier.calcul());
    }
}