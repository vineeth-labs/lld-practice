package structural.facade;

public class Main {
    public static void main(String[] args) {
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
                new Projector(),
                new SoundSystem(),
                new StreamingPlayer(),
                new TheaterLights()
        );

        homeTheater.watchMovie("Inception");
        homeTheater.endMovie();
    }
}
