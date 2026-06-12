package strategy;

import model.Elevator;
import model.ExternalRequest;

import java.util.Comparator;
import java.util.List;

public class NearestElevatorAssigner extends ElevatorAssigner {

    @Override
    public Elevator assign(List<Elevator> elevators, ExternalRequest request) {
        return elevators.stream()
                .min(Comparator.comparingInt(e -> estimateCost(e, request)))
                .orElseThrow(() -> new IllegalArgumentException("No elevators available"));
    }

    @Override
    protected int estimateCost(Elevator elevator, ExternalRequest request) {
        return Math.abs(request.getFloorNumber() - elevator.getCurrentFloor());
    }
}
