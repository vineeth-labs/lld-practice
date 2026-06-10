package model;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Elevator {
    String id;
    NavigableSet<Integer> upRequests;
    NavigableSet<Integer> downRequests;
    int currentFloor;
    ElevatorDirection direction;
    ElevatorState state;

    public Elevator(String id) {
        this.id = id;
        upRequests = new TreeSet<>();
        downRequests = new TreeSet<>();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void assignRequest(Request request){
        if (request instanceof InternalRequest) {
            int destination = request.getFloorNumber();
            if (destination > currentFloor) {
                upRequests.add(destination);
            } else if (destination < currentFloor) {
                downRequests.add(destination);
            } else {
                // do nothing
            }
        } else {
            Direction direction = ((ExternalRequest) request).getDirection();
            int from = request.getFloorNumber();
            if (state == ElevatorState.IDLE) {
                if (from > currentFloor) {
                    upRequests.add(from);
                } else if (from < currentFloor) {
                    downRequests.add(from);
                } else {
                    // do nothing
                }
            } else {
                if (direction == Direction.UP) {
                    upRequests.add(from);
                }  else if (direction == Direction.DOWN) {
                    downRequests.add(from);
                }
            }
        }
    }

    public void pickNextFloor() {
        Integer nextFloor = null;
        if (state == ElevatorState.IDLE || direction == ElevatorDirection.UP) {
            nextFloor = pickFromUpRequests();
            if (nextFloor == null) {
                nextFloor = pickFromDownRequests();
            }
        } else if (direction == ElevatorDirection.DOWN) {
            nextFloor = pickFromDownRequests();
            if (nextFloor  == null) {
                nextFloor = pickFromUpRequests();
            }
        }
        moveTo(nextFloor);
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
