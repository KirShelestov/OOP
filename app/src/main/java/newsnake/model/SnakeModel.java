package newsnake.model;

import newsnake.game.*;
import java.util.*;
import java.util.stream.Collectors;

public class SnakeModel {
    private final Snake snake;
    private Snake mirrorSnake;
    private final List<Point> foods = new ArrayList<>();
    private final List<Point> obstacles = new ArrayList<>();
    private int score = 0;
    private boolean gameOver = false;
    private final Random random = new Random();
    private final LevelType levelType;
    private int initialFoodCount;
    private static final int GRID_SIZE = 20;
    private static final int OBSTACLE_COUNT = 5;

    public SnakeModel(LevelType levelType) {
        this.levelType = levelType;
        this.snake = new Snake(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        
        if (levelType.getGameMode() == GameMode.MIRROR) {
            initializeMirrorSnake();
        }
        
        if (levelType.getGameMode() == GameMode.TELEPORT && levelType.getAppleCount() < 2) {
            this.initialFoodCount = 2;
        } else {
            this.initialFoodCount = levelType.getAppleCount() == -1 ? 
                random.nextInt(1, 8) : levelType.getAppleCount();
        }
        
        if (levelType.hasObstacles()) {
            initializeObstacles();
        }
        initializeFood();
    }

    private void initializeFood() {
        for (int i = 0; i < initialFoodCount; i++) {
            Point food;
            do {
                food = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
            } while (snake.getBody().contains(food) || 
                    foods.contains(food) ||
                    obstacles.contains(food));
            foods.add(food);
        }
    }

    private void initializeObstacles() {
        for (int i = 0; i < OBSTACLE_COUNT; i++) {
            Point obstacle;
            do {
                obstacle = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
            } while (snake.getBody().contains(obstacle) || 
                    obstacles.contains(obstacle) ||
                    isNearSnakeStart(obstacle));
            obstacles.add(obstacle);
        }
    }

    private boolean isNearSnakeStart(Point point) {
        Point start = snake.getHead();
        return Math.abs(point.x() - start.x()) <= 1 && 
               Math.abs(point.y() - start.y()) <= 1;
    }

    public void update() {
        if (!gameOver) {
            Point nextHead = snake.getNextPosition();
            if (isCollision(nextHead)) {
                gameOver = true;
                return;
            }
            snake.move(nextHead);

            // Handle mirror snake movement
            if (mirrorSnake != null) {
                Point mirrorNextHead = mirrorSnake.getNextPosition();
                if (!isCollision(mirrorNextHead)) {
                    mirrorSnake.move(mirrorNextHead);
                    if (foods.contains(mirrorNextHead)) {
                        snake.grow(nextHead);
                        mirrorSnake.grow(mirrorNextHead);
                        foods.remove(mirrorNextHead);
                        score += 10;
                    }
                } else {
                    gameOver = true;
                    return;
                }
            }

            // Handle food collisions for main snake
            if (foods.contains(nextHead)) {
                if (mirrorSnake != null) {
                    Point mirrorNextHead = mirrorSnake.getNextPosition();
                    mirrorSnake.grow(mirrorNextHead);
                }
                handleFoodCollision(nextHead);
            }

            if (levelType.getGameMode() == GameMode.TELEPORT) {
                handleTeleport();
            }
        }
    }

    private void handleTeleport() {
        Point head = snake.getHead();
        if (head.x() < 0 || head.x() >= GRID_SIZE || head.y() < 0 || head.y() >= GRID_SIZE) {
            // Remove apple at the starting position (if exists)
            Point oldPosition = snake.getTail();
            foods.remove(oldPosition);
            
            // Calculate new position and remove apple there (if exists)
            Point newHead = new Point(
                Math.floorMod(head.x(), GRID_SIZE),
                Math.floorMod(head.y(), GRID_SIZE)
            );
            foods.remove(newHead);
            
            // Teleport snake to new position
            snake.teleportTo(newHead);
            
            // Add new apples if needed
            if (foods.isEmpty()) {
                initialFoodCount = 2; // Always maintain at least 2 apples in teleport mode
                initializeFood();
            }
        }
    }

    private void handleFoodCollision(Point nextHead) {
        if (levelType.getGameMode() == GameMode.REVERSE) {
            Point currentTail = snake.getBody().get(snake.getBody().size() - 1);
            snake.reverse();
            snake.grow(currentTail);
            foods.remove(nextHead);
            score += 15;
        } else if (levelType.getGameMode() == GameMode.TELEPORT && foods.size() > 1) {
            Point currentFood = nextHead;
            List<Point> foodList = new ArrayList<>(foods);
            int currentIndex = foodList.indexOf(currentFood);
            int nextIndex = (currentIndex + 1) % foodList.size();
            Point nextFood = foodList.get(nextIndex);
            
            // Remove both apples - current and destination
            foods.remove(currentFood);
            foods.remove(nextFood);
            
            // Grow snake and teleport
            snake.grow(nextHead);
            snake.teleportTo(nextFood);
            
            // Double score for eating two apples
            score += 20;
            
            // Initialize new food immediately if needed
            if (foods.isEmpty()) {
                initialFoodCount = 2; // Always maintain at least 2 apples in teleport mode
                initializeFood();
            }
        } else {
            snake.grow(nextHead);
            foods.remove(nextHead);
            score += 10;
        }
        
        if (foods.isEmpty()) {
            initialFoodCount = levelType.getAppleCount() == -1 ? 
                random.nextInt(1, 8) : levelType.getAppleCount();
            if (levelType.getGameMode() == GameMode.TELEPORT && initialFoodCount < 2) {
                initialFoodCount = 2;
            }
            initializeFood();
        }
    }

    public boolean isCollision(Point point) {
        return point.x() < 0 || point.x() >= GRID_SIZE ||
               point.y() < 0 || point.y() >= GRID_SIZE ||
               snake.getBody().contains(point) ||
               obstacles.contains(point);
    }

    private void initializeMirrorSnake() {
        // Create mirror snake at opposite side of the grid
        Point mirrorStart = new Point(GRID_SIZE - snake.getHead().x() - 1, 
                                    GRID_SIZE - snake.getHead().y() - 1);
        // Create mirror snake with initial opposite direction
        mirrorSnake = new Snake(mirrorStart, Direction.LEFT); // противоположное Direction.RIGHT
    }

    // Getters
    public Snake getSnake() { return snake; }
    public Snake getMirrorSnake() { return mirrorSnake; }
    public List<Point> getFoods() {
        return List.copyOf(foods);
    }
    public List<Point> getObstacles() { return List.copyOf(obstacles); }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public LevelType getLevelType() { return levelType; }
}