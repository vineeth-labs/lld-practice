package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final int size;
    private final Map<Integer, Integer> jumps;

    public Board(int size, List<Snake> snakes, List<Ladder> ladders) {
        this.size = size;
        this.jumps = new HashMap<>();
        for (Snake snake : snakes) {
            jumps.put(snake.getHead(), snake.getTail());
        }
        for (Ladder ladder : ladders) {
            jumps.put(ladder.getBottom(), ladder.getTop());
        }
    }

    public int getSize() { return size; }

    // Returns the destination after applying any snake or ladder at pos, or pos itself if none.
    public int getDestination(int pos) {
        return jumps.getOrDefault(pos, pos);
    }
}
