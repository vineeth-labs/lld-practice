package behavioural.chainofresponsibility;

public class SupportRequest {
    private final String customerId;
    private final SupportLevel level;
    private final String description;

    public SupportRequest(String customerId, SupportLevel level, String description) {
        this.customerId = customerId;
        this.level = level;
        this.description = description;
    }

    public String getCustomerId() {
        return customerId;
    }

    public SupportLevel getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }
}
