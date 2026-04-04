package net.hajar.ioc_projects.dao;

import org.springframework.stereotype.Component;
@Component("d")
public class DaoImpl implements IDao{


    public double getData(){
        System.out.println("version base de donnees");
        double temp=25;
        return temp;
    }



}
