package strategy;

import model.*;

public class NearestDirectionElevatorAssigner extends ElevatorAssigner {
    private static final int PENALTY = 100;

    @Override
    protected int estimateCost(Elevator elevator, ExternalRequest request) {
        int requestFloor = request.getFloorNumber();
        int currentFloor = elevator.getCurrentFloor();
        int distance = Math.abs(requestFloor - currentFloor);

        ElevatorState elevatorDir = elevator.getState();

        if (elevatorDir == ElevatorState.IDLE) {
            return distance;
        }

        if (!isSameDirection(elevatorDir, request.getDirection())) {
            return distance + PENALTY * 2;
        }

        if (!isOnTheWay(elevatorDir, currentFloor, requestFloor)) {
            return distance + PENALTY;
        }

        return distance;
    }

    private boolean isOnTheWay(ElevatorState state,
                               int currentFloor,
                               int targetFloor) {
        return state == ElevatorState.MOVING_UP
                ? targetFloor >= currentFloor
                : targetFloor <= currentFloor;
    }

    private boolean isSameDirection(ElevatorState state,
                                    Direction requestDir) {
        return (state == ElevatorState.MOVING_UP && requestDir == Direction.UP)
                || (state == ElevatorState.MOVING_DOWN && requestDir == Direction.DOWN);
    }

}
