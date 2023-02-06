package il.cshaifasweng.OCSFMediatorExample.server;

import EmailSMPTServices.SendEmail;
import il.cshaifasweng.DataManipulationThroughDB.DAO;
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
    // TODO: 06/02/2023 add session and check

    public handleOrderesAndPenalties(SimpleServerClass server) {
        super(server);
    }
    // code for calculating the minutes to enter
    //given LocalTime timeToEnter
    // return the number of minutes to enter

    final int REMIND=0,
            LATE=1,
        NO_SHOW=2,
        CANCELATION=3;

    private int calculateMinutesToEnter(LocalDateTime timeToEnter){

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
                " AND o.reminderSent != 3 AND o.active = true";
        Map<String,Object> params = new HashMap<>();
//        Session session = getServer().handleDelaysAndPenaltiesSession;
        Session session = DAO.factory.openSession();
        session.beginTransaction();
        DataBaseManipulation<Order> handler=new DataBaseManipulation<>();
        orders = handler.executeListQuery(Order.class,hql,params, session);


        for (Order order:orders) {
            int minutesToEnter=calculateMinutesToEnter(order.getDateOfOrder());
            System.out.println(minutesToEnter);
            if ((minutesToEnter <= 30) && (minutesToEnter >= 0)&&order.getReminderSent()==REMIND){
//                System.out.println(calculateMinutesToEnter(order.getDateOfOrder()));
                SendEmail.sendEmail(order.getEmail(),"Reminder","You have an order in ParkingLot number: "+order.getParkingLotID().getId()+" in "+minutesToEnter+" minutes");
                order.setReminderSent(LATE);
            }
            else if(minutesToEnter<=0&&minutesToEnter>=-5 && order.getReminderSent()==LATE){
                SendEmail.sendEmail(order.getEmail(),"Reminder","You are late on your Appointment." +
                        "\n In ParkingLot number: "+order.getParkingLotID().getId()+"\n Please enter Your Account and Confirm Your arrival."
                        +"\nIf you don't confirm your arrival in 30 Minutes your Order will be canceled."
                        +"\nIf you do confirm your arrival , your account will be charged 20% of the Orders value"+
                        "\n upon confirming your account will be charged "+order.getValue()*1.2+
                        "\nPlease enter your account and confirm your arrival."
                        +"Your order id is: "+order.getId()+"please enter your account details and in the main page press on your order , there you will be prompted to confirm your arrival."
//                        +"Or reply yes to this email to confirm your arrival. and no to cancel your order."
                        +"\n Thank you for using our service.");
                order.setReminderSent(NO_SHOW);

            }
            else if(minutesToEnter<=-30&&order.getReminderSent()==NO_SHOW){
                SendEmail.sendEmail(order.getEmail(),"Canceled order",
                        "You have not Confirmed your arrival."
                         +"\nYour order Id:"+order.getId()+" In ParkingLot number:" +order.getParkingLotID().getId() +"has been canceled.");
                order.setReminderSent(CANCELATION);
                order.setActive(false);
            }
            session.update(order);
            session.flush();
            // TODO: 05/02/2023 maybe send a message to the client to update the orders if the client is connected


        }
        session.getTransaction().commit();
        session.close();
    }

}
