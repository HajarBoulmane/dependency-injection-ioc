package net.hajar.ioc_projects.pres;

import net.hajar.ioc_projects.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.annotation.Annotation;

public class presSpringAnnotation {
    public static void main(String[] args) {
        ApplicationContext applicationContext=new AnnotationConfigApplicationContext("net.hajar.ioc_projects");
        IMetier metier = applicationContext.getBean(IMetier.class);
        System.out.println("Résultat = " + metier.calcul());

    }
    }

