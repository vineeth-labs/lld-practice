import model.Elevator;
import model.ExternalRequest;
import model.InternalRequest;
import strategy.ElevatorAssigner;

import java.util.List;
import java.util.Map;

public class ElevatorController {
    Map<String, Elevator> elevators;
    ElevatorAssigner assigner;

    public ElevatorController(List<Elevator> elevators, ElevatorAssigner assigner) {

    }

    public void handleInternalRequest(String elevatorId, InternalRequest internalRequest) {
        Elevator elevator = elevators.get(elevatorId);
        elevator.assignRequest(internalRequest);
    }

    public void handleExternalRequest(ExternalRequest externalRequest) {
        // if request direction is UP
            // pick the nearest moving UP/IDLE elevator  below it  (ideal)
            // if not: pick the nearest moving DOWN elevator below it
            // if not: pick any elevator above it and assign (moving down ones greater than moving up ones)
        // if request direction is DOWN
            // pick the nearest moving DOWN/IDLE elevator above it (ideal)
            // if not: pick the nearest moving UP elevator above it
            // if not: pick any elevator below it (moving up ones greater than moving down ones)
        Elevator optimalElevator = assigner.assign(elevators.values().stream().toList(), externalRequest);
        if (optimalElevator != null) {
            optimalElevator.assignRequest(externalRequest);
        } else {
            System.out.println("No available elevator found");
        }
    }


}
