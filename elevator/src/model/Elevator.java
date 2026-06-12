package model;

import strategy.ElevatorSchedulingStrategy;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Elevator {
    private String id;
    private final NavigableSet<Integer> upPickups;
    private final NavigableSet<Integer> downPickups;
    private final NavigableSet<Integer> cabinRequests;
    private int currentFloor;
    private ElevatorDirection direction;
    private ElevatorState state;
    private final ElevatorSchedulingStrategy schedulingStrategy;

    public Elevator(String id, ElevatorSchedulingStrategy schedulingStrategy) {
        this.id = id;
        this.upPickups = new TreeSet<>();
        this.downPickups = new TreeSet<>();
        this.cabinRequests = new TreeSet<>();
        this.currentFloor = 0;
        this.direction = null;
        this.state = ElevatorState.IDLE;
        this.schedulingStrategy = schedulingStrategy;
    }

    public String getId() { return id; }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public ElevatorState getState() {
        return state;
    }

    public void assignInternalRequest(InternalRequest internalRequest) {
        cabinRequests.add(internalRequest.getFloorNumber());
    }

    public void assignExternalRequest(ExternalRequest externalRequest) {
        addExternalRequest(externalRequest);
    }

    private void addExternalRequest(ExternalRequest request) {
        int from = request.getFloorNumber();
        if (request.getDirection() == Direction.UP) {
            upPickups.add(from);
        } else {
            downPickups.add(from);
        }
        if (state == ElevatorState.IDLE) {
            state = ElevatorState.MOVING;
        }
    }

    public void pickNextFloor() {
        Integer nextFloor = schedulingStrategy.pickNext(
                currentFloor, direction,
                Collections.unmodifiableNavigableSet(upPickups),
                Collections.unmodifiableNavigableSet(downPickups),
                Collections.unmodifiableNavigableSet(cabinRequests));
        moveTo(nextFloor);
    }

    private void moveTo(Integer nextFloor) {
        if (nextFloor == null || nextFloor == currentFloor) {
            direction = null;
            state = ElevatorState.IDLE;
            return;
        }

        state = ElevatorState.MOVING;
        direction = nextFloor > currentFloor ? ElevatorDirection.UP : ElevatorDirection.DOWN;

        System.out.println("Moving from " + currentFloor + " to " + nextFloor);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        currentFloor = nextFloor;
        serveCurrentFloor();
    }

    private void serveCurrentFloor() {
        cabinRequests.remove(currentFloor);

        if (direction == ElevatorDirection.UP)
            upPickups.remove(currentFloor);
        else if (direction == ElevatorDirection.DOWN)
            downPickups.remove(currentFloor);
    }
}
