import model.Board;
import model.Dice;
import model.GameStatus;
import model.Player;

import java.util.List;

public class GameEngine {
    private final Board board;
    private final Dice dice;
    private final List<Player> players;
    private GameStatus status;
    private Player winner;
    private int currentPlayerIndex;

    public GameEngine(Board board, Dice dice, List<Player> players) {
        this.board = board;
        this.dice = dice;
        this.players = players;
        this.status = GameStatus.IN_PROGRESS;
        this.currentPlayerIndex = 0;
    }

    public void startGame() {
        System.out.println("Game started with " + players.size() + " players!");
        while (status == GameStatus.IN_PROGRESS) {
            playTurn();
        }
        System.out.println("Winner: " + winner.getName() + " !");
    }

    private void playTurn() {
        Player player = players.get(currentPlayerIndex);
        int roll = dice.roll();
        int newPos = player.getCurrentPosition() + roll;

        System.out.print(player.getName() + " rolled " + roll);

        if (newPos > board.getSize()) {
            System.out.println(" → overshot (stays at " + player.getCurrentPosition() + ")");
        } else {
            int destination = board.getDestination(newPos);
            if (destination < newPos) {
                System.out.print(" → " + newPos + " [SNAKE] → " + destination);
            } else if (destination > newPos) {
                System.out.print(" → " + newPos + " [LADDER] → " + destination);
            } else {
                System.out.print(" → " + destination);
            }
            player.setCurrentPosition(destination);
            System.out.println();

            if (destination == board.getSize()) {
                status = GameStatus.FINISHED;
                winner = player;
            }
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getWinner() { return winner; }
    public GameStatus getStatus() { return status; }
}
