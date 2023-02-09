package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.customerCatalogEntities.Order;
import org.hibernate.Session;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class handleOrderesAndPenalties extends  TimeTriggeredThread{
    public handleOrderesAndPenalties(SimpleServerClass server) {
        super(server);
    }

    @Override
    public void run() {
        // code for sending reminders and handling penalties
        List<Order> orders ;
        String hql = " FROM Order o "
                + "WHERE o.dateOfOrder >= CURDATE()" +
                " AND o.active = true AND o.reminderSent = false";
        Map<String,Object> params = new HashMap<>();
        Session session = getServer().handleDelaysAndPenaltiesSession;
        session.beginTransaction();
        DataBaseManipulation<Order> handler=new DataBaseManipulation<>();
        orders = handler.executeQuery(Order.class,hql,params, session);
        session.getTransaction().commit();
        for (Order order:orders) {
            if (Integer.parseInt(order.getEntering()) - LocalTime.now().getHour() ==0)
                if(60- LocalTime.now().getMinute() <= 30)
                    // TODO: 03/02/2023 send reminsders
                System.out.println("order: " + order.getId() + " is close to enter");
            // TODO: 03/02/2023 check each active orders time to enter , if it is close then send reminder else apply penalty if
            //  it is too late ! not that complicated
                // send reminder
        }
        System.out.println("orders size: " + orders.size());
    }

}
