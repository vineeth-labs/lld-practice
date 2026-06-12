import model.Direction;
import model.Elevator;
import model.ExternalRequest;
import model.InternalRequest;
import strategy.ElevatorSchedulingStrategy;
import strategy.LookSchedulingStrategy;
import strategy.NearestDirectionElevatorAssigner;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ElevatorSchedulingStrategy schedulingStrategy = new LookSchedulingStrategy();
        List<Elevator> elevators = List.of(
                new Elevator("E1", schedulingStrategy),
                new Elevator("E2", schedulingStrategy)
        );

        ElevatorController controller = new ElevatorController(elevators, new NearestDirectionElevatorAssigner());

        // Burst of external requests — both elevators should be assigned work
        controller.handleExternalRequest(new ExternalRequest(3, Direction.UP));
        controller.handleExternalRequest(new ExternalRequest(7, Direction.DOWN));
        Thread.sleep(500);

        // Internal request while E1 is already moving
        controller.handleInternalRequest("E1", new InternalRequest(10));
        Thread.sleep(500);

        // New external request arrives mid-run
        controller.handleExternalRequest(new ExternalRequest(5, Direction.UP));
        Thread.sleep(500);
        controller.handleExternalRequest(new ExternalRequest(2, Direction.DOWN));

        // Process all queued requests, then stop and wait for every elevator worker.
        controller.shutdown();
        controller.awaitTermination();
        System.out.println("Done.");
    }
}
