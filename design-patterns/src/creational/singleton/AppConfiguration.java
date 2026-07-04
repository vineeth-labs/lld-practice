package creational.singleton;

public final class AppConfiguration {
    private static AppConfiguration instance;

    private final String environment;
    private final String databaseUrl;

    private AppConfiguration() {
        this.environment = "development";
        this.databaseUrl = "jdbc:mysql://localhost:3306/lld_practice";
    }

    public static synchronized AppConfiguration getInstance() {
        if (instance == null) {
            instance = new AppConfiguration();
        }

        return instance;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }
}
