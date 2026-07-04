package creational.builder;

public class Main {
    public static void main(String[] args) {
        House simpleHouse = new House.Builder(2, 1)
                .build();

        House luxuryHouse = new House.Builder(5, 4)
                .withGarage()
                .withGarden()
                .withSwimmingPool()
                .build();

        System.out.println(simpleHouse);
        System.out.println(luxuryHouse);
    }
}
