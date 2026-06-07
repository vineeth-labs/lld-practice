package model;

import java.util.List;
import java.util.Map;

public class ParkingFloor {
    int level;
    Map<SpotType, List<ParkingSpot>> spotsMap;

    public int getLevel() {
        return level;
    }

    public Map<SpotType, List<ParkingSpot>> getSpotsMap() {
        return spotsMap;
    }
}
