package creational.factorymethod;

public abstract class Logistics {
    public void planDelivery(String packageId) {
        Transport transport = createTransport();
        transport.deliver(packageId);
    }

    protected abstract Transport createTransport();
}
