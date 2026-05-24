package model;

public enum Piece {
    X("X"), O("O"), Empty("-");
    private final String symbol;

    Piece(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
