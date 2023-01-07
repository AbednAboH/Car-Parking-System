package il.cshaifasweng.control;

import il.cshaifasweng.Customer;
import il.cshaifasweng.ParkingLot;
import il.cshaifasweng.Penalty;
import org.hibernate.CustomEntityDirtinessStrategy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PenaltyControl {
    private static SessionFactory factory = MainControl.getSessionFactory();

    public void createPenalty(Penalty penalty){

        Session session = factory.getCurrentSession();
        session.beginTransaction();

        session.save(penalty);

        session.getTransaction().commit();

    }
    public Penalty getPenalty(int id) {
        // create a session and begin a transaction
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        // retrieve the Penalty from the database
        Penalty Penalty = session.get(Penalty.class, id);

        // commit the transaction
        session.getTransaction().commit();

        return Penalty;
    }

    public void updatePenalty(Penalty Penalty) {
        // create a session and begin a transaction
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        // update the Penalty object in the database
        session.update(Penalty);

        // commit the transaction
        session.getTransaction().commit();
    }

    public boolean deletePenalty(Penalty Penalty) {
        // create a session and begin a transaction
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        // delete the Penalty object from the database
        session.delete(Penalty);

        // commit the transaction
        session.getTransaction().commit();
        return true;
    }

    public boolean penaltyOnArrival(Customer customer, ParkingLot parkingLot){
//        To-Do
        return false;
    }
    public boolean penaltyOnExit(Customer customer, ParkingLot parkingLot){
//        To-DO
        return false;
    }
    public boolean checkIfPenaltyIsDue(Customer customer, ParkingLot parkingLot){
//        To-Do
        return false;
    }



}
