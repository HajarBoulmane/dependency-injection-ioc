package net.hajar.ioc_projects.pres;

import net.hajar.ioc_projects.dao.IDao;
import net.hajar.ioc_projects.metier.IMetier;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import static java.lang.Class.forName;


public class pres2 {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(new File("config.txt"));

        // --- Create DAO object dynamically ---
        String daoClassName = scanner.nextLine();
        Class cDao = Class.forName(daoClassName);       // load class into memory
        IDao dao = (IDao) cDao.newInstance();           // call no-arg constructor

        // --- Create Metier object dynamically (constructor injection) ---
        String metierClassName = scanner.nextLine();
        Class cMetier = Class.forName(metierClassName);
        IMetier metier = (IMetier) cMetier
                .getConstructor(IDao.class)
                .newInstance(dao);

        System.out.println("Résultat = " + metier.calcul());
    }
}
