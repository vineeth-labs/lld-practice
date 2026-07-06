package behavioural.chainofresponsibility;

public class BillingSupportHandler extends SupportHandler {
    @Override
    protected boolean canHandle(SupportRequest request) {
        return request.getLevel() == SupportLevel.BILLING;
    }

    @Override
    protected void process(SupportRequest request) {
        System.out.println("Billing team handled request for " + request.getCustomerId()
                + ": " + request.getDescription());
    }
}
