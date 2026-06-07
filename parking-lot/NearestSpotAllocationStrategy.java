import model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NearestSpotAllocationStrategy implements SpotAllocationStrategy {

    @Override
    public ParkingSpot getOptimalSpot(ParkingLot parkingLot, Entrance entrance, VehicleType vehicleType) {
        List<ParkingFloor> floors = parkingLot.getFloors();
        List<ParkingFloor> sortedFloors = new ArrayList<>(floors);
        Collections.sort(sortedFloors, Comparator.comparingInt(ParkingFloor::getLevel));
        Double lowestDistance = Double.MAX_VALUE;
        for (ParkingFloor floor : sortedFloors) {
            ParkingSpot nearestSpot = null;
            for (ParkingSpot spot: floor.getSpotsMap().get(ParkingSpot.getCompatibleSpotType(vehicleType))) {
                if (!spot.isAvailable()) continue;
                Location spotLocation = spot.getLocation();
                Location entranceLocation = entrance.getLocation();
                // calculate distance
                Double distance = Math.sqrt(Math.pow(spotLocation.X - entranceLocation.X, 2) + Math.pow(spotLocation.Y - entranceLocation.Y, 2));
                if (distance < lowestDistance) {
                    nearestSpot = spot;
                    lowestDistance = distance;
                }
            }
            if (nearestSpot != null) {
                return nearestSpot;
            }
        }
        return null;
    }
}
