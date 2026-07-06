package behavioural.command;

public class Main {
    public static void main(String[] args) {
        RemoteControl remoteControl = new RemoteControl();

        Light livingRoomLight = new Light("Living room");
        Fan bedroomFan = new Fan("Bedroom");

        remoteControl.setCommand(new LightOnCommand(livingRoomLight));
        remoteControl.pressButton();

        remoteControl.setCommand(new FanOnCommand(bedroomFan));
        remoteControl.pressButton();

        remoteControl.setCommand(new LightOffCommand(livingRoomLight));
        remoteControl.pressButton();

        remoteControl.setCommand(new FanOffCommand(bedroomFan));
        remoteControl.pressButton();
    }
}
