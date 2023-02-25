package il.cshaifasweng.OCSFMediatorExample.server;

import EmailSMPTServices.SendEmail;
import il.cshaifasweng.DataManipulationThroughDB.DAO;
import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.customerCatalogEntities.OfflineOrder;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HandleOfflineOrdersTimeLimit extends  TimeTriggeredThread{
    public HandleOfflineOrdersTimeLimit(SimpleServerClass server) {
        super(server);
    }

    private int calculateMinutesToEnter(LocalDateTime timeToEnter){
        int hours=timeToEnter.getHour()-LocalDateTime.now().getHour();
        int minutes=timeToEnter.getMinute()-LocalDateTime.now().getMinute();
        return hours*60+minutes;
    }

    @Override
    public void run() {
        // code for sending reminders and handling penalties
        List<OfflineOrder> offlineOrder;
        String hql = "FROM OfflineOrder o "
                + "WHERE o.entryTimeLimit >= CURDATE()" +
                "AND o.active = true";
        Map<String,Object> params = new HashMap<>();
//        Session session = getServer().handleDelaysAndPenaltiesSession;
        Session session = DAO.factory.openSession();
        session.beginTransaction();
        DataBaseManipulation<OnlineOrder> handler=new DataBaseManipulation<>();
        offlineOrder = handler.executeListQuery(OfflineOrder.class,hql,params, session);

        for (OfflineOrder offlineOrder1 : offlineOrder) {
            int minutesToEnter=calculateMinutesToEnter(offlineOrder1.getEntryTimeLimit());
            if (minutesToEnter <= 0){
                SendEmail.sendEmail(offlineOrder1.getEmail(),"Cancelation","You'r order in ParkingLot number: "+ offlineOrder1.getParkingLotID().getId()+" has been Canceled due to late usage of the Kiosk order.");
                offlineOrder1.setActive(false);
                session.update(offlineOrder1);
                session.flush();
            }

        }
        session.getTransaction().commit();
        session.close();
    }

}
