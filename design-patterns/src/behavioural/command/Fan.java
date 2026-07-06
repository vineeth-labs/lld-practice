package behavioural.command;

public class Fan {
    private final String room;

    public Fan(String room) {
        this.room = room;
    }

    public void turnOn() {
        System.out.println(room + " fan is on");
    }

    public void turnOff() {
        System.out.println(room + " fan is off");
    }
}
