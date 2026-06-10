package model;

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

    public Elevator(String id) {
        this.id = id;
        this.upPickups = new TreeSet<>();
        this.downPickups = new TreeSet<>();
        this.cabinRequests = new TreeSet<>();
        this.currentFloor = 0;
        this.direction = null;
        this.state = ElevatorState.IDLE;
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
        Integer nextFloor = pickNextFromQueues();
        moveTo(nextFloor);
    }

    private Integer pickNextFromQueues() {

        if (direction == ElevatorDirection.UP || direction == null) {
            // prefer serving above first
            Integer next = nextAbove();
            if (next != null) return next;

            // nothing above, reverse
            direction = ElevatorDirection.DOWN;
            Integer nextDown = nextBelow();
            return nextDown;
        }

        Integer next = nextBelow();
        if (next != null) return next;

        // nothing below, reverse
        direction = ElevatorDirection.UP;
        Integer nextUp = nextAbove();
        return nextUp;
    }

    // nearest floor above (cabin dest or up pickup)
    private Integer nextAbove() {
        Integer cabin = cabinRequests.ceiling(currentFloor);
        Integer pickup = upPickups.ceiling(currentFloor);
        if (cabin == null) return pickup;
        if (pickup == null) return cabin;
        return Math.min(cabin, pickup);
    }

    // nearest floor below (cabin dest or down pickup)
    private Integer nextBelow() {
        Integer cabin  = cabinRequests.floor(currentFloor - 1);
        Integer pickup = downPickups.floor(currentFloor - 1);
        if (cabin == null) return pickup;
        if (pickup == null) return cabin;
        return Math.max(cabin, pickup); // nearest one wins
    }

    private Integer pickFromUpRequests() {
        Integer nextFloor = upPickups.ceiling(currentFloor);
        if (nextFloor == null) {return null;}
        upPickups.remove(nextFloor);
        return nextFloor;
    }

    private Integer pickFromDownRequests() {
        Integer nextFloor = downPickups.floor(currentFloor);
        if (nextFloor == null) {return null;}
        downPickups.remove(nextFloor);
        return nextFloor;
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
