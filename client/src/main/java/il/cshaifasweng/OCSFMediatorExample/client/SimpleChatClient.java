package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@Getter
@Setter
public class SimpleChatClient extends Application {

    private static Scene scene;
    private static SimpleClient client;
    private static Object user;
    private static Order currentOrder;

    public static Order getCurrentOrder() {
        return currentOrder;
    }

    public static void setCurrentOrder(Order currentOrder) {
        SimpleChatClient.currentOrder = currentOrder;
    }
    public  static Scene getScene(){
        return scene;
    }

    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        scene = new Scene(loadFXML("SubscriptionScreen"), 1080, 720);
        stage.setScene(scene);
        stage.show();
    }


    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader. load();
    }
    
    public static Object getUser(){
        return user;
    }

    public static void setUser(Object user){
        SimpleChatClient.user = user;
    }

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
		super.stop();
	}


    @Subscribe
    public void onMessageEvent(MessageEvent message) {

    }


	public static void main(String[] args) {
        launch();
    }

}