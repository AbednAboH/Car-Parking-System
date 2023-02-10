package il.cshaifasweng.OCSFMediatorExample.server;

import EmailSMPTServices.SendEmail;
import il.cshaifasweng.DataManipulationThroughDB.DAO;
import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.customerCatalogEntities.Order;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


public class HandleSubscriptionReminders extends  TimeTriggeredThread{
    public HandleSubscriptionReminders(SimpleServerClass server) {
        super(server);
        System.out.println("subscription reminders thread started");
    }
    private static final LocalTime subsReminderTime = LocalTime.of(8, 0, 0);
    public static long getDelay() {
        // TODO: 06/02/2023 maybe add it to system management gui in the future
        LocalTime now = LocalTime.now();
        now=now.minusHours(subsReminderTime.getHour());
        now=now.minusMinutes(subsReminderTime.getMinute());
        now=now.minusSeconds(subsReminderTime.getSecond());
        return now.getHour()*60*60+now.getMinute()*60+now.getSecond();

    }

    @Override
    public void run() {
        Session session = DAO.factory.openSession();
        session.beginTransaction();
        CriteriaBuilder builder= session.getCriteriaBuilder();
        CriteriaQuery<Subscription> criteria = builder.createQuery(Subscription.class);
        Root<Subscription> root = criteria.from(Subscription.class);

        criteria.select(root);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date dateOnly = calendar.getTime();
        dateOnly.setTime(dateOnly.getTime() + 7 * 24 * 60 * 60 * 1000);

        criteria.where(builder.equal(builder.function("date", Date.class, root.get("expirationDate")), dateOnly),
                builder.equal(root.get("isActive"), true));

        List<Subscription> subscriptions = session.createQuery(criteria).getResultList();

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
        System.out.println("subscriptions reminders ended");

    }

}
