package behavioural.chainofresponsibility;

public class AccountSupportHandler extends SupportHandler {
    @Override
    protected boolean canHandle(SupportRequest request) {
        return request.getLevel() == SupportLevel.ACCOUNT;
    }

    @Override
    protected void process(SupportRequest request) {
        System.out.println("Account team handled request for " + request.getCustomerId()
                + ": " + request.getDescription());
    }
}
