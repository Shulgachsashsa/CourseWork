module demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires server.api;
    requires java.desktop;
    requires java.management;

    opens org.example.application to javafx.fxml;
    exports org.example.application;
}