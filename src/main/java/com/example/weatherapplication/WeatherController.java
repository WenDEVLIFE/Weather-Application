package com.example.weatherapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WeatherController {

    @FXML
    private Label WeatherLabels;

    @FXML
    private AnchorPane anchorpane;

    @FXML
    private Button Track_Button;

    @FXML
    private ComboBox<String> SelectDay;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Your initialization code goes here
        WeatherLabels.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");

        Track_Button.getStyleClass().addAll("btn", "btn-outline-primary");
        anchorpane.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");

        // Add days to the combobox
        // Add days to the ComboBox
        ObservableList<String> days = FXCollections.observableArrayList(
                "Select a Day", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        );
        SelectDay.setItems(days);  // Set the items first
        SelectDay.getSelectionModel().selectFirst();  // Select the first item



    }
}