package model;

import java.util.Random;

public class Dice {
    private final int numberOfDice;
    private final int sidesPerDie;
    private final Random random;

    public Dice(int numberOfDice, int sidesPerDie) {
        this.numberOfDice = numberOfDice;
        this.sidesPerDie = sidesPerDie;
        this.random = new Random();
    }

    public int roll() {
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += random.nextInt(sidesPerDie) + 1;
        }
        return total;
    }
}
