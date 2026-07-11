package model;

import java.util.ArrayList;
import java.util.List;

public class Theatre {
    private String id;
    private String name;
    private List<Screen> screens;
    private Location location;

    public Theatre(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.screens = new ArrayList<>();
    }

    public Theatre(String id, String name, List<Screen> screens, Location location) {
        this.id = id;
        this.name = name;
        this.screens = screens;
        this.location = location;
    }

    public void addScreen(Screen screen) {
        this.screens.add(screen);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
