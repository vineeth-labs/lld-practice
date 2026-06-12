package model;

import java.util.NavigableSet;

public class MovingUpState implements ElevatorState {

    @Override
    public boolean isIdle() { return false; }

    @Override
    public Direction getDirection() { return Direction.UP; }

    @Override
    public void serveFloor(NavigableSet<Integer> upPickups, NavigableSet<Integer> downPickups, int currentFloor) {
        if (upPickups.contains(currentFloor)) {
            upPickups.remove(currentFloor);
        } else {
            // Repositioning: traveled UP to reach a downPickup
            downPickups.remove(currentFloor);
        }
    }

    @Override
    public ElevatorState afterServing(boolean hasNoRequests) {
        return hasNoRequests ? new IdleState() : this;
    }
}