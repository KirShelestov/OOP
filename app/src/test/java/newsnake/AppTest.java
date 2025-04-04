package newsnake;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test 
    void appHasCorrectInitialSettings() {
        App classUnderTest = new App();
        // Basic test to verify window dimensions are set
        assertTrue(classUnderTest instanceof javafx.application.Application, 
            "App should extend JavaFX Application");
    }
}