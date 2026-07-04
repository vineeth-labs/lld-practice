package creational.factorymethod;

public class Ship implements Transport {
    @Override
    public void deliver(String packageId) {
        System.out.println("Delivering package " + packageId + " by sea using a ship.");
    }
}
