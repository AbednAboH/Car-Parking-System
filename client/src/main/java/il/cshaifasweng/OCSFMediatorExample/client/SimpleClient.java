package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
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
		else if(message.getMessage().equals("Error! we got an empty message")){
		}else if(message.getMessage().equals("#getUser")){
			EventBus.getDefault().post(new RegisteredCutomerSubscriber(message));
		}else if(message.getMessage().equals("Error! we got an empty message")) {
			EventBus.getDefault().post(new ErrorEvent(message));
		}else if(message.getMessage().equals("#GetAllCompliants")||message.getMessage().startsWith("#CloseComplaint")) {
			EventBus.getDefault().post(new CompliantsSubscriber(message));
		}else if(message.getMessage().startsWith("#getAllOrders")){
			EventBus.getDefault().post(new OrderHistoryResponse(message));
		}else if (message.getMessage().startsWith("#applyComplaint")){
			EventBus.getDefault().post(new ComplaintSubscriber(message));
		}else {
			System.out.println(message.getMessage());
			EventBus.getDefault().post(new MessageEvent(message));
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
