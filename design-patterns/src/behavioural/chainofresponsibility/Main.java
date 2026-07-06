package behavioural.chainofresponsibility;

public class Main {
    public static void main(String[] args) {
        SupportHandler supportChain = new BillingSupportHandler();
        supportChain
                .setNextHandler(new TechnicalSupportHandler())
                .setNextHandler(new AccountSupportHandler());

        supportChain.handle(new SupportRequest("CUST-101", SupportLevel.BILLING,
                "Refund not reflected in account."));
        supportChain.handle(new SupportRequest("CUST-202", SupportLevel.TECHNICAL,
                "Unable to upload documents."));
        supportChain.handle(new SupportRequest("CUST-303", SupportLevel.ACCOUNT,
                "Need to update registered email."));
        supportChain.handle(new SupportRequest("CUST-404", SupportLevel.UNKNOWN,
                "Need help with an unsupported category."));
    }
}
