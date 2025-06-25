module ru.nsu.shelestov.task_2_4_1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.nsu.shelestov.task_2_4_1 to javafx.fxml;
    exports ru.nsu.shelestov.task_2_4_1;
}