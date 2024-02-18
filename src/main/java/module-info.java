module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
    exports application;
    opens application to javafx.fxml;
    exports application.settings;
    opens application.settings to javafx.fxml;
    exports application.cell;
    opens application.cell to javafx.fxml;
    exports application.board;
    opens application.board to javafx.fxml;
}