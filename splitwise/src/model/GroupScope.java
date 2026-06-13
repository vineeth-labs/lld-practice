package model;

public record GroupScope(Group group) implements Scope {

    @Override
    public String id() {
        return group.getId();
    }
}
