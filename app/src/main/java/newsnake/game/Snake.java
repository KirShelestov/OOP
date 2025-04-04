package newsnake.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Snake {
    private final LinkedList<Point> body = new LinkedList<>();
    private Direction direction = Direction.RIGHT;

    public Snake(Point startPosition) {
        body.add(startPosition);
    }

    public void move(Point newHead) {
        body.addFirst(newHead);
        body.removeLast();
    }

    public void grow(Point nextHead) {
        body.addFirst(nextHead);
    }

    public Point getHead() {
        return body.getFirst();
    }

    public Point getTail() {
        return body.getLast();
    }

    public List<Point> getBody() {
        return List.copyOf(body);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction newDirection) {
        // Only allow changing to non-opposite directions
        if (!newDirection.isOpposite(direction)) {
            direction = newDirection;
        }
    }

    public void teleportTo(Point newPosition) {
        body.addFirst(newPosition);
        body.removeLast();
    }

    public void reverse() {
        Collections.reverse(body);
        direction = direction.getOpposite();
    }

    private boolean isOpposite(Direction dir1, Direction dir2) {
        return (dir1 == Direction.UP && dir2 == Direction.DOWN) ||
               (dir1 == Direction.DOWN && dir2 == Direction.UP) ||
               (dir1 == Direction.LEFT && dir2 == Direction.RIGHT) ||
               (dir1 == Direction.RIGHT && dir2 == Direction.LEFT);
    }
}