package creational.factorymethod;

public class Truck implements Transport {
    @Override
    public void deliver(String packageId) {
        System.out.println("Delivering package " + packageId + " by road using a truck.");
    }
}
