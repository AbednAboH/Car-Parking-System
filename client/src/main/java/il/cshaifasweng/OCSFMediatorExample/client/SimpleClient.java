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
		}else if(message.getMessage().equals("#getUser")){
			EventBus.getDefault().post(new RegisteredCutomerSubscriber(message));
		}else if(message.getMessage().equals("Error! we got an empty message")) {
			EventBus.getDefault().post(new ErrorEvent(message));
		}else if(message.getMessage().startsWith("#GetAllCompliants")||message.getMessage().startsWith("#CloseComplaint")) {
			EventBus.getDefault().post(new CompliantsSubscriber(message));
		}else if(message.getMessage().startsWith("#verifySubscription")){
			EventBus.getDefault().post(new KioskSubscriber(message));
		}else if(message.getMessage().startsWith("#verifyOrder")){
			EventBus.getDefault().post(new KioskSubscriber(message));
		}else if(message.getMessage().startsWith("#getAllOrders")){
			EventBus.getDefault().post(new OrderHistoryResponse(message));
		}else if (message.getMessage().startsWith("#applyComplaint")){
			EventBus.getDefault().post(new ComplaintSubscriber(message));
		}else if (message.getMessage().startsWith("#GetCustomerCars")){
			EventBus.getDefault().post(new CustomerCarsSubscriber(message));
		}else if(message.getMessage().startsWith("#GetRefundChart")){
			System.out.println("We got a message");
			EventBus.getDefault().post(new RefundChartSubscriber(message));
		}else if(message.getMessage().startsWith("#CancelOrderAndGetRefund")){
			EventBus.getDefault().post(new CancelationRefundSubscriber(message));
			EventBus.getDefault().post(new CustomerRefundsSubscriber(message));
		}else if(message.getMessage().startsWith("#GetParkingSpots")){
			EventBus.getDefault().post(new ParkingSpotsSubscriber(message));
		}else if(message.getMessage().startsWith("#GetActiveOrders")){
			EventBus.getDefault().post(new OrderHistoryResponse(message));
		}else if(message.getMessage().startsWith("#GetAllOrdersForManager")){
			EventBus.getDefault().post(new OrderHistoryResponse(message));
		}else if(message.getMessage().startsWith("#RejectAllPriceRequests")){
			EventBus.getDefault().post(new UpdateMessageEvent(message));
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
//
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
