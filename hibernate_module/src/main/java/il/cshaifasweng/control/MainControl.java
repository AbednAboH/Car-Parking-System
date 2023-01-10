package il.cshaifasweng.control;
import il.cshaifasweng.LogInEntities.Employees.GlobalManager;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotManager;
import il.cshaifasweng.MoneyRelatedServices.Penalty;
import il.cshaifasweng.MoneyRelatedServices.Refund;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import il.cshaifasweng.customerCatalogEntities.FullSubscription;
import il.cshaifasweng.customerCatalogEntities.RegularSubscription;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class MainControl {
    private static final SessionFactory factory;

    static {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ParkingLot.class);
        configuration.addAnnotatedClass(ParkingSpot.class);
        configuration.addAnnotatedClass(ParkingLotManager.class);
        configuration.addAnnotatedClass(ParkingLotEmployee.class);
        configuration.addAnnotatedClass(GlobalManager.class);
        configuration.addAnnotatedClass(Penalty.class);
        configuration.addAnnotatedClass(Refund.class);
        configuration.addAnnotatedClass(Subscription.class);
        configuration.addAnnotatedClass(RegularSubscription.class);
        configuration.addAnnotatedClass(FullSubscription.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build()
                ;
        factory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        return factory;
    }
}
