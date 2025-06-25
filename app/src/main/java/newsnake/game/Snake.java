package newsnake.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Snake {
    private final LinkedList<Point> body = new LinkedList<>();
    private Direction direction;

    public Snake(Point startPosition, Direction initialDirection) {
        body.add(startPosition);
        this.direction = initialDirection;
    }

    // Для обратной совместимости
    public Snake(Point startPosition) {
        this(startPosition, Direction.RIGHT);
    }

    public Point getNextPosition() {
        Point head = getHead();
        return switch (direction) {
            case UP -> new Point(head.x(), head.y() - 1);
            case DOWN -> new Point(head.x(), head.y() + 1);
            case LEFT -> new Point(head.x() - 1, head.y());
            case RIGHT -> new Point(head.x() + 1, head.y());
        };
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
        // Get the current head position
        Point head = getHead();
        Point nextPos = switch (newDirection) {
            case UP -> new Point(head.x(), head.y() - 1);
            case DOWN -> new Point(head.x(), head.y() + 1);
            case LEFT -> new Point(head.x() - 1, head.y());
            case RIGHT -> new Point(head.x() + 1, head.y());
        };
        
        // Check if the new direction would cause immediate collision with the snake's body
        List<Point> bodyWithoutTail = new ArrayList<>(body);
        bodyWithoutTail.remove(bodyWithoutTail.size() - 1);
        
        if (!bodyWithoutTail.contains(nextPos) && !newDirection.isOpposite(direction)) {
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