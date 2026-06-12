package strategy;

import model.ElevatorState;

import java.util.NavigableSet;

public interface ElevatorSchedulingStrategy {
    Integer pickNext(int currentFloor, ElevatorState state,
                     NavigableSet<Integer> upPickups,
                     NavigableSet<Integer> downPickups);
}