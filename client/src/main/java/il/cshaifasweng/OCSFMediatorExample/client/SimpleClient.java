package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.CompliantsSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.KioskSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.LogInSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.RegisteredCutomerSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		Message message = (Message) msg;
		if(message.getMessage().equals("#getPricingChart")){
			EventBus.getDefault().post(new SubscriptionsChartResults(message));
		}else if(message.getMessage().equals("#getAllParkingLots")){
			EventBus.getDefault().post(new ParkingLotResults(message));
		} else if (message.getMessage().equals("#showOrders")) {
			EventBus.getDefault().post(new OrderHistoryResponse(message));
		}else if (message.getMessage().equals("#showSubscription")) {
			EventBus.getDefault().post(new SubscriptionResponse(message));
		}else if(message.getMessage().startsWith("#authintication")){
			EventBus.getDefault().post(new LogInSubscriber(message));
		}else if(message.getMessage().equals("#placeOrder")){
			EventBus.getDefault().post(new UpdateMessageEvent(message));
		}else if(message.getMessage().equals("addSubscription")){
			EventBus.getDefault().post(new UpdateMessageEvent(message));
		}else if(message.getMessage().equals("cancelSubscription")){
			EventBus.getDefault().post(new UpdateMessageEvent(message));
		}else if(message.getMessage().equals("cancelOrder")){
			EventBus.getDefault().post(new UpdateMessageEvent(message));
		}
		else if(message.getMessage().equals("#getUser")){
			EventBus.getDefault().post(new RegisteredCutomerSubscriber(message));
		}else if(message.getMessage().equals("Error! we got an empty message")) {
			EventBus.getDefault().post(new ErrorEvent(message));
		}else if(message.getMessage().startsWith("#GetAllCompliants")||message.getMessage().startsWith("#CloseComplaint")) {
			EventBus.getDefault().post(new CompliantsSubscriber(message));
		}else if(message.getMessage().startsWith("#verifySubscription")){
			EventBus.getDefault().post(new KioskSubscriber(message));
		}else if(message.getMessage().startsWith("#verifyOrder")){
			System.out.println("We got a message");
			EventBus.getDefault().post(new KioskSubscriber(message));
		}else if(message.getMessage().startsWith("#getAllOrders")){
			EventBus.getDefault().post(new OrderHistoryResponse(message));
		}else if (message.getMessage().startsWith("#applyComplaint")){
			EventBus.getDefault().post(new ComplaintSubscriber(message));
		}else if (message.getMessage().startsWith("#GetCustomerCars")){
			EventBus.getDefault().post(new CustomerCarsSubscriber(message));
		}else if(message.getMessage().startsWith("#GetRefundChart")){
			EventBus.getDefault().post(new RefundChartSubscriber(message));
		}else if(message.getMessage().startsWith("#CancelOrderAndGetRefund")){
			EventBus.getDefault().post(new CancelationRefundSubscriber(message));
			EventBus.getDefault().post(new CustomerRefundsSubscriber(message));
		}else if(message.getMessage().startsWith("#GetParkingSpots")){
			EventBus.getDefault().post(new ParkingSpotsSubscriber(message));
		}
		else if (message.getMessage().startsWith("#getRefunds")){
			EventBus.getDefault().post(new CustomerRefundsSubscriber(message));
		} else if (message.getMessage().startsWith("#getTransactions")) {
			EventBus.getDefault().post(new TransactionsSubscirber(message));
		}else if (message.getMessage().startsWith("#getEntryAndExitLogs")) {
			EventBus.getDefault().post(new LogsSubscriber(message));
		}else if (message.getMessage().startsWith("#getOfflineOrders")) {
			EventBus.getDefault().post(new offlineOrdersSubscriber(message));
		}else if (message.getMessage().startsWith("#getToBeConfirmed")) {
			EventBus.getDefault().post(new UnconfirmedArrivalSubscriber(message));
		} else {
			System.out.println(message.getMessage());
			EventBus.getDefault().post(new MessageEvent(message));
		}
//		String messageString = message.getMessage();
//		switch (messageString) {
//			case "#authintication" -> EventBus.getDefault().post(new LogInSubscriber(message));
//			case "#getUser" -> EventBus.getDefault().post(new RegisteredCutomerSubscriber(message));
//			case "#getAllParkingLots" -> EventBus.getDefault().post(new ParkingLotResults(message));
//			case "#getPricingChart" -> EventBus.getDefault().post(new SubscriptionsChartResults(message));
//			case "#showOrders", "#getAllOrders" -> EventBus.getDefault().post(new OrderHistoryResponse(message));
//			case "#showSubscription" -> EventBus.getDefault().post(new SubscriptionResponse(message));
//			case "#placeOrder", "addSubscription", "cancelSubscription", "cancelOrder" -> EventBus.getDefault().post(new UpdateMessageEvent(message));
//			case "Error! we got an empty message" -> EventBus.getDefault().post(new ErrorEvent(message));
//			case "#GetAllCompliants", "#CloseComplaint" -> EventBus.getDefault().post(new CompliantsSubscriber(message));
//			case "#verifySubscription", "#verifyOrder" -> EventBus.getDefault().post(new KioskSubscriber(message));
//			case "#applyComplaint" -> EventBus.getDefault().post(new ComplaintSubscriber(message));
//			case "#GetCustomerCars" -> EventBus.getDefault().post(new CustomerCarsSubscriber(message));
//			case "#GetRefundChart" -> EventBus.getDefault().post(new RefundChartSubscriber(message));
//			case "#CancelOrderAndGetRefund" -> EventBus.getDefault().post(new CancelationRefundSubscriber(message));
//			default -> EventBus.getDefault().post(new MessageEvent(message));
//		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
