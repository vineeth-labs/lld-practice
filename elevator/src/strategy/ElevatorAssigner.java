package strategy;

import model.Elevator;
import model.ExternalRequest;

import java.util.List;

abstract public class ElevatorAssigner {
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

    abstract protected int estimateCost(Elevator elevator, ExternalRequest request);
}
