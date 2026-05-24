package model;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private final List<List<Piece>> grid;

    public Board(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Board size must be at least 1");
        }
        this.size = size;
        this.grid = new ArrayList<>();
        reset();
    }

    public int getSize() {
        return size;
    }

    public void reset() {
        grid.clear();
        for (int i = 0; i < size; i++) {
            List<Piece> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(Piece.Empty);
            }
            grid.add(row);
        }
    }

    public Piece getPiece(int row, int col) {
        validateCoordinates(row, col);
        return grid.get(row).get(col);
    }

    public void placePiece(int row, int col, Piece piece) {
        validateCoordinates(row, col);
        if (grid.get(row).get(col) != Piece.Empty) {
            throw new IllegalArgumentException("Cell already occupied");
        }
        grid.get(row).set(col, piece);
    }

    public boolean isCellEmpty(int row, int col) {
        validateCoordinates(row, col);
        return grid.get(row).get(col) == Piece.Empty;
    }

    public boolean checkWinner(int row, int col, Piece piece) {
        validateCoordinates(row, col);

        boolean rowWin = true;
        for (int i = 0; i < size; i++) {
            if (grid.get(row).get(i) != piece) {
                rowWin = false;
                break;
            }
        }
        if (rowWin) {
            return true;
        }

        boolean colWin = true;
        for (int i = 0; i < size; i++) {
            if (grid.get(i).get(col) != piece) {
                colWin = false;
                break;
            }
        }
        if (colWin) {
            return true;
        }

        if (row == col) {
            boolean diagWin = true;
            for (int i = 0; i < size; i++) {
                if (grid.get(i).get(i) != piece) {
                    diagWin = false;
                    break;
                }
            }
            if (diagWin) {
                return true;
            }
        }

        if (row + col == size - 1) {
            boolean antiDiagWin = true;
            for (int i = 0; i < size; i++) {
                if (grid.get(i).get(size - 1 - i) != piece) {
                    antiDiagWin = false;
                    break;
                }
            }
            if (antiDiagWin) {
                return true;
            }
        }

        return false;
    }

    private void validateCoordinates(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Coordinates are out of bounds");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (List<Piece> row : grid) {
            for (Piece piece : row) {
                builder.append(piece).append(" | ");
            }
            builder.setLength(builder.length() - 3);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}