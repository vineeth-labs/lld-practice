package creational.builder;

public class House {
    private final int numberOfRooms;
    private final int numberOfBathrooms;
    private final boolean hasGarage;
    private final boolean hasGarden;
    private final boolean hasSwimmingPool;

    private House(Builder builder) {
        this.numberOfRooms = builder.numberOfRooms;
        this.numberOfBathrooms = builder.numberOfBathrooms;
        this.hasGarage = builder.hasGarage;
        this.hasGarden = builder.hasGarden;
        this.hasSwimmingPool = builder.hasSwimmingPool;
    }

    @Override
    public String toString() {
        return "House{" +
                "numberOfRooms=" + numberOfRooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", hasGarage=" + hasGarage +
                ", hasGarden=" + hasGarden +
                ", hasSwimmingPool=" + hasSwimmingPool +
                '}';
    }

    public static class Builder {
        private final int numberOfRooms;
        private final int numberOfBathrooms;
        private boolean hasGarage;
        private boolean hasGarden;
        private boolean hasSwimmingPool;

        public Builder(int numberOfRooms, int numberOfBathrooms) {
            this.numberOfRooms = numberOfRooms;
            this.numberOfBathrooms = numberOfBathrooms;
        }

        public Builder withGarage() {
            this.hasGarage = true;
            return this;
        }

        public Builder withGarden() {
            this.hasGarden = true;
            return this;
        }

        public Builder withSwimmingPool() {
            this.hasSwimmingPool = true;
            return this;
        }

        public House build() {
            return new House(this);
        }
    }
}
