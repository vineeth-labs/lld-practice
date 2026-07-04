package creational.factory;

public class PushNotification implements Notification {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Sending push notification to " + recipient + ": " + message);
    }
}
