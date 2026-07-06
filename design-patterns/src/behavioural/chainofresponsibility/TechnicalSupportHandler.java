package behavioural.chainofresponsibility;

public class TechnicalSupportHandler extends SupportHandler {
    @Override
    protected boolean canHandle(SupportRequest request) {
        return request.getLevel() == SupportLevel.TECHNICAL;
    }

    @Override
    protected void process(SupportRequest request) {
        System.out.println("Technical team handled request for " + request.getCustomerId()
                + ": " + request.getDescription());
    }
}
