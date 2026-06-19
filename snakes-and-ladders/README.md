# Snakes and Ladders — Low Level Design

A console-based Snakes and Ladders implementation designed for interview-level OOD practice.

---

## Problem Statement

Design a turn-based Snakes and Ladders game where multiple players move across a 100-cell board by rolling dice. Landing on a snake's head slides the player backward to its tail; landing on a ladder's bottom climbs the player forward to its top. The first player to land exactly on cell 100 wins.

---

## Requirements

### Functional
- Support 2 or more players, each identified by name
- Dice are configurable: number of dice and sides per die set at game creation
- Board is configurable: snakes and ladders are provided at game setup, not hardcoded
- Snake: landing on a head position moves the player down to the tail position
- Ladder: landing on a bottom position moves the player up to the top position
- Win condition: a player must land **exactly** on cell 100 to win; overshooting leaves their position unchanged
- Players take turns in order; the game ends as soon as a winner is declared

### Non-Functional
- No persistence, no UI — runs as a console simulation
- Extensible: new board sizes, custom dice, or additional jump types can be added without rewriting core logic
- Clean OOP structure suitable for a 30–45 minute interview walkthrough

---

## Core Entities

| Class | Package | Responsibility |
|---|---|---|
| `Player` | `model` | Stores player `id`, `name`, and `currentPosition` (starts at 0) |
| `Dice` | `model` | Configurable `numberOfDice` and `sidesPerDie`; `roll()` returns the sum of all dice |
| `Snake` | `model` | Value object pairing `head` (start) → `tail` (destination) |
| `Ladder` | `model` | Value object pairing `bottom` (start) → `top` (destination) |
| `Board` | `model` | Owns the unified `jumps` map and board `size`; exposes `getDestination(pos)` |
| `GameStatus` | `model` | Enum: `IN_PROGRESS`, `FINISHED` |
| `GameEngine` | root | Orchestrates turns: roll dice → move player → apply jump → check win |
| `Main` | root | Wires up a sample game and runs the simulation |

---

## Design Decisions

### Unified Jumps Map on Board
`Board` merges snakes and ladders into a single `Map<Integer, Integer> jumps` during construction:

```
Snake  head=62 → tail=19  :  jumps.put(62, 19)
Ladder bottom=14 → top=55 :  jumps.put(14, 55)
```

`getDestination(int pos)` returns `jumps.getOrDefault(pos, pos)` — one O(1) lookup with no branching on entity type. This also makes adding new jump types (e.g., portals, teleporters) trivial.

### Configurable Dice
`Dice(int numberOfDice, int sidesPerDie)` allows the caller to use any combination (1d6, 2d6, 1d12, etc.) without subclassing. `roll()` sums `n` random values in `[1, sidesPerDie]`.

### Win Condition — Exact Landing
Before moving a player, `GameEngine` checks whether `currentPosition + roll > boardSize`. If so, the player stays in place and the turn passes. This is enforced in `GameEngine`, not in `Board`, keeping board logic stateless.

---

## Class Relationships

```
Main
 └── creates GameEngine
        ├── has Board
        │     └── builds Map<Integer,Integer> jumps
        │           (from List<Snake> + List<Ladder>)
        ├── has Dice
        └── has List<Player>
```

---

## Package Structure

```
snakes-and-ladders/src/
├── model/
│   ├── Player.java          # id, name, currentPosition
│   ├── Dice.java            # numberOfDice, sidesPerDie, roll()
│   ├── Snake.java           # head, tail
│   ├── Ladder.java          # bottom, top
│   ├── Board.java           # size, jumps map, getDestination()
│   └── GameStatus.java      # IN_PROGRESS, FINISHED
├── GameEngine.java          # turn loop, win detection
└── Main.java                # wires up and runs a sample game
```

---

## Game Flow

```
1. Main creates players, dice, snakes, ladders → passes to GameEngine
2. GameEngine.startGame():
   while no winner:
     a. Current player rolls Dice.roll()
     b. newPos = currentPos + roll
     c. if newPos > 100 → skip (overshoot)
     d. newPos = Board.getDestination(newPos)  // apply snake or ladder if any
     e. player.setCurrentPosition(newPos)
     f. if newPos == 100 → set winner, GameStatus = FINISHED
     g. advance to next player
3. Print winner
```
