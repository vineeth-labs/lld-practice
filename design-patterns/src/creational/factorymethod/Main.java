package creational.factorymethod;

public class Main {
    public static void main(String[] args) {
        Logistics roadLogistics = new RoadLogistics();
        roadLogistics.planDelivery("PKG-101");

        Logistics seaLogistics = new SeaLogistics();
        seaLogistics.planDelivery("PKG-202");
    }
}
