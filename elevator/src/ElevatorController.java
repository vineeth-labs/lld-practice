import model.Elevator;
import model.ExternalRequest;
import model.InternalRequest;
import strategy.ElevatorAssigner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElevatorController {
    Map<String, Elevator> elevators;
    ElevatorAssigner assigner;

    public ElevatorController(List<Elevator> elevators, ElevatorAssigner assigner) {
        this.elevators = new HashMap<>();
        for (Elevator e : elevators) {
            this.elevators.put(e.getId(), e);
            Thread t = new Thread(e, "elevator-" + e.getId());
            t.setDaemon(true);
            t.start();
        }
        this.assigner = assigner;
    }

    public void handleInternalRequest(String elevatorId, InternalRequest internalRequest) {
        Elevator elevator = elevators.get(elevatorId);
        elevator.assignInternalRequest(internalRequest);
    }

    public void handleExternalRequest(ExternalRequest externalRequest) {
        Elevator optimalElevator = assigner.assign(elevators.values().stream().toList(), externalRequest);
        if (optimalElevator != null) {
            optimalElevator.assignExternalRequest(externalRequest);
        } else {
            System.out.println("No available elevator found");
        }
    }


}
