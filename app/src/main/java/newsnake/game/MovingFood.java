package newsnake.game;

public class MovingFood {
    private Point position;
    private Direction xDirection;
    private Direction yDirection;

    public MovingFood(Point position, Direction xDirection, Direction yDirection) {
        this.position = position;
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public void move() {
        position = position.translate(xDirection).translate(yDirection);
    }

    public void bounceX() {
        xDirection = xDirection.getOpposite();
    }

    public void bounceY() {
        yDirection = yDirection.getOpposite();
    }

    public Point getPosition() {
        return position;
    }
}