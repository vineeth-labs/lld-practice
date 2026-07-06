package structural.facade;

public class HomeTheaterFacade {
    private final Projector projector;
    private final SoundSystem soundSystem;
    private final StreamingPlayer streamingPlayer;
    private final TheaterLights theaterLights;

    public HomeTheaterFacade(Projector projector, SoundSystem soundSystem,
                             StreamingPlayer streamingPlayer, TheaterLights theaterLights) {
        this.projector = projector;
        this.soundSystem = soundSystem;
        this.streamingPlayer = streamingPlayer;
        this.theaterLights = theaterLights;
    }

    public void watchMovie(String movieName) {
        theaterLights.dim(20);
        projector.turnOn();
        projector.setInput("Streaming Player");
        soundSystem.turnOn();
        soundSystem.setVolume(8);
        streamingPlayer.turnOn();
        streamingPlayer.play(movieName);
    }

    public void endMovie() {
        streamingPlayer.stop();
        streamingPlayer.turnOff();
        soundSystem.turnOff();
        projector.turnOff();
        theaterLights.turnOn();
    }
}
