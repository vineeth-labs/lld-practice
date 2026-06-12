package model;

import java.util.NavigableSet;

public interface ElevatorState {
    boolean isIdle();
    Direction getDirection();
    void serveFloor(NavigableSet<Integer> upPickups, NavigableSet<Integer> downPickups, int currentFloor);
    ElevatorState afterServing(boolean hasNoRequests);
}