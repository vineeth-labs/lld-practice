package strategy;

import model.Elevator;
import model.ExternalRequest;

import java.util.List;

public class NearestElevatorAssigner implements ElevatorAssigner {
    @Override
    public Elevator assign(List<Elevator> elevators, ExternalRequest request) {
        Elevator optimalElevator = null;
        int minCost = Integer.MAX_VALUE;
        for (Elevator elevator : elevators) {
            int cost = estimateCost(elevator, request);
            if (cost < minCost) {
                minCost = estimateCost(elevator, request);
                optimalElevator = elevator;
            }
        }
        return optimalElevator;
    }

    private int estimateCost(Elevator elevator, ExternalRequest externalRequest) {
        int floor =  externalRequest.getFloorNumber();
        int currentFloor = elevator.getCurrentFloor();
        return Math.abs(floor - currentFloor);
    }
}
