# Elevator Low Level Design

This project is a Java low level design practice implementation for an elevator system. It models multiple elevators, external hall requests, internal destination requests, elevator movement state, request scheduling, and elevator assignment strategies.

The code is intentionally small and interview-focused: it separates responsibilities clearly and leaves room to discuss tradeoffs, extensions, and alternate strategies.

## Requirements Covered

- Support multiple elevators managed by a central controller.
- Accept external requests from a floor with a desired direction.
- Accept internal requests from inside a specific elevator.
- Keep each elevator running independently on its own worker thread.
- Track elevator state as idle, moving up, or moving down.
- Schedule stops using a LOOK-style algorithm.
- Assign external requests using a pluggable elevator assignment strategy.
- Shut down gracefully after queued requests are processed.

## High Level Design

```text
Client / Main
    |
    v
ElevatorController
    |
    |-- assigns external requests using ElevatorAssigner
    |-- routes internal requests to a specific Elevator
    |
    v
Elevator
    |
    |-- owns current floor, movement state, and pending stops
    |-- runs as a worker thread
    |-- picks next stop using ElevatorSchedulingStrategy
    |
    v
ElevatorState
    |
    |-- IdleState
    |-- MovingUpState
    |-- MovingDownState
```

## Main Classes

### `ElevatorController`

Acts as the entry point for requests.

- Stores elevators by id.
- Starts one thread per elevator.
- Handles internal requests by forwarding them to the target elevator.
- Handles external requests by asking an `ElevatorAssigner` to choose the best elevator.
- Coordinates shutdown and waits for elevator threads to finish.

### `Elevator`

Represents a single elevator car.

- Tracks `id`, `currentFloor`, `ElevatorState`, and pending pickup floors.
- Maintains two sorted stop sets:
  - `upPickups`: floors to serve while moving up.
  - `downPickups`: floors to serve while moving down.
- Uses `wait()` and `notifyAll()` to sleep when idle and wake when new requests arrive.
- Delegates next-floor selection to `ElevatorSchedulingStrategy`.

### Request Model

- `Request`: base request containing a floor number.
- `ExternalRequest`: hall call with source floor and requested direction.
- `InternalRequest`: destination floor selected inside an elevator.
- `Direction`: `UP` or `DOWN`.

### Elevator State Model

The state pattern is used to represent movement behavior.

- `IdleState`: elevator has no active direction.
- `MovingUpState`: elevator is currently moving upward.
- `MovingDownState`: elevator is currently moving downward.

Each state defines:

- Whether the elevator is idle.
- Current direction.
- How to serve a floor.
- What state to move to after serving a floor.

## Scheduling Strategy

`ElevatorSchedulingStrategy` defines how an elevator chooses its next stop from its local queues.

Current implementation:

### `LookSchedulingStrategy`

Implements a LOOK-style elevator algorithm:

- If moving up, continue serving upward stops first.
- If no upward stop remains, reverse toward the nearest pending lower stop.
- If moving down, continue serving downward stops first.
- If no downward stop remains, reverse toward the nearest pending higher stop.
- If idle, choose the closest pending stop.

This avoids scanning all floors like SCAN would and only moves as far as the farthest pending request in the current direction.

## Elevator Assignment Strategy

`ElevatorAssigner` defines how an external request is assigned to one elevator.

Current implementations:

### `NearestElevatorAssigner`

Chooses the elevator with the smallest absolute distance from the request floor.

### `NearestDirectionElevatorAssigner`

Chooses based on distance, direction, and whether the request is on the elevator's current path.

Cost rules:

- Idle elevator: distance only.
- Moving in the same direction and request is on the way: distance only.
- Moving in the same direction but request is not on the way: distance plus penalty.
- Moving in the opposite direction: distance plus a larger penalty.

This better approximates real elevator dispatching than nearest-distance alone.

## Request Flow

### External Request

```text
ExternalRequest(floor, direction)
    -> ElevatorController.handleExternalRequest()
    -> ElevatorAssigner.assign()
    -> Elevator.assignExternalRequest()
    -> request added to upPickups/downPickups
    -> elevator thread wakes up
    -> scheduling strategy picks next floor
```

### Internal Request

```text
InternalRequest(destination)
    -> ElevatorController.handleInternalRequest(elevatorId, request)
    -> Elevator.assignInternalRequest()
    -> destination added based on current floor
    -> elevator thread wakes up
    -> scheduling strategy picks next floor
```

## Concurrency Model

- Each `Elevator` implements `Runnable`.
- `ElevatorController` starts one thread per elevator.
- Elevator methods that read or mutate request queues are synchronized.
- Idle elevators wait until a request is assigned.
- Request assignment calls `notifyAll()` to wake the elevator worker.
- `shutdown()` signals workers to exit once no pending work remains.

## Design Patterns Used

- Strategy pattern:
  - `ElevatorSchedulingStrategy`
  - `ElevatorAssigner`
- State pattern:
  - `ElevatorState`
  - `IdleState`
  - `MovingUpState`
  - `MovingDownState`
- Controller pattern:
  - `ElevatorController` coordinates request routing and worker lifecycle.

## How to Run

From the project root:

```bash
javac -d out $(find src -name "*.java")
java -cp out Main
```

`Main` creates two elevators, sends a burst of external and internal requests, then shuts the system down after processing.

## Interview Discussion Points

- Why keep elevator assignment separate from per-elevator scheduling?
- How would the design change for destination control elevators?
- How should capacity, door state, emergency mode, maintenance mode, or floor bounds be modeled?
- Should internal requests include passenger direction or only destination?
- How would fairness and starvation prevention be added?
- How would the system persist metrics such as wait time, travel time, and utilization?
- How would this change in a distributed system where controllers and elevators communicate over a network?

## Possible Extensions

- Add floor bounds validation.
- Add elevator capacity and passenger count.
- Add door states such as opening, open, closing, and closed.
- Add emergency stop and maintenance states.
- Add request timestamps and metrics.
- Add unit tests for assignment and scheduling strategies.
- Add a richer simulation runner for randomized traffic.
