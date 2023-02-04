package il.cshaifasweng.OCSFMediatorExample.server;

import EmailSMPTServices.SendEmail;
import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.customerCatalogEntities.Order;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class handleOrderesAndPenalties extends  TimeTriggeredThread{
    public handleOrderesAndPenalties(SimpleServerClass server) {
        super(server);
    }
    // code for calculating the minutes to enter
    //given LocalTime timeToEnter
    // return the number of minutes to enter
    private int calculateMinutesToEnter(LocalDateTime timeToEnter){
        System.out.println(timeToEnter+" "+LocalDateTime.now());

        int hours=timeToEnter.getHour()-LocalDateTime.now().getHour();
        int minutes=timeToEnter.getMinute()-LocalDateTime.now().getMinute();

        return hours*60+minutes;
    }

    @Override
    public void run() {
        // code for sending reminders and handling penalties
        List<Order> orders ;
        String hql = "FROM Order o "
                + "WHERE o.dateOfOrder >= CURDATE()" +
                " AND o.reminderSent = false";
        Map<String,Object> params = new HashMap<>();
        Session session = getServer().handleDelaysAndPenaltiesSession;
        session.beginTransaction();
        DataBaseManipulation<Order> handler=new DataBaseManipulation<>();
        orders = handler.executeListQuery(Order.class,hql,params, session);
        session.getTransaction().commit();


        for (Order order:orders) {
            int minutesToEnter=calculateMinutesToEnter(order.getDateOfOrder());
            System.out.println(minutesToEnter);
            if ((minutesToEnter <= 30) && (minutesToEnter >= 0)){
//                System.out.println(calculateMinutesToEnter(order.getDateOfOrder()));
                // TODO: 04/02/2023  use it to send reminder
                //  SendEmail.sendEmail(order.getEmail(),"Reminder","You have an order in "+order.getParkingLotID().getId()+" in "+minutesToEnter+" minutes");
            }
            else System.out.println("too late");
            // TODO: 03/02/2023 check each active orders time to enter , if it is close then send reminder else apply penalty if
            //  it is too late ! not that complicated
                // send reminder
        }
        System.out.println("orders size: " + orders.size());
    }

}
