package strategy;

import model.ElevatorDirection;

import java.util.NavigableSet;

public class LookSchedulingStrategy implements ElevatorSchedulingStrategy {

    @Override
    public Integer pickNext(int currentFloor, ElevatorDirection direction,
                            NavigableSet<Integer> upPickups,
                            NavigableSet<Integer> downPickups,
                            NavigableSet<Integer> cabinRequests) {
        if (direction == ElevatorDirection.UP || direction == null) {
            Integer next = nextAbove(currentFloor, upPickups, cabinRequests);
            if (next != null) return next;
            return nextBelow(currentFloor, downPickups, cabinRequests);
        }

        Integer next = nextBelow(currentFloor, downPickups, cabinRequests);
        if (next != null) return next;
        return nextAbove(currentFloor, upPickups, cabinRequests);
    }

    private Integer nextAbove(int currentFloor, NavigableSet<Integer> upPickups,
                               NavigableSet<Integer> cabinRequests) {
        Integer cabin = cabinRequests.ceiling(currentFloor);
        Integer pickup = upPickups.ceiling(currentFloor);
        if (cabin == null) return pickup;
        if (pickup == null) return cabin;
        return Math.min(cabin, pickup);
    }

    private Integer nextBelow(int currentFloor, NavigableSet<Integer> downPickups,
                               NavigableSet<Integer> cabinRequests) {
        Integer cabin = cabinRequests.floor(currentFloor - 1);
        Integer pickup = downPickups.floor(currentFloor - 1);
        if (cabin == null) return pickup;
        if (pickup == null) return cabin;
        return Math.max(cabin, pickup);
    }
}