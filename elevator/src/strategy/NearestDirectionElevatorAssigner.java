package strategy;

import model.*;

public class NearestDirectionElevatorAssigner extends ElevatorAssigner {
    private static final int PENALTY = 100;

    @Override
    protected int estimateCost(Elevator elevator, ExternalRequest request) {
        int requestFloor = request.getFloorNumber();
        Direction requestDir = request.getDirection();
        int currentFloor = elevator.getCurrentFloor();
        ElevatorDirection elevatorDir = elevator.getDirection();
        ElevatorState state = elevator.getState();
        int distance = Math.abs(requestFloor - currentFloor);

        // IDLE elevator — always a good candidate, just costs distance
        if (state == ElevatorState.IDLE) {
            return distance;
        }

        // Moving in SAME direction AND will pass through request floor on the way
        if (elevatorDir == ElevatorDirection.UP && requestDir == Direction.UP
                && currentFloor <= requestFloor) {
            return distance; // best case — scoop them up on the way
        }

        if (elevatorDir == ElevatorDirection.DOWN && requestDir == Direction.DOWN
                && currentFloor >= requestFloor) {
            return distance; // best case — scoop them up on the way
        }

        // Moving in same direction but already PASSED the floor
        // has to finish current direction, reverse, come back
        if (elevatorDir == ElevatorDirection.UP && requestDir == Direction.UP
                && currentFloor > requestFloor) {
            return distance + PENALTY;
        }

        if (elevatorDir == ElevatorDirection.DOWN && requestDir == Direction.DOWN
                && currentFloor < requestFloor) {
            return distance + PENALTY;
        }

        // Moving in OPPOSITE direction — worst case, has to finish and reverse
        return distance + PENALTY * 2;
    }
}
