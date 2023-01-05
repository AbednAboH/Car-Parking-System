package il.cshaifasweng;

import java.util.List;
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
//creates a session factory and adds all "class" type entities to the session
    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ParkingLot.class);
        configuration.addAnnotatedClass(ParkingSpot.class);
        configuration.addAnnotatedClass(ParkingLotManager.class);
        configuration.addAnnotatedClass(ParkingLotEmployee.class);
        configuration.addAnnotatedClass(GlobalManager.class);
//        configuration.addAnnotatedClass(PricingChart.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build()
                ;

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void main( String[] args ) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
//            addParkingLotEmployee("msaod","maroom","kiosk Operator","something@CSP.co.il",234,6);
//            Print(ParkingLot.class);
//            Print(ParkingLotManager.class);
//            Print(ParkingLotEmployee.class);
//            deleteEntity(6,6,ParkingLot.class);

            session.getTransaction().commit(); // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            assert session != null;
            session.close();
            session.getSessionFactory().close();
            System.exit(0);

        }
    }

    private static ParkingLot addParkingLotToDB(int floor,int rowsInEachFloor,int rowCapacity,ParkingLotManager manager) throws Exception {
            session.save(manager);
            session.save(new ParkingLot(floor,rowsInEachFloor, rowCapacity,retrieveLastAdded(ParkingLotManager.class)));
            ParkingLot parking = retrieveLastAdded(ParkingLot.class);

            for (ParkingSpot spot:parking.getSpots()){
                spot.setParkingLot(parking);
                session.save(spot);
            }
            session.flush();
            return parking;
    }
//these functions delete ,finds and prints all entities of type X
    // in other words if you want to Print Parking lot , all you would do is insert ParkingLot.Class
    // then the function iterates over all parking lots in the DB and prints/deletes/retrieves them
    public static <T> T retrieveLastAdded(Class<T> EntityClass){
        String hql ="FROM "+EntityClass.getName()+" e ORDER BY e.id DESC";
        TypedQuery<T> query = session.createQuery(hql, EntityClass).setMaxResults(1);
        return query.getSingleResult();
    }

    // TODO: 1/4/2023 check which entities are deleted and if relevant entieties are deleted likewise
    private static <T> void deleteEntity(int fromId,int toId,Class<T> EntityClass){
        for (int id=fromId;id<=toId;id++){
            T entity =  session.get(EntityClass, id);

        if (entity!=null)
            session.delete(entity);
        }
    }
    private static <T> T getEntity(int EntityId,Class<T> EntityClass){
             return session.get(EntityClass,EntityId);
    }

    private static <T> List<T> getAllEntities(Class<T> EntityClass) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(EntityClass);
        query.from(EntityClass);
        return session.createQuery(query).getResultList();
    }

    private static <T> void Print(Class<T> Entity) throws Exception {
        List<T> entity =getAllEntities(Entity) ;
        for (T tinyEntity : entity) {
            System.out.println(tinyEntity.toString());
        }
        System.out.println("\n");
    }
    private static void addParkingLotEmployee(String firstName,String lastName, String title,String email,double salary,int parkingLotId){
        ParkingLot pl=getEntity(parkingLotId,ParkingLot.class);
        ParkingLotEmployee employee=new ParkingLotEmployee(firstName,lastName,title,email,salary,pl);
        session.save(employee);
        pl.addEmployee(employee);
        session.flush();
    }
    private static void addExecutiveManager(String name,String title,double salary){
        GlobalManager globalManager=new GlobalManager(name,title,salary);
        ParkingLot pl=retrieveLastAdded(ParkingLot.class);
    }
}