package strategy;

import model.ElevatorDirection;

import java.util.NavigableSet;

public class LookSchedulingStrategy implements ElevatorSchedulingStrategy {

    @Override
    public Integer pickNext(int currentFloor, ElevatorDirection direction,
                            NavigableSet<Integer> upPickups,
                            NavigableSet<Integer> downPickups) {
        if (direction == ElevatorDirection.UP) {
            Integer next = upPickups.ceiling(currentFloor);
            if (next != null) return next;
            return highest(
                    upPickups.lower(currentFloor),
                    downPickups.floor(currentFloor));
        }

        if (direction == ElevatorDirection.DOWN) {
            Integer next = downPickups.floor(currentFloor);
            if (next != null) return next;
            return lowest(
                    upPickups.ceiling(currentFloor),
                    downPickups.higher(currentFloor));
        }

        Integer above = lowest(
                upPickups.ceiling(currentFloor),
                downPickups.ceiling(currentFloor));
        Integer below = highest(
                upPickups.lower(currentFloor),
                downPickups.lower(currentFloor));

        if (above == null) return below;
        if (below == null) return above;
        return above - currentFloor <= currentFloor - below ? above : below;
    }

    private Integer lowest(Integer first, Integer second) {
        if (first == null) return second;
        if (second == null) return first;
        return Math.min(first, second);
    }

    private Integer highest(Integer first, Integer second) {
        if (first == null) return second;
        if (second == null) return first;
        return Math.max(first, second);
    }
}
