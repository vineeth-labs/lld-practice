package creational.abstractfactory;

public class MacCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("Rendering a macOS-style checkbox.");
    }
}
