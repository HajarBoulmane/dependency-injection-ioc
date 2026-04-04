package net.hajar.ioc_projects.metier;

import net.hajar.ioc_projects.dao.IDao;
import net.hajar.ioc_projects.framework.annotations.Component;
import net.hajar.ioc_projects.framework.annotations.Qualifier;

@Component("metier")
public class MetierImpl implements IMetier {
    private IDao dao;

    // ADD THIS ↓
    public MetierImpl() {}

    // Keep this
    public MetierImpl(@Qualifier("d") IDao dao) {
        this.dao = dao;
    }

    // ADD THIS ↓
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    public double calcul() {
        double t = dao.getData();
        return t * 23;
    }
}