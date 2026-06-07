import model.Entrance;
import model.ParkingLot;
import model.ParkingSpot;
import model.VehicleType;

public interface SpotAllocationStrategy {
    public ParkingSpot getOptimalSpot(ParkingLot parkingLot, Entrance entrance, VehicleType vehicleType);
}
