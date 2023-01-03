package il.cshaifasweng;

import java.util.List;
import java.util.Random;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
public class App
{

    private static Session session;

    private static SessionFactory getSessionFactory(boolean update_create) throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ParkingLot.class);
        configuration.addAnnotatedClass(ParkingSpot.class);
        configuration.addAnnotatedClass(ParkingLotManager.class);
        configuration.addAnnotatedClass(ParkingLotEmployee.class);
        configuration.addAnnotatedClass(GlobalManager.class);
//        if(update_create) configuration.setProperty("hibernate.hbm2ddl.auto","update");
//        else configuration.setProperty("hibernate.hbm2ddl.auto","create");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build()
                ;

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void main( String[] args ) {
        try {
            SessionFactory sessionFactory = getSessionFactory(false);
            session = sessionFactory.openSession();
//            ParkingLot park=new ParkingLot();
//            ParkingSpot spot=new ParkingSpot();
//            session.save(park);
//            session.save(spot);
            session.flush();
            session.beginTransaction();
//            deleteParkingLots(33,42);
//            addParkingLotToDB(3,3,3);
//            printAllParkingLots();

            session.getTransaction().commit(); // Save everything.

        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
            session.getSessionFactory().close();
        }
    }
    private static void generateParkingLots() throws Exception {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            ParkingLot parkingLot = new ParkingLot(random.nextInt(14), 3, 3 + random.nextInt(4));
            session.save(parkingLot);
            session.flush();
        }
    }
    private static void addParkingLotToDB(int floor,int rowsInEachFloor,int rowCapacity) throws Exception {
            session.save(new ParkingLot(floor,rowsInEachFloor, rowCapacity));
            ParkingLot parking = retrievLastAdded(ParkingLot.class);
            parking.initiateParkingSpots();
            for (ParkingSpot spot:parking.getSpots()){
                spot.setParkingLot(parking);
                session.save(spot);
            }
            session.flush();

    }
    public static <T> T retrievLastAdded(Class<T> EntityClass){
        String hql ="FROM "+EntityClass.getName()+" e ORDER BY e.id DESC";
        TypedQuery<T> query = session.createQuery(hql, EntityClass).setMaxResults(1);
        return query.getSingleResult();
    }

    private static void deleteParkingLots(int fromId,int toId){
        for (int id=fromId;id<=toId;id++){
        ParkingLot parkingLot =  session.get(ParkingLot.class, id);
//        ParkingLotEmployee employee=session.get(ParkingLotEmployee.class,);
        if (parkingLot!=null)
            session.delete(parkingLot);

        }

    }

    private static List<ParkingLot> getAllParkingLots() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ParkingLot> query = builder.createQuery(ParkingLot.class);
        query.from(ParkingLot.class);
        List<ParkingLot> data = session.createQuery(query).getResultList();
        return data;
    }

    private static void printAllParkingLots() throws Exception {
        List<ParkingLot> park = getAllParkingLots();
        for (ParkingLot parkingL : park) {

            System.out.print("Id: ");
            System.out.print(parkingL.getId());
            System.out.print(", floor: ");
            System.out.print(parkingL.getFloor());
            System.out.print(", row capacity:");
            System.out.print(parkingL.getRowCapacity());
            System.out.print(", rows in each floor: ");
            System.out.print(parkingL.getRowsInEachFloor());
            System.out.print('\n');
        }
    }
}