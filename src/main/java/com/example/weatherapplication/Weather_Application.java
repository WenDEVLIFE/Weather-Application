package com.example.weatherapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Weather_Application extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Weather_Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        WeatherController controller = fxmlLoader.getController();
        controller.initialize(null, null);
        Image icon = new Image(getClass().getResourceAsStream("icons/cloudynigga.png"));
        stage.getIcons().add(icon);
        scene.getStylesheets().add(getClass().getResource("materialfx.css").toExternalForm());
        stage.setTitle("Weather Tracker");
        stage.setResizable(false);
        centerStageOnScreen(stage);
        stage.setScene(scene);
        stage.show();


    }
    private void centerStageOnScreen(Stage stage) {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        double centerX = (screenWidth - stageWidth) / 2;
        double centerY = (screenHeight - stageHeight) / 2;

        stage.setX(centerX);
        stage.setY(centerY);
    }
    public static void main(String[] args) {
        launch();
    }
}