package il.cshaifasweng.OCSFMediatorExample.server;

import EmailSMPTServices.SendEmail;
import il.cshaifasweng.DataManipulationThroughDB.DAO;
import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.customerCatalogEntities.Order;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HandleSubscriptionReminders extends  TimeTriggeredThread{
    public HandleSubscriptionReminders(SimpleServerClass server) {
        super(server);
    }


    @Override
    public void run() {
        // code for sending reminders and handling penalties
        List<Subscription> subscriptions ;
        String subscriptionsQueiry = "FROM Subscription s "
                + "WHERE S.expirationDate = DATE_SUB(CURDATE(), INTERVAL 7 DAY)" +
                "AND o.isActive = true";

        Map<String,Object> params = new HashMap<>();

        Session session = DAO.factory.openSession();
        session.beginTransaction();
        DataBaseManipulation<Subscription> handler=new DataBaseManipulation<>();
        subscriptions = handler.executeListQuery(Subscription.class,subscriptionsQueiry,params, session);

        for (Subscription subscription:subscriptions) {
            SendEmail.sendEmail(subscription.getRegisteredCustomer().getEmail(),
                    "Reminder",
                    "Your subscription is about to expire in 7 days"
            +"\nYour Subscription ID is: "+subscription.getId()+
                    "\nYour Subscription Type is: "+subscription.getClass().getSimpleName()+
                    "\nYour Subscription Expiration Date is: "+subscription.getExpirationDate());
            session.flush();
            // TODO: 05/02/2023 maybe send a notification to the customer as well

        }
        session.getTransaction().commit();
        session.close();
    }

}
