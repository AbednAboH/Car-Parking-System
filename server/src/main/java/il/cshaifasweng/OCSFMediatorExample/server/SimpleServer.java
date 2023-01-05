//package il.cshaifasweng.OCSFMediatorExample.server;
//
//import il.cshaifasweng.ParkingLotEmployee;
//import il.cshaifasweng.OCSFMediatorExample.entities.PricingChartEnum;
//import il.cshaifasweng.Message;
//import il.cshaifasweng.ParkingLot;
//import il.cshaifasweng.PricingChart;
//import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
//import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.service.ServiceRegistry;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SimpleServer extends AbstractServer {
//
//	private static SessionFactory sessionFactory;
//	private static Session session;
//	private static List<ParkingLot> parkingLotList;
//
////	Constants.
//	public enum Constants
//	{
//		EMPTY(""),
//		BLANK("Error! we got an empty message"),
//		PARKING_LOTS("Get parking lot lists"),
//		PRICES("Get parking prices"),
//		;
//
//
//
//
//		public final String label;
//
//		private Constants(String label) {
//			this.label = label;
//		}
//	}
//	private static ArrayList<ParkingLot> parkingLots = new ArrayList<>();
//	private static PricingChart pricingChart;
//
//	public SimpleServer(int port) {
//		super(port);
//	}
//
//	@Override
//	protected void handleMessageFromClient(Object msg,ConnectionToClient client){
//		Message message = (Message) msg;
//		String request = message.getMessage();
//		try{
//			SimpleServerClass.session = SimpleServerClass.sessionFactory.openSession();
//			if (request.isBlank()){
//				message.setMessage("Error! we got an empty message");
//				client.sendToClient(message);
//				return;
//			}
//			if(request.startsWith("#ParkingLotsRequest")){
//				sendParkingLotList(client);
//				return;
//			}
//			if(request.startsWith("#getPrices")) {
//				sendPrices(client);
//				return;
//			}
//			if(request.startsWith("#changePrices")){
//				updatePricingTable((PricingChart)message.getObject(),client);
//			}
//
//		SimpleServerClass.session.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	public void connectData() {
//		try {
//			final SessionFactory sessionFactory = getSessionFactory();
//			(SimpleServerClass.session = sessionFactory.openSession()).beginTransaction();
//			initializeData();
//		}
//		catch (Exception exception) {
//			if (SimpleServerClass.session != null) {
//				SimpleServerClass.session.getTransaction().rollback();
//			}
//			System.err.println("An error occured, changes have been rolled back.");
//			exception.printStackTrace();
//		}
//		finally {
//			if (SimpleServerClass.session != null) {
//				SimpleServerClass.session.close();
//				SimpleServerClass.session.getSessionFactory().close();
//			}
//		}
//	}
//
//
//	private static void initializeData() throws Exception {
//
//		PricingChart pricingChart = new PricingChart(PricingChartEnum.PARK_VIA_KIOSK.value, PricingChartEnum.ONE_TIME_PURCHASED_AHEAD.value,
//				PricingChartEnum.REGULAR_SUBSCRIPTION.value, PricingChartEnum.REGULAR_SUBSCRIPTION_MULIPLE_CARS.value, PricingChartEnum.FULL_SUBSCRIPTION.value);
//		SimpleServerClass.session.save(pricingChart);
//		SimpleServerClass.session.flush();
//
//		ParkingLotEmployee employee_1 = new ParkingLotEmployee("Abed", 24, 24.0);
//		ParkingLotEmployee employee_2 = new ParkingLotEmployee("Lian", 22, 20.0);
//		ParkingLotEmployee employee_3 = new ParkingLotEmployee("Shehab", 23, 15.0);
//
//		SimpleServerClass.session.save(employee_1);
//		SimpleServerClass.session.save(employee_2);
//		SimpleServerClass.session.save(employee_3);
//		SimpleServerClass.session.flush();
//
//
//		ParkingLot parkingLot_1 = new ParkingLot(3,3,4);
//		parkingLot_1.addNewEmployee(employee_1);
//		ParkingLot parkingLot_2 = new ParkingLot(3,3,3);
//		parkingLot_2.addNewEmployee(employee_2);
//		ParkingLot parkingLot_3 = new ParkingLot(3,2,5);
//		parkingLot_3.addNewEmployee(employee_3);
//
//		SimpleServerClass.session.save(parkingLot_1);
//		SimpleServerClass.session.save(parkingLot_2);
//		SimpleServerClass.session.save(parkingLot_3);
//		SimpleServerClass.session.flush();
//
//		SimpleServerClass.session.getTransaction().commit();
//	}
//
//	private static SessionFactory getSessionFactory() throws HibernateException {
//		final Configuration configuration = new Configuration();
//        configuration.addAnnotatedClass(ParkingLot.class);
//        configuration.addAnnotatedClass(PricingChartEnum.class);
//		final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
//				configuration.getProperties()).build();
//		return configuration.buildSessionFactory(serviceRegistry);
//	}
//
//
//
//	public void sendParkingLotList(ConnectionToClient client) throws IOException {
//		final CriteriaBuilder builder = SimpleServerClass.session.getCriteriaBuilder();
//		final CriteriaQuery<ParkingLot> query = builder.createQuery(ParkingLot.class);
//		query.from(ParkingLot.class);
//		final ArrayList<ParkingLot> data = (ArrayList<ParkingLot>)(ArrayList) SimpleServerClass.session.createQuery(query).getResultList();
//		client.sendToClient(parkingLots);
//	}
//	public void sendPrices(ConnectionToClient client) throws IOException{
//		final CriteriaBuilder builder = SimpleServerClass.session.getCriteriaBuilder();
//		final CriteriaQuery<PricingChart> query = builder.createQuery(PricingChart.class);
//		query.from(PricingChart.class);
//		final ArrayList<PricingChart> data = (ArrayList<PricingChart>)(ArrayList) SimpleServerClass.session.createQuery(query).getResultList();
//		client.sendToClient(data.get(0));
//	}
//	private void updatePricingTable(PricingChart newChart,ConnectionToClient client) throws IOException {
//		final CriteriaBuilder builder = SimpleServerClass.session.getCriteriaBuilder();
//		final CriteriaQuery<PricingChart> query = builder.createQuery(PricingChart.class);
//		query.from(PricingChart.class);
//		final ArrayList<PricingChart> data = (ArrayList<PricingChart>)(ArrayList) SimpleServerClass.session.createQuery(query).getResultList();
//		for(PricingChart oldChart : data){
//			if(oldChart.getId() == newChart.getId()){
//				SimpleServerClass.session.beginTransaction();
//				oldChart.setFullSubscription(newChart.getFullSubscription());
//				oldChart.setOneTimePurchasedAhead(newChart.getOneTimePurchasedAhead());
//				oldChart.setParkViaKiosk(newChart.getParkViaKiosk());
//				oldChart.setRegularSubscription(newChart.getRegularSubscription());
//				oldChart.setRegularMultipleCars(newChart.getRegularMultipleCars());
//				SimpleServerClass.session.save(oldChart);
//				SimpleServerClass.session.flush();
//				SimpleServerClass.session.getTransaction().commit();
//				client.sendToClient(new Message("#RefreshAdd","refresh"));
//			}
//		}
//	}
//
//
//}
