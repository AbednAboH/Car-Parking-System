package il.cshaifasweng.OCSFMediatorExample.client.KioskOrdersCreator;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class KioskOrdersPage extends SimpleChatClient {
    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        client = SimpleClient.getClient();
        client.openConnection();
        scene = new Scene(loadFXML("KioskSetupPage"), 1080, 720);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
