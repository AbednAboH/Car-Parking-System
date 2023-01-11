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
    private static final Map<Integer,Class> mappedClasses=Map.ofEntries(Map.entry(1,ParkingLotManager.class),
            Map.entry(2,ParkingLotEmployee.class),
            Map.entry(4,GlobalManager.class),
            Map.entry(3,CustomerServiceEmployee.class),
            Map.entry(6,OneTimeCustomer.class),
            Map.entry(5,RegisteredCustomer.class));
    public static <T> T getAuthenticatedEntity(String email, String password) {
        SessionFactory factory = MySQL.getSessionFactory();
        Session session = factory.openSession();
        T entity;
        for (int i=1;i<7;i++){
            entity=retrieveUser(i,email,password,session);
            if (entity!=null){
                session.close();
                return entity;
            }

        }
        return null;

    }
    public static int checkAuthintecatedEntityType(String email,String password){
        SessionFactory factory = MySQL.getSessionFactory();
        Session session = factory.openSession();
        for (int i=1;i<7;i++){
            if (retrieveUser(i,email,password,session)!=null){
                session.close();
                return i;
            }
        }
        session.close();
        return 0;

    }
    public static boolean checkEmailExistance(String email){
        SessionFactory factory = MySQL.getSessionFactory();
        Session session = factory.openSession();
        for (int i=1;i<7;i++){
            if (emailQuery(i,email,session)!=null){
                session.close();
                return true;
            }
        }
        session.close();
        return false;

    }
    public static <T> T emailQuery(int classNum,String email,Session session){
        Class<T> entitiesClass=mappedClasses.get(classNum);
        String hql = new StringBuilder().append("SELECT c FROM ").append(entitiesClass.getName()).append(" WHERE c.email = :email").toString();
        return session.createQuery(hql, entitiesClass).setParameter("email", email).getSingleResult();

    }

    private static <T> T retrieveUser(int classNum,String email,String password,Session session){
        Class<T> entitiesClass=mappedClasses.get(classNum);
        String hql = new StringBuilder().append("SELECT c FROM ").append(entitiesClass.getName()).append(" WHERE c.email = :email and c.password = :password").toString();
        return session.createQuery(hql, entitiesClass).setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();

    }

}