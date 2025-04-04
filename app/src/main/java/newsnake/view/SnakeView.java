package newsnake.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import newsnake.game.*;
import newsnake.model.SnakeModel;
import java.util.List;

public class SnakeView extends Canvas {
    private static final int CELL_SIZE = 20;
    private static final int SCORE_HEIGHT = 50;
    private static final int SCORE_PADDING = 10;
    private static final int GRID_SIZE = 20;
    private static final int GRID_OFFSET_Y = SCORE_HEIGHT;
    private int gridOffsetX;
    private final SnakeModel model;
    private final GraphicsContext gc;
    
    private final Image headUp = new Image(getClass().getResourceAsStream("/images/head_up.png"));
    private final Image headDown = new Image(getClass().getResourceAsStream("/images/head_down.png"));
    private final Image headLeft = new Image(getClass().getResourceAsStream("/images/head_left.png"));
    private final Image headRight = new Image(getClass().getResourceAsStream("/images/head_right.png"));
    private final Image headUpDead = new Image(getClass().getResourceAsStream("/images/head_up_dead.png"));
    private final Image headDownDead = new Image(getClass().getResourceAsStream("/images/head_down_dead.png"));
    private final Image headLeftDead = new Image(getClass().getResourceAsStream("/images/head_left_dead.png"));
    private final Image headRightDead = new Image(getClass().getResourceAsStream("/images/head_right_dead.png"));
    private final Image bodyHorizontal = new Image(getClass().getResourceAsStream("/images/body_horizontal.png"));
    private final Image bodyVertical = new Image(getClass().getResourceAsStream("/images/body_vertical.png"));
    private final Image bodyTopLeft = new Image(getClass().getResourceAsStream("/images/body_tl.png"));
    private final Image bodyTopRight = new Image(getClass().getResourceAsStream("/images/body_tr.png"));
    private final Image bodyBottomLeft = new Image(getClass().getResourceAsStream("/images/body_bl.png"));
    private final Image bodyBottomRight = new Image(getClass().getResourceAsStream("/images/body_br.png"));
    private final Image tailUp = new Image(getClass().getResourceAsStream("/images/tail_up.png"));
    private final Image tailDown = new Image(getClass().getResourceAsStream("/images/tail_down.png"));
    private final Image tailLeft = new Image(getClass().getResourceAsStream("/images/tail_left.png"));
    private final Image tailRight = new Image(getClass().getResourceAsStream("/images/tail_right.png"));
    private final Image background = new Image(getClass().getResourceAsStream("/images/background.jpg"));
    private final Image apple = new Image(getClass().getResourceAsStream("/images/apple.png"));
    private final Image scoreIcon = new Image(getClass().getResourceAsStream("/images/superapple.png"));
    private final Image obstacle = new Image(getClass().getResourceAsStream("/images/obstacle.png"));

    public SnakeView(SnakeModel model, int gridSize) {
        super(gridSize * CELL_SIZE, gridSize * CELL_SIZE + SCORE_HEIGHT);
        this.model = model;
        this.gc = getGraphicsContext2D();
        
        double gridCellSize = background.getWidth() / gridSize;
        this.gridOffsetX = (int)((gridCellSize - CELL_SIZE) / 2);
    }

    public void render() {
        gc.clearRect(0, 0, getWidth(), getHeight());
        
        gc.drawImage(background, 0, SCORE_HEIGHT, getWidth(), getHeight() - SCORE_HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), SCORE_HEIGHT);
        
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial Bold", 20));
        gc.drawImage(scoreIcon, SCORE_PADDING, (SCORE_HEIGHT - 20) / 2, 20, 20);
        gc.fillText(String.valueOf(model.getScore()), SCORE_PADDING + 30, SCORE_HEIGHT / 2 + 8);
        
        drawSnake(model.getSnake(), gc);
        if (model.getLevelType().getGameMode() == GameMode.MIRROR && model.getMirrorSnake() != null) {
            drawSnake(model.getMirrorSnake(), gc);
        }
        
        for (Point food : model.getFoods()) {
            gc.drawImage(apple, getGridX(food.x()), getGridY(food.y()), CELL_SIZE, CELL_SIZE);
        }
        
        if (model.getLevelType().getGameMode() == GameMode.OBSTACLES) {
            for (Point obstaclePoint : model.getObstacles()) {
                gc.drawImage(obstacle, getGridX(obstaclePoint.x()), getGridY(obstaclePoint.y()), CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawSnake(Snake snake, GraphicsContext gc) {
        List<Point> body = snake.getBody();
        if (body.size() > 0) {
            Point head = snake.getHead();
            drawHead(head, snake.getDirection());
            
            for (int i = 1; i < body.size() - 1; i++) {
                Point prev = body.get(i - 1);
                Point current = body.get(i);
                Point next = body.get(i + 1);
                drawBodySegment(prev, current, next);
            }
            
            if (body.size() > 1) {
                Point tail = body.get(body.size() - 1);
                Point beforeTail = body.get(body.size() - 2);
                drawTail(tail, beforeTail);
            }
        }
    }

    private double getGridX(int x) { return gridOffsetX + x * GRID_SIZE; }
    private double getGridY(int y) { return GRID_OFFSET_Y + y * GRID_SIZE; }

    private void drawHead(Point head, Direction direction) {
        Image headImage = switch (direction) {
            case UP -> model.isGameOver() ? headUpDead : headUp;
            case DOWN -> model.isGameOver() ? headDownDead : headDown;
            case LEFT -> model.isGameOver() ? headLeftDead : headLeft;
            case RIGHT -> model.isGameOver() ? headRightDead : headRight;
        };
        gc.drawImage(headImage, getGridX(head.x()), getGridY(head.y()), CELL_SIZE, CELL_SIZE);
    }

    private void drawBodySegment(Point prev, Point current, Point next) {
        Image bodyImage = (prev.x() == next.x()) ? bodyVertical :
                         (prev.y() == next.y()) ? bodyHorizontal :
                         getCornerImage(prev, current, next);
        gc.drawImage(bodyImage, getGridX(current.x()), getGridY(current.y()), CELL_SIZE, CELL_SIZE);
    }

    private Image getCornerImage(Point prev, Point current, Point next) {
        if ((prev.x() < current.x() && next.y() < current.y()) || 
            (prev.y() < current.y() && next.x() < current.x())) return bodyTopLeft;
        if ((prev.x() > current.x() && next.y() < current.y()) || 
            (prev.y() < current.y() && next.x() > current.x())) return bodyTopRight;
        if ((prev.x() < current.x() && next.y() > current.y()) || 
            (prev.y() > current.y() && next.x() < current.x())) return bodyBottomLeft;
        return bodyBottomRight;
    }

    private void drawTail(Point tail, Point beforeTail) {
        Image tailImage = (beforeTail.x() < tail.x()) ? tailRight :
                         (beforeTail.x() > tail.x()) ? tailLeft :
                         (beforeTail.y() < tail.y()) ? tailDown : tailUp;
        gc.drawImage(tailImage, getGridX(tail.x()), getGridY(tail.y()), CELL_SIZE, CELL_SIZE);
    }

    public SnakeModel getModel() {
        return model;
    }
}