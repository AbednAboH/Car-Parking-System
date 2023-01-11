package il.cshaifasweng.LogInEntities;

import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.OneTimeCustomer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.LogInEntities.Employees.*;
import il.cshaifasweng.MySQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Map;


public class AuthenticationService {
    private static final Map<Integer,Class> mappedClasses=Map.ofEntries(Map.entry(3,ParkingLotManager.class),
            Map.entry(2,ParkingLotEmployee.class),
            Map.entry(4,GlobalManager.class),
            Map.entry(5,CustomerServiceEmployee.class),
            Map.entry(6,OneTimeCustomer.class),
            Map.entry(1,RegisteredCustomer.class));
    public <T> T getAuthenticatedEntity(String email, String password) {
        SessionFactory factory = MySQL.getSessionFactory();
        Session session = factory.openSession();
        T entity;
        for (int i=1;i<7;i++){
            entity=retrieveUser(i,email,password,session);
            if (entity!=null)
                return entity;

        }
        return null;

    }
    public int checkAuthintecatedEntityType(String email,String password){
        SessionFactory factory = MySQL.getSessionFactory();
        Session session = factory.openSession();
        for (int i=1;i<7;i++){
            if (retrieveUser(i,email,password,session)!=null)
                return i;
        }
        return 0;

    }
    private static <T> T retrieveUser(int classNum,String email,String password,Session session){
        Class<T> entitiesClass=mappedClasses.get(classNum);
        String hql = new StringBuilder().append("SELECT c FROM ").append(entitiesClass.getName()).append(" WHERE c.email = :email and c.password = :password").toString();
        return session.createQuery(hql, entitiesClass).setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();

    }

}