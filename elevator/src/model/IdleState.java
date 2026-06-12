package model;

import java.util.NavigableSet;

public class IdleState implements ElevatorState {

    @Override
    public boolean isIdle() { return true; }

    @Override
    public Direction getDirection() { return null; }

    @Override
    public void serveFloor(NavigableSet<Integer> upPickups, NavigableSet<Integer> downPickups, int currentFloor) {
        upPickups.remove(currentFloor);
        downPickups.remove(currentFloor);
    }

    @Override
    public ElevatorState afterServing(boolean hasNoRequests) {
        return this;
    }
}
