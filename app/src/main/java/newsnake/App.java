package newsnake;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import newsnake.game.*;
import newsnake.controller.SnakeController;

public class App extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private String buttonStyle;

    @Override
    public void start(Stage primaryStage) {
        VBox menuBox = new VBox(30);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle("-fx-background-color: #ffffff;");

        Label titleLabel = new Label("Snake Game");
        titleLabel.setFont(new Font("Arial Bold", 64));
        titleLabel.setTextFill(Color.web("#4285F4"));

        Button classicButton = new Button("Classic Mode");
        Button customButton = new Button("Create Level");

        buttonStyle = "-fx-background-color: #4285F4;" +
                     "-fx-text-fill: white;" +
                     "-fx-background-radius: 4;" +
                     "-fx-padding: 15 30;";

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setOffsetX(0);
        shadow.setOffsetY(2);
        shadow.setRadius(5);

        for (Button btn : new Button[]{classicButton, customButton}) {
            btn.setStyle(buttonStyle);
            btn.setFont(new Font("Arial", 20));
            btn.setMinWidth(250);
            btn.setEffect(shadow);
            btn.setOnMouseEntered(e -> btn.setStyle(buttonStyle + "-fx-background-color: #3367D6;"));
            btn.setOnMouseExited(e -> btn.setStyle(buttonStyle));
        }

        classicButton.setOnAction(e -> startGame(LevelType.SINGLE_APPLE_CLEAR));
        customButton.setOnAction(e -> showLevelCreator(primaryStage));

        menuBox.getChildren().addAll(titleLabel, classicButton, customButton);

        Scene scene = new Scene(menuBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.WHITE);

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void startGame(LevelType levelType) {
        Stage gameStage = new Stage();
        SnakeController controller = new SnakeController(levelType);
        
        // Add game over and restart handling
        controller.getView().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE -> {
                    showGameOver(controller.getView().getModel().getScore());
                    gameStage.close();
                }
                case R -> {
                    startGame(levelType);
                    gameStage.close();
                }
            }
        });

        Scene gameScene = new Scene(new StackPane(controller.getView()));
        gameStage.setTitle("Snake Game - " + levelType.getDisplayName());
        gameStage.setScene(gameScene);
        gameStage.setResizable(false);
        gameStage.show();

        controller.startGame();
    }

    private void showGameOver(int score) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Game Over! Your score: " + score);
        alert.show();
    }

    private void showLevelCreator(Stage primaryStage) {
        Stage levelStage = new Stage();
        VBox mainContainer = new VBox(30);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label titleLabel = new Label("Create Level");
        titleLabel.setFont(new Font("Arial Bold", 24));

        HBox appleSelector = new HBox(20);
        appleSelector.setAlignment(Pos.CENTER);
        Label appleLabel = new Label("Apples:");
        appleLabel.setFont(new Font("Arial", 18));
        Button leftAppleBtn = new Button("<");
        Label appleCountLabel = new Label("1");
        Button rightAppleBtn = new Button(">");
        int[] appleCounts = {1, 3, 5, -1};
        final int[] currentAppleIndex = {0};

        leftAppleBtn.setOnAction(e -> {
            currentAppleIndex[0] = (currentAppleIndex[0] - 1 + appleCounts.length) % appleCounts.length;
            appleCountLabel.setText(appleCounts[currentAppleIndex[0]] == -1 ? "Random" : 
                String.valueOf(appleCounts[currentAppleIndex[0]]));
        });

        rightAppleBtn.setOnAction(e -> {
            currentAppleIndex[0] = (currentAppleIndex[0] + 1) % appleCounts.length;
            appleCountLabel.setText(appleCounts[currentAppleIndex[0]] == -1 ? "Random" : 
                String.valueOf(appleCounts[currentAppleIndex[0]]));
        });

        appleSelector.getChildren().addAll(appleLabel, leftAppleBtn, appleCountLabel, rightAppleBtn);

        HBox modeSelector = new HBox(20);
        modeSelector.setAlignment(Pos.CENTER);
        Label modeLabel = new Label("Mode:");
        modeLabel.setFont(new Font("Arial", 18));
        Button leftModeBtn = new Button("<");
        Label modeStateLabel = new Label("Classic");
        Button rightModeBtn = new Button(">");
        String[] modes = {"Classic", "Obstacles", "Mirror", "Teleport", "Reverse", "Flying"};
        final int[] currentModeIndex = {0};

        leftModeBtn.setOnAction(e -> {
            currentModeIndex[0] = (currentModeIndex[0] - 1 + modes.length) % modes.length;
            modeStateLabel.setText(modes[currentModeIndex[0]]);
        });

        rightModeBtn.setOnAction(e -> {
            currentModeIndex[0] = (currentModeIndex[0] + 1) % modes.length;
            modeStateLabel.setText(modes[currentModeIndex[0]]);
        });

        modeSelector.getChildren().addAll(modeLabel, leftModeBtn, modeStateLabel, rightModeBtn);

        Button startButton = new Button("Start Game");
        startButton.setStyle(buttonStyle);
        startButton.setFont(new Font("Arial", 18));
        startButton.setMinWidth(200);

        startButton.setOnAction(e -> {
            LevelType selectedType = getLevelType(appleCounts[currentAppleIndex[0]], modes[currentModeIndex[0]]);
            startGame(selectedType);
            levelStage.close();
        });

        mainContainer.getChildren().addAll(titleLabel, appleSelector, modeSelector, startButton);

        Scene scene = new Scene(mainContainer, 400, 300);
        levelStage.setTitle("Level Creator");
        levelStage.setScene(scene);
        levelStage.show();
    }

    private LevelType getLevelType(int appleCount, String mode) {
        return switch (appleCount) {
            case 1 -> switch (mode) {
                case "Obstacles" -> LevelType.SINGLE_APPLE_OBSTACLES;
                case "Mirror" -> LevelType.SINGLE_APPLE_MIRROR;
                case "Teleport" -> LevelType.SINGLE_APPLE_TELEPORT;
                case "Reverse" -> LevelType.SINGLE_APPLE_REVERSE;
                case "Flying" -> LevelType.SINGLE_APPLE_FLYING;
                default -> LevelType.SINGLE_APPLE_CLEAR;
            };
            case 3 -> switch (mode) {
                case "Obstacles" -> LevelType.THREE_APPLES_OBSTACLES;
                case "Mirror" -> LevelType.THREE_APPLES_MIRROR;
                case "Teleport" -> LevelType.THREE_APPLES_TELEPORT;
                case "Reverse" -> LevelType.THREE_APPLES_REVERSE;
                case "Flying" -> LevelType.THREE_APPLES_FLYING;
                default -> LevelType.THREE_APPLES_CLEAR;
            };
            case 5 -> switch (mode) {
                case "Obstacles" -> LevelType.FIVE_APPLES_OBSTACLES;
                case "Mirror" -> LevelType.FIVE_APPLES_MIRROR;
                case "Teleport" -> LevelType.FIVE_APPLES_TELEPORT;
                case "Reverse" -> LevelType.FIVE_APPLES_REVERSE;
                case "Flying" -> LevelType.FIVE_APPLES_FLYING;
                default -> LevelType.FIVE_APPLES_CLEAR;
            };
            default -> switch (mode) {
                case "Obstacles" -> LevelType.RANDOM_APPLES_OBSTACLES;
                case "Mirror" -> LevelType.RANDOM_APPLES_MIRROR;
                case "Teleport" -> LevelType.RANDOM_APPLES_TELEPORT;
                case "Reverse" -> LevelType.RANDOM_APPLES_REVERSE;
                case "Flying" -> LevelType.RANDOM_APPLES_FLYING;
                default -> LevelType.RANDOM_APPLES_CLEAR;
            };
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}