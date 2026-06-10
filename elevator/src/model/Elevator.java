package model;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Elevator {
    private String id;
    private final NavigableSet<Integer> upRequests;
    private final NavigableSet<Integer> downRequests;
    private final NavigableSet<Integer> cabinRequests;
    private int currentFloor;
    private ElevatorDirection direction;
    private ElevatorState state;

    public Elevator(String id) {
        this.id = id;
        this.upRequests = new TreeSet<>();
        this.downRequests = new TreeSet<>();
        this.cabinRequests = new TreeSet<>();
        this.currentFloor = 0;
        this.direction = null;
        this.state = ElevatorState.IDLE;
    }

    public String getId() { return id; }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void assignInternalRequest(InternalRequest internalRequest) {
        cabinRequests.add(internalRequest.getFloorNumber());
    }

    public void assignExternalRequest(ExternalRequest externalRequest) {
        addExternalRequest(externalRequest);
    }

    private void addInternalRequest(int destination) {
        if (destination > currentFloor) {
            addToUp(destination);
        } else if (destination < currentFloor) {
            addToDown(destination);
        }
    }

    private void addExternalRequest(ExternalRequest request) {
        int from = request.getFloorNumber();
        Direction reqDir = request.getDirection();

        if (state == ElevatorState.IDLE) {
            // when idle, decide based on source position
            if (from > currentFloor) addToUp(from);
            else if (from < currentFloor) addToDown(from);
            return;
        }

        // if moving, try to honor request direction
        if (reqDir == Direction.UP) addToUp(from);
        else addToDown(from);
    }

    private void addToUp(int floor) { upRequests.add(floor); }
    private void addToDown(int floor) { downRequests.add(floor); }

    public void pickNextFloor() {
        Integer nextFloor = pickNextFromQueues();
        moveTo(nextFloor);
    }

    private Integer pickNextFromQueues() {
        if (state == ElevatorState.IDLE || direction == null || direction == ElevatorDirection.UP) {
            Integer next = pickFromUpRequests();
            return next != null ? next : pickFromDownRequests();
        }
        // direction == DOWN
        Integer next = pickFromDownRequests();
        return next != null ? next : pickFromUpRequests();
    }

    private Integer pickFromUpRequests() {
        Integer nextFloor = upRequests.ceiling(currentFloor);
        if (nextFloor == null) {return null;}
        upRequests.remove(nextFloor);
        return nextFloor;
    }

    private Integer pickFromDownRequests() {
        Integer nextFloor = downRequests.floor(currentFloor);
        if (nextFloor == null) {return null;}
        downRequests.remove(nextFloor);
        return nextFloor;
    }

    private void moveTo(Integer nextFloor) {
        if (nextFloor == null || nextFloor == currentFloor) {
            direction = null;
            state = ElevatorState.IDLE;
            return;
        }
        if (state == ElevatorState.IDLE) {
            state = ElevatorState.MOVING;
        }
        if (nextFloor >  currentFloor) {
            direction = ElevatorDirection.UP;
        } else {
            direction = ElevatorDirection.DOWN;
        }
        System.out.println("Moving from " + currentFloor + " to " + nextFloor);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        currentFloor = nextFloor;
    }
}
