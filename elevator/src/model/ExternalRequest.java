package model;

public class ExternalRequest extends Request {
    Direction direction;
    public ExternalRequest(int from, Direction direction) {
        super(from);
        this.direction = direction;
    }
    public Direction getDirection() {
        return direction;
    }
}
