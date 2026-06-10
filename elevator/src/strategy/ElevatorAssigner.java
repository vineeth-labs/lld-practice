package strategy;

import model.Elevator;
import model.ExternalRequest;

import java.util.List;

public interface ElevatorAssigner {
    public Elevator assign(List<Elevator> elevators, ExternalRequest request);
}
