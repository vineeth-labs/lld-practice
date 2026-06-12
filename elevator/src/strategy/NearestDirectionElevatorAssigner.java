package strategy;

import model.*;

public class NearestDirectionElevatorAssigner extends ElevatorAssigner {
    private static final int PENALTY = 100;

    @Override
    protected int estimateCost(Elevator elevator, ExternalRequest request) {
        int requestFloor = request.getFloorNumber();
        int currentFloor = elevator.getCurrentFloor();
        int distance = Math.abs(requestFloor - currentFloor);

        ElevatorDirection elevatorDir = elevator.getDirection();

        if (elevatorDir == ElevatorDirection.IDLE) {
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

    private boolean isOnTheWay(ElevatorDirection direction,
                               int currentFloor,
                               int targetFloor) {
        return direction == ElevatorDirection.UP
                ? targetFloor >= currentFloor
                : targetFloor <= currentFloor;
    }

    private boolean isSameDirection(ElevatorDirection elevatorDir,
                                    Direction requestDir) {
        return (elevatorDir == ElevatorDirection.UP && requestDir == Direction.UP)
                || (elevatorDir == ElevatorDirection.DOWN && requestDir == Direction.DOWN);
    }

}
