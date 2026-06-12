package model;

import java.util.NavigableSet;

public class MovingDownState implements ElevatorState {

    @Override
    public boolean isIdle() { return false; }

    @Override
    public Direction getDirection() { return Direction.DOWN; }

    @Override
    public void serveFloor(NavigableSet<Integer> upPickups, NavigableSet<Integer> downPickups, int currentFloor) {
        if (downPickups.contains(currentFloor)) {
            downPickups.remove(currentFloor);
        } else {
            // Repositioning: traveled DOWN to reach an upPickup
            upPickups.remove(currentFloor);
        }
    }

    @Override
    public ElevatorState afterServing(boolean hasNoRequests) {
        return hasNoRequests ? new IdleState() : this;
    }
}