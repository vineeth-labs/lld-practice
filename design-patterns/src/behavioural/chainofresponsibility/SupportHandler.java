package behavioural.chainofresponsibility;

public abstract class SupportHandler {
    private SupportHandler nextHandler;

    public SupportHandler setNextHandler(SupportHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public void handle(SupportRequest request) {
        if (canHandle(request)) {
            process(request);
            return;
        }

        if (nextHandler != null) {
            nextHandler.handle(request);
            return;
        }

        System.out.println("No handler available for request: " + request.getDescription());
    }

    protected abstract boolean canHandle(SupportRequest request);

    protected abstract void process(SupportRequest request);
}
