import model.Board;
import model.Dice;
import model.Ladder;
import model.Player;
import model.Snake;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Snake> snakes = Arrays.asList(
            new Snake(99, 54),
            new Snake(70, 55),
            new Snake(62, 19),
            new Snake(46, 6),
            new Snake(17, 3)
        );

        List<Ladder> ladders = Arrays.asList(
            new Ladder(2, 38),
            new Ladder(7, 14),
            new Ladder(32, 52),
            new Ladder(42, 68),
            new Ladder(58, 77),
            new Ladder(65, 85),
            new Ladder(88, 96)
        );

        Board board = new Board(100, snakes, ladders);
        Dice dice = new Dice(1, 6);

        List<Player> players = Arrays.asList(
            new Player("p1", "Alice"),
            new Player("p2", "Bob")
        );

        GameEngine game = new GameEngine(board, dice, players);
        game.startGame();
    }
}
