package il.cshaifasweng.control;

import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.MoneyRelatedServices.Penalty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class PenaltyControl {
    private static SessionFactory factory = MainControl.getSessionFactory();

    public void createPenalty(Penalty penalty){

        Session session = factory.getCurrentSession();
        

        session.save(penalty);

       //

    }
    public Penalty getPenalty(int id) {
        // create a session and begin a transaction
        Session session = factory.getCurrentSession();
        

        // retrieve the Penalty from the database
        Penalty Penalty = session.get(Penalty.class, id);

        // commit the transaction
       

        return Penalty;
    }

    public void updatePenalty(Penalty Penalty) {
        // create a session and begin a transaction
        Session session = factory.getCurrentSession();
        

        // update the Penalty object in the database
        session.update(Penalty);

        // commit the transaction
       
    }

    public boolean deletePenalty(Penalty Penalty) {
        // create a session and begin a transaction
        Session session = factory.getCurrentSession();
        

        // delete the Penalty object from the database
        session.delete(Penalty);

        // commit the transaction
       
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
