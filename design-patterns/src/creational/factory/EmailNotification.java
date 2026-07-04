package creational.factory;

public class EmailNotification implements Notification {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Sending email to " + recipient + ": " + message);
    }
}
