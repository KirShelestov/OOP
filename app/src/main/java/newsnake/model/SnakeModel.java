package newsnake.model;

import newsnake.game.*;
import java.util.*;
import java.util.stream.Collectors;

public class SnakeModel {
    private final Snake snake;
    private Snake mirrorSnake;
    private final List<Point> foods = new ArrayList<>();
    private final List<MovingFood> movingFoods = new ArrayList<>();
    private final List<Point> obstacles = new ArrayList<>();
    private int score = 0;
    private boolean gameOver = false;
    private final Random random = new Random();
    private final LevelType levelType;
    private int initialFoodCount;
    private static final int GRID_SIZE = 20;
    private static final int OBSTACLE_COUNT = 5;
    private static final int FLYING_INTERVAL = 5;
    private int flyingCounter = 0;

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
        if (levelType.getGameMode() == GameMode.FLYING) {
            spawnMovingFood();
        } else {
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

    private void spawnMovingFood() {
        while (true) {
            Point candidate = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
            if (snake.getBody().contains(candidate) || obstacles.contains(candidate)) continue;
            
            boolean foodCollision = movingFoods.stream()
                .anyMatch(food -> food.getPosition().equals(candidate));
            if (foodCollision) continue;
            
            Direction xDir = random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
            Direction yDir = random.nextBoolean() ? Direction.UP : Direction.DOWN;
            MovingFood food = new MovingFood(candidate, xDir, yDir);
            movingFoods.add(food);
            break;
        }
    }

    public void update() {
        if (gameOver) return;

        Point nextHead = snake.getHead().translate(snake.getDirection());

        // Handle teleport before collision check
        if (levelType.isTeleport()) {
            boolean willTeleport = false;
            Point oldHead = nextHead;

            if (nextHead.x() < 0) {
                nextHead = new Point(GRID_SIZE - 1, nextHead.y());
                willTeleport = true;
            } else if (nextHead.x() >= GRID_SIZE) {
                nextHead = new Point(0, nextHead.y());
                willTeleport = true;
            }
            if (nextHead.y() < 0) {
                nextHead = new Point(nextHead.x(), GRID_SIZE - 1);
                willTeleport = true;
            } else if (nextHead.y() >= GRID_SIZE) {
                nextHead = new Point(nextHead.x(), 0);
                willTeleport = true;
            }

            // If teleporting, check if destination is valid
            if (willTeleport && isCollision(nextHead)) {
                gameOver = true;
                return;
            }

            // If teleported successfully, add food at the old position
            if (willTeleport) {
                foods.add(snake.getTail());
            }
        } else if (isCollision(nextHead)) {
            // For non-teleport modes, any collision is game over
            gameOver = true;
            return;
        }

        // Move snake
        if (foods.contains(nextHead)) {
            handleFoodCollision(nextHead);
        } else {
            snake.move(nextHead);
        }

        if (levelType.getGameMode() == GameMode.FLYING) {
            flyingCounter++;
            if (flyingCounter >= FLYING_INTERVAL) {
                flyingCounter = 0;
                moveAllFruits();
            }
        }
    }

    private void handleTeleport() {
        Point head = snake.getHead();
        if (head.x() < 0 || head.x() >= GRID_SIZE || head.y() < 0 || head.y() >= GRID_SIZE) {
            // Create new apple at the previous position
            Point oldPosition = snake.getTail();
            foods.add(oldPosition);
            
            // Teleport to opposite side
            Point newHead = new Point(
                Math.floorMod(head.x(), GRID_SIZE),
                Math.floorMod(head.y(), GRID_SIZE)
            );
            snake.teleportTo(newHead);
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
            
            foods.remove(currentFood);
            snake.grow(nextHead);
            snake.teleportTo(nextFood);
            score += 20;
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

    private void moveAllFruits() {
        for (MovingFood food : movingFoods) {
            food.move();
            Point pos = food.getPosition();
            if (pos.x() <= 0 || pos.x() >= GRID_SIZE - 1) food.bounceX();
            if (pos.y() <= 0 || pos.y() >= GRID_SIZE - 1) food.bounceY();
        }
    }

    public boolean isCollision(Point point) {
        return point.x() < 0 || point.x() >= GRID_SIZE ||
               point.y() < 0 || point.y() >= GRID_SIZE ||
               snake.getBody().contains(point) ||
               obstacles.contains(point);
    }

    private void initializeMirrorSnake() {
        if (levelType.isMirror()) {
            Point mirrorStart = new Point(
                GRID_SIZE - snake.getHead().x(),
                GRID_SIZE - snake.getHead().y()
            );
            mirrorSnake = new Snake(mirrorStart);
            mirrorSnake.setDirection(snake.getDirection().getOpposite());
        }
    }

    // Getters
    public Snake getSnake() { return snake; }
    public Snake getMirrorSnake() { return mirrorSnake; }
    public List<Point> getFoods() {
        if (levelType.getGameMode() == GameMode.FLYING) {
            return movingFoods.stream()
                .map(MovingFood::getPosition)
                .collect(Collectors.toList());
        }
        return List.copyOf(foods);
    }
    public List<Point> getObstacles() { return List.copyOf(obstacles); }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public LevelType getLevelType() { return levelType; }
}