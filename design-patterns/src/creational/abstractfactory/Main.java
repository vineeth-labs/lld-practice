package creational.abstractfactory;

public class Main {
    public static void main(String[] args) {
        UiComponentFactory windowsFactory = new WindowsUiComponentFactory();
        Application windowsApplication = new Application(windowsFactory);
        windowsApplication.renderUi();

        UiComponentFactory macFactory = new MacUiComponentFactory();
        Application macApplication = new Application(macFactory);
        macApplication.renderUi();
    }
}
