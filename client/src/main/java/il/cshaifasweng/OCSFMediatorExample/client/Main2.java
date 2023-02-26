package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class Main2 {
	static class mainPage extends SimpleChatClient{
		@Override
		public void start(Stage stage) throws IOException {
			EventBus.getDefault().register(this);
			client = SimpleClient.getClient();
			client.openConnection();
			scene = new Scene(loadFXML("EntranceAndExitPageSetup"), 1080, 720);
			stage.setScene(scene);
			stage.show();
		}
	}
	public static void main(String[] args) {
		SimpleChatClient.main(args);
	}

}
