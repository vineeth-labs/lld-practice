package strategy;

import model.ElevatorDirection;

import java.util.NavigableSet;

public class LookSchedulingStrategy implements ElevatorSchedulingStrategy {

    @Override
    public Integer pickNext(int currentFloor, ElevatorDirection direction,
                            NavigableSet<Integer> upPickups,
                            NavigableSet<Integer> downPickups) {
        if (direction == ElevatorDirection.UP || direction == null) {
            Integer next = upPickups.ceiling(currentFloor);
            // When idle, also consider downPickups above (must travel up to reach them)
            if (direction == null) {
                Integer downAbove = downPickups.ceiling(currentFloor);
                if (downAbove != null && (next == null || downAbove < next)) next = downAbove;
            }
            if (next != null) return next;
            return downPickups.floor(currentFloor - 1);
        }

        Integer next = downPickups.floor(currentFloor - 1);
        if (next != null) return next;
        return upPickups.ceiling(currentFloor);
    }
}
