package structural.facade;

public class SoundSystem {
    public void turnOn() {
        System.out.println("Sound system is on");
    }

    public void setVolume(int volume) {
        System.out.println("Sound system volume set to " + volume);
    }

    public void turnOff() {
        System.out.println("Sound system is off");
    }
}
