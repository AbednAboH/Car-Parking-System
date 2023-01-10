package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.LogInEntities.Employees.GlobalManager;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotManager;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import org.hibernate.HibernateException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class DataBase {

    static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        final Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ParkingLot.class);
        configuration.addAnnotatedClass(ParkingLotManager.class);
        configuration.addAnnotatedClass(ParkingLotEmployee.class);
        configuration.addAnnotatedClass(GlobalManager.class);
        final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void connectData() {
        try {
            final SessionFactory sessionFactory = getSessionFactory();
            (DataBase.session = sessionFactory.openSession()).beginTransaction();
            initializeData();
        }
        catch (Exception exception) {
            if (DataBase.session != null) {
                DataBase.session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        }
        finally {
            if (DataBase.session != null) {
                DataBase.session.close();
                DataBase.session.getSessionFactory().close();
            }
        }
    }


    private static void initializeData() throws Exception {

//        PricingChart pricingChart = new PricingChart(8.0, 7.0,
//                60, 54, 72);
//        DataBase.session.save(pricingChart);
//        DataBase.session.flush();

//        Employee employee_1 = new Employee("Abed", 24, 24.0);
//        Employee employee_2 = new Employee("Lian", 22, 20.0);
//        Employee employee_3 = new Employee("Shehab", 23, 15.0);

//        DataBase.session.save(employee_2);
//        DataBase.session.save(employee_3);
//        DataBase.session.flush();
//        DataBase.session.save(employee_1);

        ParkingLotManager manager=new ParkingLotManager("idiot","manager",13);
        ParkingLot parkingLot_1 = new ParkingLot(3,3,4,manager);
        ParkingLotManager manager1=new ParkingLotManager("idiot2","manager",13);
        ParkingLot parkingLot_2 = new ParkingLot(3,3,3,manager1);
        ParkingLotManager manager2=new ParkingLotManager("idiot3","manager",13);
        ParkingLot parkingLot_3 = new ParkingLot(3,2,5,manager2);

        DataBase.session.save(manager);
        DataBase.session.save(manager1);
        DataBase.session.save(manager2);
        DataBase.session.save(parkingLot_1);
        DataBase.session.save(parkingLot_2);
        DataBase.session.save(parkingLot_3);
        DataBase.session.flush();

        DataBase.session.getTransaction().commit();
    }


}
