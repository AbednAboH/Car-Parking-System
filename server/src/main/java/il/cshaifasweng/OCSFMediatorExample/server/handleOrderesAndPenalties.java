package il.cshaifasweng.OCSFMediatorExample.server;

import EmailSMPTServices.SendEmail;
import il.cshaifasweng.DataManipulationThroughDB.DAO;
import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class handleOrderesAndPenalties extends  TimeTriggeredThread{
    // TODO: 06/02/2023 add session and check the option to add another field in all transaction entities, or paid entities , for entrance and exit form the parking lot ,
    // TODO: 06/02/2023 meaning actuall arival time , and exit time ,this might be troublesome for subscriptions , as they enter multiple times
    // // TODO: 06/02/2023 maybe change the name to currently using for subscriptions , and is using for orders !! this way entrance and exit can be attended
    // TODO: 25/02/2023 check if the order's owner has a subscription , if he does then don't send him a reminder , and don't charge him a penalty !
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
        List<OnlineOrder> OnlineOrders;
        String hql = "FROM OnlineOrder o "
                + "WHERE o.dateOfOrder >= CURDATE()" +
                " AND o.reminderSent != 3 AND o.active = true";
        Map<String,Object> params = new HashMap<>();
//        Session session = getServer().handleDelaysAndPenaltiesSession;
        Session session = DAO.factory.openSession();
        session.beginTransaction();
        DataBaseManipulation<OnlineOrder> handler=new DataBaseManipulation<>();
        OnlineOrders = handler.executeListQuery(OnlineOrder.class,hql,params, session);

        for (OnlineOrder onlineOrder : OnlineOrders) {
            int minutesToEnter=calculateMinutesToEnter(onlineOrder.getDateOfOrder());
            if ((minutesToEnter <= 30) && (minutesToEnter >= 0)&& onlineOrder.getReminderSent()==REMIND){
                SendEmail.sendEmail(onlineOrder.getEmail(),"Reminder","You have an order in ParkingLot number: "+ onlineOrder.getParkingLotID().getId()+" in "+minutesToEnter+" minutes");
                onlineOrder.setReminderSent(LATE);
            }
            else if(minutesToEnter<=0&&minutesToEnter>=-5 && onlineOrder.getReminderSent()==LATE){
                if (!onlineOrder.getRegisteredCustomer().isCustomerByDefinition())
                    SendEmail.sendEmail(onlineOrder.getEmail(),"Reminder","You are late on your Appointment." +
                        "\n In ParkingLot number: "+ onlineOrder.getParkingLotID().getId()+"\n Please enter Your Account and Confirm Your arrival."
                        +"\nIf you don't confirm your arrival in 30 Minutes your Order will be canceled."
                        +"\nIf you do confirm your arrival , your account will be charged 20% of the Orders value"+
                        "\n upon confirming your account will be charged "+ onlineOrder.getValue()*1.2+
                        "\nPlease enter your account and confirm your arrival."
                        +"Your order id is: "+ onlineOrder.getId()+"please enter your account details and in the main page press on unconfirmed arrivals , there you will be prompted to confirm your arrival."
                        +"\n Thank you for using our service.");
                else SendEmail.sendEmail(onlineOrder.getEmail(),"Reminder","You are late on your Appointment." +
                        "\n In ParkingLot number: "+ onlineOrder.getParkingLotID().getId()+"\n Please enter Your Account and Confirm Your arrival."
                        +"\nIf you don't confirm your arrival in 30 Minutes your Order will be canceled."
                        +"\nPlease enter your account and confirm your arrival."
                        +"Your order id is: "+ onlineOrder.getId()+"please enter your account details and in the main page press on your Unconfirmed arrival, there you will be prompted to confirm your arrival."
                        +"\n Thank you for using our service.");
                onlineOrder.setReminderSent(NO_SHOW);

            }
            else if(minutesToEnter<=-30&& onlineOrder.getReminderSent()==NO_SHOW){
                SendEmail.sendEmail(onlineOrder.getEmail(),"Canceled order",
                        "You have not Confirmed your arrival."
                         +"\nYour order Id:"+ onlineOrder.getId()+" In ParkingLot number:" + onlineOrder.getParkingLotID().getId() +"has been canceled.");
                onlineOrder.setReminderSent(CANCELATION);
                onlineOrder.setActive(false);
            }
            session.update(onlineOrder);
            session.flush();
            // TODO: 05/02/2023 maybe send a message to the client to update the orders if the client is connected


        }
        session.getTransaction().commit();
        session.close();
    }

}
