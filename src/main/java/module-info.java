module com.example.weatherapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires MaterialFX;

    opens com.example.weatherapplication to javafx.fxml;
    exports com.example.weatherapplication;
}