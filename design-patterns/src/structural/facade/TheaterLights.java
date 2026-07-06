package structural.facade;

public class TheaterLights {
    public void dim(int level) {
        System.out.println("Theater lights dimmed to " + level + "%");
    }

    public void turnOn() {
        System.out.println("Theater lights are on");
    }
}
