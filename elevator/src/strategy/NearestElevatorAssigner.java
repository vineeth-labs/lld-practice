package strategy;

import model.Elevator;
import model.ExternalRequest;

import java.util.List;

public class NearestElevatorAssigner extends ElevatorAssigner {
    @Override
    public Elevator assign(List<Elevator> elevators, ExternalRequest request) {
        Elevator optimalElevator = null;
        int minCost = Integer.MAX_VALUE;
        for (Elevator elevator : elevators) {
            int cost = estimateCost(elevator, request);
            if (cost < minCost) {
                minCost = cost;
                optimalElevator = elevator;
            }
        }
        return optimalElevator;
    }

    @Override
    protected int estimateCost(Elevator elevator, ExternalRequest externalRequest) {
        int floor =  externalRequest.getFloorNumber();
        int currentFloor = elevator.getCurrentFloor();
        return Math.abs(floor - currentFloor);
    }
}
