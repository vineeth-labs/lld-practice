package structural.composite;

import java.util.ArrayList;
import java.util.List;

public class MenuSection implements MenuComponent {
    private final String name;
    private final List<MenuComponent> menuComponents = new ArrayList<>();

    public MenuSection(String name) {
        this.name = name;
    }

    public void add(MenuComponent menuComponent) {
        menuComponents.add(menuComponent);
    }

    public void remove(MenuComponent menuComponent) {
        menuComponents.remove(menuComponent);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + name);

        for (MenuComponent menuComponent : menuComponents) {
            menuComponent.print(indent + "  ");
        }
    }
}
