package il.cshaifasweng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
public class MySQL
{
    private static final Class[] classes=new Class[]{ParkingLot.class,ParkingSpot.class,ParkingLotManager.class,ParkingLotEmployee.class,
            GlobalManager.class,PricingChart.class};
    private static final Map<String,Class> mappedClasses=Map.ofEntries(Map.entry("Lot",ParkingLot.class),
            Map.entry("Manager",ParkingLotManager.class),Map.entry("Spot",ParkingSpot.class),
            Map.entry("Employee",ParkingLotEmployee.class),Map.entry("CEO",GlobalManager.class),
            Map.entry("Prices",PricingChart.class));


    private static Session session;
//creates a session factory and adds all "class" type entities to the session
    private static SessionFactory getSessionFactory() throws HibernateException {

        Configuration configuration = new Configuration();
        for (Class cl:classes)
            configuration.addAnnotatedClass(cl);
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
//            deleteEntity(1,20,PricingChart.class);
//            initiatePricingChart();
//            PricingChart pr=new PricingChart("parkViaKiosk",true,0,8);
//            System.out.println(pr.toString());
//            session.save(new PricingChart("parkViaKiosk",true,0,8));
//            initiatePricingChart();
              printAllEntities();

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

    private static <T> List<T> acquireEntitiesFromDB(String entityType) throws Exception{
        List<T> entities=new ArrayList<>();
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            entities=getAllEntities(mappedClasses.get(entityType));
            session.getTransaction().commit(); // Save everything.
            return entities;
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
        return entities;
    }
    private static ParkingLot addParkingLotToDB(int floor,int rowsInEachFloor,int rowCapacity,ParkingLotManager manager) throws Exception {
            session.save(new ParkingLot(floor,rowsInEachFloor, rowCapacity,manager));
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
    public static <T> T retrieveLastAdded(Class<T> EntityClass)throws Exception{
        String hql ="FROM "+EntityClass.getName()+" e ORDER BY e.id DESC";
        TypedQuery<T> query = session.createQuery(hql, EntityClass).setMaxResults(1);
        return query.getSingleResult();
    }

    // TODO: 1/4/2023 check which entities are deleted and if relevant entieties are deleted likewise
    private static <T> void deleteEntity(int fromId,int toId,Class<T> EntityClass)throws Exception{
        for (int id=fromId;id<=toId;id++){
            T entity =  session.get(EntityClass, id);

        if (entity!=null)
            session.delete(entity);
        }
    }
    private static <T> T getEntity(int EntityId,Class<T> EntityClass)throws Exception{
             return session.get(EntityClass,EntityId);
    }

    private static <T> List<T> getAllEntities(Class<T> EntityClass) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(EntityClass);
        query.from(EntityClass);
        return session.createQuery(query).getResultList();
    }
    private static void printAllEntities()throws Exception{
        for (Class cl:classes)
            printEntity(cl);
    }
    private static <T> void printEntity(Class<T> Entity) throws Exception {
        List<T> entity =getAllEntities(Entity) ;
        for (T tinyEntity : entity) {
            System.out.println(tinyEntity.toString());
        }
        System.out.println("\n");
    }
    private static void addParkingLotEmployee(String firstName,String lastName, String title,String email,double salary,int parkingLotId)throws Exception{
        ParkingLot pl=getEntity(parkingLotId,ParkingLot.class);
        ParkingLotEmployee employee=new ParkingLotEmployee(firstName,lastName,title,email,salary,pl);
        session.save(employee);
        pl.addEmployee(employee);
        session.flush();
    }
    private static void addExecutiveManager(String name,String title,double salary)throws Exception{
        GlobalManager globalManager=new GlobalManager(name,title,salary);
        ParkingLot pl=retrieveLastAdded(ParkingLot.class);
    }
    private static void initiatePricingChart()throws Exception{
        session.save(new PricingChart("parkViaKiosk",true,0,8));
        PricingChart oneTimePar=retrieveLastAdded(PricingChart.class);
        oneTimePar.setRateId(oneTimePar.getId());
        session.save(new PricingChart("OneTimeParchaseAhead",true,0,7));
        oneTimePar=retrieveLastAdded(PricingChart.class);
        oneTimePar.setRateId(oneTimePar.getId());
        session.save(new PricingChart("RegularSubscription",false,oneTimePar.getId(),60));
        session.save(new PricingChart("RegularSubscriptionMultipleCars",false,oneTimePar.getId(),54));
        session.save(new PricingChart("FullSubscription",false,oneTimePar.getId(),72));
        initiateParkingLot();
        session.flush();
    }
    private static void initiateParkingLot() throws Exception {
        intiateParkingLotManagers();
        List<ParkingLotManager> managers=getAllEntities(ParkingLotManager.class);
        for(ParkingLotManager manager: managers)
            addParkingLotToDB(3,3,4,manager);

    }
    private static void intiateParkingLotManagers()throws Exception{
        session.save(new ParkingLotManager("manager1","prototype1","manager","manager1Prototype@Cps.co.il",15000));
        session.save(new ParkingLotManager("manager2","prototype2","manager","manager1Prototype@Cps.co.il",12000));
        session.save(new ParkingLotManager("manager3","prototype3","manager","manager1Prototype@Cps.co.il",13000));
        session.flush();
    }
}