package creational.singleton;

public class Main {
    public static void main(String[] args) {
        AppConfiguration firstConfig = AppConfiguration.getInstance();
        AppConfiguration secondConfig = AppConfiguration.getInstance();

        System.out.println("Environment: " + firstConfig.getEnvironment());
        System.out.println("Database URL: " + firstConfig.getDatabaseUrl());
        System.out.println("Both references point to same object: " + (firstConfig == secondConfig));
    }
}
