package newsnake;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle("-fx-background-color: #ffffff;");

        // Apple count selection
        HBox appleBox = new HBox(20);
        appleBox.setAlignment(Pos.CENTER);
        ToggleGroup appleGroup = new ToggleGroup();

        RadioButton singleApple = new RadioButton("1 Apple");
        RadioButton threeApples = new RadioButton("3 Apples");
        RadioButton fiveApples = new RadioButton("5 Apples");
        RadioButton randomApples = new RadioButton("Random");

        singleApple.setToggleGroup(appleGroup);
        threeApples.setToggleGroup(appleGroup);
        fiveApples.setToggleGroup(appleGroup);
        randomApples.setToggleGroup(appleGroup);
        singleApple.setSelected(true);

        appleBox.getChildren().addAll(singleApple, threeApples, fiveApples, randomApples);

        // Game mode selection
        HBox modeBox = new HBox(20);
        modeBox.setAlignment(Pos.CENTER);
        ToggleGroup modeGroup = new ToggleGroup();

        RadioButton clearMode = new RadioButton("Clear");
        RadioButton obstaclesMode = new RadioButton("Obstacles");
        RadioButton mirrorMode = new RadioButton("Mirror");
        RadioButton teleportMode = new RadioButton("Teleport");
        RadioButton reverseMode = new RadioButton("Reverse");

        clearMode.setToggleGroup(modeGroup);
        obstaclesMode.setToggleGroup(modeGroup);
        mirrorMode.setToggleGroup(modeGroup);
        teleportMode.setToggleGroup(modeGroup);
        reverseMode.setToggleGroup(modeGroup);
        clearMode.setSelected(true);

        modeBox.getChildren().addAll(clearMode, obstaclesMode, mirrorMode, teleportMode, reverseMode);

        Button startButton = new Button("Start Game");
        startButton.setStyle(buttonStyle);
        startButton.setFont(new Font("Arial", 18));
        startButton.setMinWidth(200);

        startButton.setOnAction(e -> {
            RadioButton selectedApple = (RadioButton) appleGroup.getSelectedToggle();
            RadioButton selectedMode = (RadioButton) modeGroup.getSelectedToggle();

            int appleCount = switch (selectedApple.getText()) {
                case "1 Apple" -> 1;
                case "3 Apples" -> 3;
                case "5 Apples" -> 5;
                default -> -1;
            };

            String mode = selectedMode.getText();
            LevelType selectedType = getLevelType(appleCount, mode);
            startGame(selectedType);
        });

        menuBox.getChildren().addAll(appleBox, modeBox, startButton);

        Scene scene = new Scene(menuBox, 400, 300);
        primaryStage.setTitle("Level Creator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private LevelType getLevelType(int appleCount, String mode) {
        return switch (appleCount) {
            case 1 -> switch (mode) {
                case "Clear" -> LevelType.SINGLE_APPLE_CLEAR;
                case "Obstacles" -> LevelType.SINGLE_APPLE_OBSTACLES;
                case "Mirror" -> LevelType.SINGLE_APPLE_MIRROR;
                case "Teleport" -> LevelType.SINGLE_APPLE_TELEPORT;
                case "Reverse" -> LevelType.SINGLE_APPLE_REVERSE;
                default -> LevelType.SINGLE_APPLE_CLEAR;
            };
            case 3 -> switch (mode) {
                case "Clear" -> LevelType.THREE_APPLES_CLEAR;
                case "Obstacles" -> LevelType.THREE_APPLES_OBSTACLES;
                case "Mirror" -> LevelType.THREE_APPLES_MIRROR;
                case "Teleport" -> LevelType.THREE_APPLES_TELEPORT;
                case "Reverse" -> LevelType.THREE_APPLES_REVERSE;
                default -> LevelType.THREE_APPLES_CLEAR;
            };
            case 5 -> switch (mode) {
                case "Clear" -> LevelType.FIVE_APPLES_CLEAR;
                case "Obstacles" -> LevelType.FIVE_APPLES_OBSTACLES;
                case "Mirror" -> LevelType.FIVE_APPLES_MIRROR;
                case "Teleport" -> LevelType.FIVE_APPLES_TELEPORT;
                case "Reverse" -> LevelType.FIVE_APPLES_REVERSE;
                default -> LevelType.FIVE_APPLES_CLEAR;
            };
            default -> switch (mode) {
                case "Clear" -> LevelType.RANDOM_APPLES_CLEAR;
                case "Obstacles" -> LevelType.RANDOM_APPLES_OBSTACLES;
                case "Mirror" -> LevelType.RANDOM_APPLES_MIRROR;
                case "Teleport" -> LevelType.RANDOM_APPLES_TELEPORT;
                case "Reverse" -> LevelType.RANDOM_APPLES_REVERSE;
                default -> LevelType.RANDOM_APPLES_CLEAR;
            };
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}