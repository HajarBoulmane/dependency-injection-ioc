package metier;

import framework.annotations.Autowired; // Use YOUR framework annotation
import framework.annotations.Component;
import dao.IDao;

@Component
public class MetierImpl implements IMetier {

    @Autowired // This is what the framework looks for to perform injection
    private IDao dao;

    @Override
    public double calcul() {
        return dao.getData() * 2; // Line 25 - where your crash happens
    }
}