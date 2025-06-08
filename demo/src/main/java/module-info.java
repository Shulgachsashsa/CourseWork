module demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires server.api;
    requires java.management;
    requires org.apache.poi.ooxml;

    opens org.example.application to javafx.fxml;
    exports org.example.application;
}