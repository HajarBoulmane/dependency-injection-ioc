package dao;
import framework.annotations.Component; // Import YOUR custom annotation

@Component
public class DaoImpl implements IDao {

    @Override

    public double getData() {
        return 10; // valeur exemple
    }
}
