package model;

import strategy.ElevatorSchedulingStrategy;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Elevator implements Runnable {
    private String id;

    // upPickups: floors where someone wants to go UP (external button or internal request above current floor)
    // downPickups: floors where someone wants to go DOWN (external button or internal request below current floor)
    private final NavigableSet<Integer> upPickups;
    private final NavigableSet<Integer> downPickups;

    private int currentFloor;
    private ElevatorState state;
    private boolean shutdownRequested;
    private final ElevatorSchedulingStrategy schedulingStrategy;

    public Elevator(String id, ElevatorSchedulingStrategy schedulingStrategy) {
        this.id = id;
        this.upPickups = new TreeSet<>();
        this.downPickups = new TreeSet<>();
        this.currentFloor = 0;
        this.state = new IdleState();
        this.schedulingStrategy = schedulingStrategy;
    }

    public String getId() { return id; }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorState getState() {
        return state;
    }

    public synchronized void run() {
        while (true) {
            while (state.isIdle() && hasNoRequests()) {
                if (shutdownRequested) {
                    return;
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            pickNextFloor();
        }
    }

    public synchronized void shutdown() {
        shutdownRequested = true;
        notifyAll();
    }

    private boolean hasNoRequests() {
        return upPickups.isEmpty() && downPickups.isEmpty();
    }

    public synchronized void assignInternalRequest(InternalRequest internalRequest) {
        int floor = internalRequest.getFloorNumber();
        if (floor > currentFloor) {
            upPickups.add(floor);
        } else if (floor < currentFloor) {
            downPickups.add(floor);
        }
        notifyAll();
    }

    public synchronized void assignExternalRequest(ExternalRequest externalRequest) {
        addExternalRequest(externalRequest);
        notifyAll();
    }

    private void addExternalRequest(ExternalRequest request) {
        int from = request.getFloorNumber();
        if (request.getDirection() == Direction.UP) {
            upPickups.add(from);
        } else {
            downPickups.add(from);
        }
    }

    public synchronized void pickNextFloor() {
        Integer nextFloor = schedulingStrategy.pickNext(
                currentFloor, state,
                Collections.unmodifiableNavigableSet(upPickups),
                Collections.unmodifiableNavigableSet(downPickups));
        moveTo(nextFloor);
    }

    private void moveTo(Integer nextFloor) {
        if (nextFloor == null) {
            state = new IdleState();
            return;
        }

        if (nextFloor == currentFloor) {
            state.serveFloor(upPickups, downPickups, currentFloor);
            state = state.afterServing(hasNoRequests());
            return;
        }

        state = nextFloor > currentFloor ? new MovingUpState() : new MovingDownState();

        System.out.println("[" + id + "] Moving from floor " + currentFloor + " to floor " + nextFloor);
        try {
            long deadline = System.currentTimeMillis() + 1000;
            long remaining;
            while ((remaining = deadline - System.currentTimeMillis()) > 0) {
                wait(remaining);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        currentFloor = nextFloor;
        state.serveFloor(upPickups, downPickups, currentFloor);
        state = state.afterServing(hasNoRequests());
        System.out.println("[" + id + "] Arrived at floor " + currentFloor);
    }
}
