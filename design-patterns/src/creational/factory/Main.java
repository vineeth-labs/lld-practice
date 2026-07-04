package creational.factory;

public class Main {
    public static void main(String[] args) {
        NotificationFactory notificationFactory = new NotificationFactory();

        Notification emailNotification = notificationFactory.createNotification(NotificationType.EMAIL);
        emailNotification.send("student@example.com", "Your assignment is due today.");

        Notification smsNotification = notificationFactory.createNotification(NotificationType.SMS);
        smsNotification.send("+91-9876543210", "Your OTP is 123456.");

        Notification pushNotification = notificationFactory.createNotification(NotificationType.PUSH);
        pushNotification.send("user-123", "You have a new message.");
    }
}
