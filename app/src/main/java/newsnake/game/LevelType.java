package newsnake.game;

public enum LevelType {
    // Classic modes
    SINGLE_APPLE_CLEAR("Single Apple - Clear", 1, GameMode.CLEAR),
    THREE_APPLES_CLEAR("Three Apples - Clear", 3, GameMode.CLEAR),
    FIVE_APPLES_CLEAR("Five Apples - Clear", 5, GameMode.CLEAR),
    RANDOM_APPLES_CLEAR("Random Apples - Clear", -1, GameMode.CLEAR),

    // Obstacle modes
    SINGLE_APPLE_OBSTACLES("Single Apple - Obstacles", 1, GameMode.OBSTACLES),
    THREE_APPLES_OBSTACLES("Three Apples - Obstacles", 3, GameMode.OBSTACLES),
    FIVE_APPLES_OBSTACLES("Five Apples - Obstacles", 5, GameMode.OBSTACLES),
    RANDOM_APPLES_OBSTACLES("Random Apples - Obstacles", -1, GameMode.OBSTACLES),

    // Mirror modes
    SINGLE_APPLE_MIRROR("Single Apple - Mirror", 1, GameMode.MIRROR),
    THREE_APPLES_MIRROR("Three Apples - Mirror", 3, GameMode.MIRROR),
    FIVE_APPLES_MIRROR("Five Apples - Mirror", 5, GameMode.MIRROR),
    RANDOM_APPLES_MIRROR("Random Apples - Mirror", -1, GameMode.MIRROR),

    // Teleport modes
    SINGLE_APPLE_TELEPORT("Single Apple - Teleport", 1, GameMode.TELEPORT),
    THREE_APPLES_TELEPORT("Three Apples - Teleport", 3, GameMode.TELEPORT),
    FIVE_APPLES_TELEPORT("Five Apples - Teleport", 5, GameMode.TELEPORT),
    RANDOM_APPLES_TELEPORT("Random Apples - Teleport", -1, GameMode.TELEPORT),

    // Reverse modes
    SINGLE_APPLE_REVERSE("Single Apple - Reverse", 1, GameMode.REVERSE),
    THREE_APPLES_REVERSE("Three Apples - Reverse", 3, GameMode.REVERSE),
    FIVE_APPLES_REVERSE("Five Apples - Reverse", 5, GameMode.REVERSE),
    RANDOM_APPLES_REVERSE("Random Apples - Reverse", -1, GameMode.REVERSE);

    private final String displayName;
    private final int appleCount;
    private final GameMode gameMode;

    LevelType(String displayName, int appleCount, GameMode gameMode) {
        this.displayName = displayName;
        this.appleCount = appleCount;
        this.gameMode = gameMode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAppleCount() {
        return appleCount;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public boolean hasObstacles() {
        return gameMode == GameMode.OBSTACLES;
    }
}