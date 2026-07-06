package structural.facade;

public class StreamingPlayer {
    public void turnOn() {
        System.out.println("Streaming player is on");
    }

    public void play(String movieName) {
        System.out.println("Playing movie: " + movieName);
    }

    public void stop() {
        System.out.println("Streaming player stopped");
    }

    public void turnOff() {
        System.out.println("Streaming player is off");
    }
}
