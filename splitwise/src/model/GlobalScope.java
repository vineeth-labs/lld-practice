package model;

// the single bucket for expenses that don't belong to any group
public final class GlobalScope implements Scope {

    public static final GlobalScope INSTANCE = new GlobalScope();

    private GlobalScope() {}

    @Override
    public String id() {
        return "global";
    }
}
