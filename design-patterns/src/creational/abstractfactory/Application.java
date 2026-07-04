package creational.abstractfactory;

public class Application {
    private final Button button;
    private final Checkbox checkbox;

    public Application(UiComponentFactory uiComponentFactory) {
        this.button = uiComponentFactory.createButton();
        this.checkbox = uiComponentFactory.createCheckbox();
    }

    public void renderUi() {
        button.render();
        checkbox.render();
    }
}
