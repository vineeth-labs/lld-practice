package strategy;

import model.ElevatorDirection;

import java.util.NavigableSet;

public interface ElevatorSchedulingStrategy {
    Integer pickNext(int currentFloor, ElevatorDirection direction,
                     NavigableSet<Integer> upPickups,
                     NavigableSet<Integer> downPickups,
                     NavigableSet<Integer> cabinRequests);
}