package model;
public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("Player 1", Piece.X);
        Player player2 = new Player("Player 2", Piece.O);
        TicTacToeGame game = new TicTacToeGame(3, player1, player2);
        game.makeMove(0, 0);
        game.makeMove(1, 1);
        game.makeMove(0, 1);
        game.makeMove(2, 2);
        game.makeMove(0, 2); // This move should win the game for Player X
        game.makeMove(1, 0); // This move should not be allowed as the game is already over
        game.resetGame(); // Reset the game to play again
        game.makeMove(1, 0); // This move should be allowed as the game has been reset
        game.makeMove(2, 0);
        game.makeMove(1, 1);
        game.makeMove(2, 1);
        game.makeMove(1, 2); // This move should win the game for Player
    }
}
