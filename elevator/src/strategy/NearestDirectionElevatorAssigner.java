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

        if (elevatorDir.isIdle()) {
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
        return state.getDirection() == Direction.UP
                ? targetFloor >= currentFloor
                : targetFloor <= currentFloor;
    }

    private boolean isSameDirection(ElevatorState state,
                                    Direction requestDir) {
        return state.getDirection() == requestDir;
    }

}
