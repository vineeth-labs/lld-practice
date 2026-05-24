package model;

import java.util.List;

public class TicTacToeGame {
    private final Board board;
    private final List<Player> players;
    private int currentPlayerIndex;
    private boolean gameOver;

    public TicTacToeGame(int boardSize, Player... players) {
        if (players.length < 2) {
            throw new IllegalArgumentException("At least two players are required");
        }
        this.board = new Board(boardSize);
        this.players = List.of(players);
        this.currentPlayerIndex = 0;
        this.gameOver = false;
    }

    public void resetGame() {
        board.reset();
        currentPlayerIndex = 0;
        gameOver = false;
    }

    public void makeMove(int row, int col) {
        if (gameOver) {
            System.out.println("Game is already over. Please start a new game.");
            return;
        }

        Player currentPlayer = getCurrentPlayer();
        try {
            board.placePiece(row, col, currentPlayer.getPieceChosen());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Player: " + currentPlayer.getName() + " places " + currentPlayer.getPieceChosen() + " at (" + row + ", " + col + ")");
        System.out.println(board);

        if (board.checkWinner(row, col, currentPlayer.getPieceChosen())) {
            System.out.println("Player: " + currentPlayer.getName() + " wins!");
            gameOver = true;
            return;
        }

        advancePlayer();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void advancePlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}
