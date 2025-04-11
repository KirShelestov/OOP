package newsnake.controller;

import newsnake.game.*;
import newsnake.model.SnakeModel;
import newsnake.view.SnakeView;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public class SnakeController {
    private final SnakeModel model;
    private final SnakeView view;
    private final AnimationTimer gameLoop;
    private long lastDirectionChange = 0;
    private static final long DIRECTION_CHANGE_DELAY = 50_000_000; // 50ms delay

    public SnakeController(LevelType levelType) {
        this.model = new SnakeModel(levelType);
        this.view = new SnakeView(model, 20);
        
        this.gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            private static final long UPDATE_INTERVAL = 200_000_000; // 200ms

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= UPDATE_INTERVAL) {
                    model.update();
                    view.render();
                    lastUpdate = now;
                    if (model.isGameOver()) {
                        stop();
                    }
                }
            }
        };
    }

    public void startGame() {
        setupInputHandling();
        gameLoop.start();
    }

    private void setupInputHandling() {
        view.setOnKeyPressed(event -> {
            long now = System.nanoTime();
            if (now - lastDirectionChange < DIRECTION_CHANGE_DELAY) {
                return; // Ignore rapid key presses
            }

            Direction newDirection = switch (event.getCode()) {
                case UP, W -> Direction.UP;
                case DOWN, S -> Direction.DOWN;
                case LEFT, A -> Direction.LEFT;
                case RIGHT, D -> Direction.RIGHT;
                default -> null;
            };
            
            if (newDirection != null) {
                Direction currentDirection = model.getSnake().getDirection();
                // Only change direction if it's not opposite to current direction
                if (!newDirection.isOpposite(currentDirection)) {
                    model.getSnake().setDirection(newDirection);
                    if (model.getMirrorSnake() != null) {
                        model.getMirrorSnake().setDirection(newDirection.getOpposite());
                    }
                    lastDirectionChange = now;
                }
            }
        });
        view.setFocusTraversable(true);
        view.requestFocus();
    }

    public SnakeView getView() {
        return view;
    }
}