package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.LogInSubscriber;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class LogInController{

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    // Import the application's controls
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private Label invalidSignupCredentials;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private TextField loginPasswordPasswordField;
    @FXML
    private TextField signUpUsernameTextField;
    @FXML
    private TextField signUpEmailTextField;
    @FXML
    private TextField signUpPasswordPasswordField;
    @FXML
    private TextField signUpRepeatPasswordPasswordField;
    @FXML
    private DatePicker signUpDateDatePicker;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;
    RadioButton selected;
    ToggleGroup toggleGroup = new ToggleGroup();
    private Object user;
    public final int SECOND = 6000;
    private CountDownLatch latch;
    public static AtomicInteger authintication=new AtomicInteger(0);
    @FXML
    void initialize(){
        EventBus.getDefault().register(LogInController.this);

        try {
            // Check if the connection with the server is alive.
            Message message = new Message("#ConnectionAlive");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            showErrorMessage(Constants.INTERNAL_ERROR);
            e.printStackTrace();
        }
    }

    // Creation of methods which are activated on events in the forms
    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Subscribe
    public void getUser(LogInSubscriber event) {
        System.out.println("get user method event");
        if(event.getMessage().getMessage().startsWith("#authintication"))
              if (event.getMessage().getMessage().startsWith("#authintication successful!"))
                  authintication.set(1);
              else
                  authintication.set(2);
        this.user =  event.getMessage().getObject();

    }
    @FXML
    protected void onLoginButtonClick() {
        //validation
        Task<Void> eventTask = new Task<Void>() {
            @Override
            protected Void call() {
                System.out.println("send to server");
                askForUser(loginUsernameTextField.getText(), loginPasswordPasswordField.getText());
                while (authintication.get()==0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // Handle exception
                    }
                }
                Platform.runLater(() -> {
                    if (validateCredentials()) {
                        //login success
                        invalidLoginCredentials.setText("Login Successful!");
                        invalidLoginCredentials.setStyle(successMessage);
                        loginUsernameTextField.setStyle(successStyle);
                        try {
//                            EventBus.getDefault().unregister(LogInController.this);
                            SimpleChatClient.setRoot("primary");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        invalidLoginCredentials.setText("Invalid data");
                        loginUsernameTextField.setStyle(errorStyle);
                        loginPasswordPasswordField.setStyle(errorStyle);
                        loginUsernameTextField.setText("");
                        loginPasswordPasswordField.setText("");
                        authintication.set(0);
                    }

                });
                return null;
            }
        };
        new Thread(eventTask).start();
    }

    private void askForUser(String email,String password){
        try {
            Message message = new Message("#LogIn&"+ email + "&"+password);
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            showErrorMessage(Constants.INTERNAL_ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSignUpButtonClick() {

//        male.setToggleGroup(toggleGroup);
//        female.setToggleGroup(toggleGroup);
//        // Set one of the radio buttons as the default selection
//        male.setSelected(true);
//        // Create a horizontal box to hold the radio buttons
//        HBox hbox = new HBox(male, female);
//        toggleGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
//            selected = (RadioButton) newToggle;
//        });
        String username = signUpUsernameTextField.getText();
        String password = signUpPasswordPasswordField.getText();
        String repeatPassword = signUpRepeatPasswordPasswordField.getText();
        String email = signUpEmailTextField.getText();

        if(username.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank()){
            invalidSignupCredentials.setText("Please fill in all fields!");
            invalidSignupCredentials.setStyle(errorMessage);
            invalidLoginCredentials.setText("");
            if (username.isBlank()) {
                signUpUsernameTextField.setStyle(errorStyle);
            } else if (email.isBlank()) {
                signUpEmailTextField.setStyle(errorStyle);
            } else if (password.isBlank()) {
                signUpPasswordPasswordField.setStyle(errorStyle);
            } else if (repeatPassword.isBlank()) {
                signUpRepeatPasswordPasswordField.setStyle(errorStyle);
            }
            return;
        }
        if(!validateEmail(email)){
            invalidSignupCredentials.setText("Please add a valid email");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpEmailTextField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
            return;
        }
        if(!password.equals(repeatPassword)){
            invalidSignupCredentials.setText("The Passwords don't match!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpPasswordPasswordField.setStyle(errorStyle);
            signUpRepeatPasswordPasswordField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
            return;
        }
        if(!isStrongPassword(password)){
            invalidSignupCredentials.setText("Please add a valid password! ( Must Contains ...) ");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpPasswordPasswordField.setStyle(errorStyle);
            signUpRepeatPasswordPasswordField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
            return;
        }
        if(!addNewUser(username,password,email,signUpDateDatePicker.getValue(),"LastName")) {
            invalidSignupCredentials.setText("Something went wrong in the process, please try again!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpPasswordPasswordField.setStyle(errorStyle);
            signUpRepeatPasswordPasswordField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
            return;
        }

        invalidSignupCredentials.setText("Signed up successfully !!");
        invalidSignupCredentials.setStyle(successMessage);
        signUpUsernameTextField.setStyle(successStyle);
        signUpEmailTextField.setStyle(successStyle);
        signUpPasswordPasswordField.setStyle(successStyle);
        signUpRepeatPasswordPasswordField.setStyle(successStyle);
        invalidLoginCredentials.setText("");

    }
    public void showErrorMessage(Constants message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message.toString());
        alert.showAndWait();
    }
    private boolean addNewUser(String username, String password, String email, LocalDate value, String lastName) {
        try {
            Message message = new Message("#Register&"+ email + "&"+password+"&"+username+"&"+lastName);
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            showErrorMessage(Constants.INTERNAL_ERROR);
            e.printStackTrace();
        }
        return true;
    }

    private boolean isStrongPassword(String password) {
        // TODO: 10/01/2023 Check if the password is strong - valid.
        return true;
    }

    private boolean validateCredentials() {
        if (authintication.get()==2)
            return false;
        else if(authintication.get()==1)
            return true;
        return false;
    }

    private boolean validateEmail(String mail){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern).matcher(mail).matches();
    }


}