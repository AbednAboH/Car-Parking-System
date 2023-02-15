package il.cshaifasweng;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.OneTimeCustomer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.LogInEntities.Employees.CustomerServiceEmployee;
import il.cshaifasweng.LogInEntities.Employees.GlobalManager;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.LogInEntities.Employees.ParkingLotManager;
import il.cshaifasweng.MoneyRelatedServices.*;
import il.cshaifasweng.ParkingLotEntities.*;
import il.cshaifasweng.converters.ParkingLotScheduelerInterceptor;
import il.cshaifasweng.customerCatalogEntities.*;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class MySQL
{
    public static final Class[] classes=new Class[]{ParkingLot.class, ParkingSpot.class, ParkingLotManager.class, ParkingLotEmployee.class,
            GlobalManager.class,PricingChart.class, CustomerServiceEmployee.class, FullSubscription.class, RegularSubscription.class, Subscription.class, Car.class, Complaint.class
            , OneTimeCustomer.class, RegisteredCustomer.class, Penalty.class, Refund.class, Reports.class, Order.class, Customer.class, RefundChart.class, EntryAndExitLog.class,ParkingLotScheduler.class};
    private static final Map<String,Class> mappedClasses=Map.ofEntries(Map.entry("Lot",ParkingLot.class),
            Map.entry("Manager",ParkingLotManager.class),Map.entry("Spot",ParkingSpot.class),
            Map.entry("Employee",ParkingLotEmployee.class),Map.entry("CEO",GlobalManager.class),
            Map.entry("Prices",PricingChart.class),Map.entry("Service",CustomerServiceEmployee.class)
            ,Map.entry("FullSub",FullSubscription.class),Map.entry("RegularSub",RegularSubscription.class)
            ,Map.entry("Sub",Subscription.class),Map.entry("Car",Car.class)
            ,Map.entry("Complaint",Complaint.class),Map.entry("OneTime",OneTimeCustomer.class),Map.entry("Registered",RegisteredCustomer.class)
            ,Map.entry("Penalty",Penalty.class),Map.entry("Refund",Refund.class)
            ,Map.entry("Reports",Reports.class),Map.entry("MoneyRelatedServices",Customer.class)
            ,Map.entry("Orders", Order.class),Map.entry("RefundChart",Refund.class),Map.entry("Scheduler",ParkingLotScheduler.class),Map.entry("Vehicle", EntryAndExitLog.class));

    private static Session session;
//creates a session factory and adds all "class" type entities to the session
    public static SessionFactory getSessionFactory() throws HibernateException {

        Configuration configuration = new Configuration();
        configuration.configure(MySQL.class.getClassLoader().getResource("hibernate.cfg.xml"));
        for (Class cl:classes)
            configuration.addAnnotatedClass(cl);


        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void main( String[] args ) {
        try {

            connectToDB();
//            initiateParkingLot();
//            EnterExiteParkingLotPrefixedValuesForTesting(true);
            EnterExiteParkingLotPrefixedValuesForTesting(false);
//            ParkingLot plot=getEntity(1,ParkingLot.class);
//            plot.getSpots().get(0).setOccupied(true);
//            session.update(plot.getSpots().get(0));
            session.getTransaction().commit();
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

    private static void EnterExiteParkingLotPrefixedValuesForTesting(boolean enterExit) throws Exception {
        List<RegularSubscription> rSubs=getAllEntities(RegularSubscription.class);
        List<FullSubscription> fSubs=getAllEntities(FullSubscription.class);
        List<Order> orders=getAllEntities(Order.class);
        ParkingLot plot=getEntity(1,ParkingLot.class);
        for(int i=0;i<30;i+=3){
            EntryAndExitLog enteredExitedPlot = enterExit? plot.entryToPLot(rSubs.get(i),rSubs.get(i).getCarsList().get(0).getCarNum()):plot.exitParkingLot(rSubs.get(i),rSubs.get(i).getCarsList().get(0).getCarNum());
            if(enteredExitedPlot!=null){
                System.out.println("Vehicle "+rSubs.get(i).getLatestLog().getActiveCar()+" entered the parking lot");
                if (!enterExit)
                    session.update(enteredExitedPlot);

            }
            else{
                System.out.println(enterExit?"ParkingLot is full":"ParkingLot is empty");
                break;
            }

        }

        for(int i=0;i<30;i+=2){
            EntryAndExitLog enteredExitedPlot = enterExit? plot.entryToPLot(orders.get(i),orders.get(i).getCar().getCarNum()):plot.exitParkingLot(orders.get(i),orders.get(i).getCar().getCarNum());
            if(enteredExitedPlot!=null){

                System.out.println("Vehicle "+orders.get(i).getEntryAndExitLog().getActiveCar()+" entered the parking lot");
                if (!enterExit)
                    session.update(enteredExitedPlot);

            }
            else{
                System.out.println(enterExit?"ParkingLot is full":"ParkingLot is empty");

                break;

            }
        }
        for(int i=0;i<30;i+=2){
            EntryAndExitLog enteredExitedPlot = enterExit? plot.entryToPLot(fSubs.get(i),fSubs.get(i).getCarsList().get(0).getCarNum()):plot.exitParkingLot(orders.get(i),orders.get(i).getCar().getCarNum());
            if(enteredExitedPlot!=null){
                System.out.println("Vehicle "+fSubs.get(i).getLatestLog().getActiveCar()+" entered the parking lot");
                if (!enterExit)
                    session.update(enteredExitedPlot);
            }
            else{
                System.out.println(enterExit?"ParkingLot is full":"ParkingLot is empty");

                break;
            }
        }
        session.update(plot);
//        session.update(plot.getSpots());

    }

    public static void connectToDB()throws Exception{
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();

    }
    public static Session getSession() throws Exception{
        return session;
    }

    public static void handleException(Exception e){
        if (session != null) {
            session.getTransaction().rollback();
        }
        System.err.println("An error occured, changes have been rolled back.");
    }
    public static void finalizeConnection(){
        assert session != null;
        session.close();
        session.getSessionFactory().close();
        System.exit(0);
    }
    public static <T> List<T> acquireEntitiesFromDB(String entityType) throws Exception{
        List<T> entities=new ArrayList<>();
        entities=getAllEntities(mappedClasses.get(entityType));
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
//one entity manipulations
    public static <T> T retrieveLastAdded(Class<T> EntityClass)throws Exception{
        String hql ="FROM "+EntityClass.getName()+" e ORDER BY e.id DESC";
        TypedQuery<T> query = session.createQuery(hql, EntityClass).setMaxResults(1);
        return query.getSingleResult();
    }
    public static <T> void update(T entity){
        session.update(entity);
    }
    private static <T> void deleteEntity(int fromId,int toId,Class<T> EntityClass)throws Exception{
        for (int id=fromId;id<=toId;id++){
            T entity =  session.get(EntityClass, id);

        if (entity!=null)
            session.delete(entity);
        }
    }
    private static <T> void update(Class<T> EntityClass)throws Exception{
        session.update(EntityClass);
    }

    private static <T> List<T> getAllEntities(Class<T> EntityClass) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(EntityClass);
        query.from(EntityClass);
        return session.createQuery(query).getResultList();
    }

    public static void printAllEntities()throws Exception{
        for (Class cl:classes)
            printEntity(cl);
    }
    public static <T> void printEntity(Class<T> Entity) throws Exception {
        List<T> entity =getAllEntities(Entity) ;
        for (T tinyEntity : entity) {
            System.out.println(tinyEntity.toString());
        }
        System.out.println("\n");
    }

    public static <T> T getEntity(int EntityId,Class<T> EntityClass)throws Exception{
        return session.get(EntityClass,EntityId);
    }
    public static <T> T getEntityByName(int EntityId,String entityName)throws Exception{

        return (T) getEntity(EntityId,mappedClasses.get(entityName));
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
        PricingChart pC=new PricingChart();
        session.save(pC);
//        initiateParkingLot();
        session.flush();
    }
    public static void initiateParkingLot() throws Exception {
        initiatePricingChart();
        intiateParkingLotManagers();
        List<ParkingLotManager> managers=getAllEntities(ParkingLotManager.class);
        for(ParkingLotManager manager: managers)
            addParkingLotToDB(3,3,4,manager);
        initiateRefundChart();
        initiateCustomers();
        initiateEmployees();
        session.flush();

    }
    private static void intiateParkingLotManagers()throws Exception{
        session.save(new ParkingLotManager("manager1","prototype1","manager","manager1Prototype@Cps.co.il",15000));
        session.save(new ParkingLotManager("manager2","prototype2","manager","manager1Prototype@Cps.co.il",12000));
        session.save(new ParkingLotManager("manager3","prototype3","manager","manager1Prototype@Cps.co.il",13000));
        session.flush();
    }
    private static void initiateRefundChart()throws Exception{
        session.save(new RefundChart(0,1,0.1));
        session.save(new RefundChart(1,3,0.5));
        session.save(new RefundChart(3,-1,0.9));
        session.flush();
    }
    private static void initiateCustomers()throws Exception {
        for (int i=0;i<10;i++){
            session.save(new RegisteredCustomer(i,"customer"+i+"@email.com",   "user"+i,"userFamily"+i,"123456789"));
        }
        initiateOrderList();
        initiateSubscriptions();

    }
    private static void initiateOrderList()throws Exception{
        for (RegisteredCustomer customer : getAllEntities(RegisteredCustomer.class)){
            ParkingLot parkingLot=retrieveLastAdded(ParkingLot.class);
            for (int i=0;i<10;i++){
                Random random = new Random();
                int randomNum = random.nextInt(900000000) + 100000000;
                Order order=new Order(customer,parkingLot, LocalDate.now(),
                        LocalTime.now().plusMinutes(i*2 ), Integer.toString(LocalDateTime.now().getHour()+1),Integer.toString(randomNum), customer.getEmail());
                order.setTransaction_method("cash");
                order.setTransactionStatus(true);
                order.setValue(100);
                session.save(order);

            }
            session.update(customer);

        }
        session.flush();
    }
    private static void initiateEmployees()throws Exception{
        for (ParkingLot parkingLot : getAllEntities(ParkingLot.class)){
            for (int i=0;i<10;i++) {
                ParkingLotEmployee employee = new ParkingLotEmployee("employee" + i, "Family" + i, "general employee", "employee" + i + "@email.com", 10000, parkingLot);
                session.save(employee);
            }
        }
        CustomerServiceEmployee customerServiceEmployee=new CustomerServiceEmployee("ServiceName","ServiceFamily","customerServiceEmployee","CustomeService@email.com",10000,getAllEntities(ParkingLot.class),getAllEntities(Complaint.class));
        session.save(customerServiceEmployee);
        session.flush();
    }

    private static void initiateSubscriptions() throws Exception{
        for (RegisteredCustomer customer : getAllEntities(RegisteredCustomer.class)){
            ParkingLot parkingLot=retrieveLastAdded(ParkingLot.class);
            for (int i=0;i<10;i++){
                Random random = new Random();
                int randomNum = random.nextInt(900000000) + 100000000;
                List<String> rand=new ArrayList<String>();
                rand.add(Integer.toString(randomNum));
                RegularSubscription subscription=new RegularSubscription(customer,60, LocalDate.now(), LocalDate.now().plusMonths(1), parkingLot, LocalTime.MIDNIGHT,rand );
                subscription.setTransaction_method("cash");
                subscription.setTransactionStatus(true);
                subscription.setValue(100);
                subscription.setDate(LocalDate.now());

                session.save(subscription);
            }
            session.update(customer);
             for (int i=0;i<10;i++){
                Random random = new Random();
                int randomNum = random.nextInt(900000000) + 100000000;
                List<String> rand=new ArrayList<String>();
                rand.add(Integer.toString(randomNum));
                FullSubscription subscription=new FullSubscription(customer,60, LocalDate.now(), LocalDate.now().plusMonths(1),rand ,14);
                 subscription.setTransaction_method("cash");
                 subscription.setTransactionStatus(true);
                 subscription.setValue(100);
                 subscription.setDate(LocalDate.now());
                session.save(subscription);

            }
            session.update(customer);

        }
        session.flush();
    }
}