module demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires server.api;

    opens org.example.application to javafx.fxml;
    exports org.example.application;
}