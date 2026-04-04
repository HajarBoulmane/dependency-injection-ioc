package net.hajar.ioc_projects.ext;

import net.hajar.ioc_projects.dao.IDao;
import org.springframework.stereotype.Component;

@Component("d2")
public class DaoImpl2 implements IDao {
    public double getData(){
        System.out.println("version exstension");
        double temp=25;
        return temp;
    }

}
