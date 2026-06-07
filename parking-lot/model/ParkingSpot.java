package model;

public class ParkingSpot {
    int id;
    SpotType type;
    ParkingFloor floor;
    SpotStatus status;
    Location location;

    ParkingSpot(int id, SpotType type,  ParkingFloor floor,  SpotStatus status) {
        this.id = id;
        this.type = type;
        this.floor = floor;
        this.status = status;
    }

    public boolean isCompatibleSpotType(VehicleType vehicleType) {
        return this.type == getCompatibleSpotType(vehicleType);
    }

    public static SpotType getCompatibleSpotType(VehicleType vehicleType) {
        switch (vehicleType) {
            case CAR:
                return SpotType.MEDIUM;
            case BIKE:
                return SpotType.SMALL;
            case TRUCK:
                return SpotType.LARGE;
        }
        return null;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public void setStatus(SpotStatus status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void park() {
        setStatus(SpotStatus.OCCUPIED);
    }
    public void unpark() {
        setStatus(SpotStatus.AVAILABLE);
    }

    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }
}
