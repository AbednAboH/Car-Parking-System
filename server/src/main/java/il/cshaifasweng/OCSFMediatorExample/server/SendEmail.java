package il.cshaifasweng.OCSFMediatorExample.server;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SendEmail {

    private static final String PROPERTIES_FILE =SendEmail.class.getClassLoader().getResource("email.properties").getPath();
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getEmail() {
        return properties.getProperty("email");
    }

    public static String getPassword() {
        return properties.getProperty("password");
    }

    public static void main(String[] args) {

        sendEmail("freazer122@gmail.com","testing","the body is here\n is it good ?");
    }

    public static void sendEmail( String email, String subject, String body) {
        Session session = getSession();
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("TestForChecking@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("couldn't Send Email");;
        }
    }

    private static Session getSession() {
        final String username = getEmail();
        final String password = getPassword();
        System.out.println(getEmail()+" "+getPassword());
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return session;
    }

}