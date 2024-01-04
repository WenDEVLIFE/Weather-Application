package com.example.weatherapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

import javafx.scene.image.ImageView;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;


public class WeatherController {

    @FXML
    private Label WeatherLabels;

    @FXML
    private Label Tempnum;

    @FXML
    private Label Humidity;

    @FXML
    private Label RainStatus;

    @FXML
    private Label GetDay;


    @FXML
    private AnchorPane anchorpane;

    @FXML
    private Button Track_Button;

    @FXML
    private ComboBox<String> SelectDay;

    @FXML
    private TextField Cityfield;

    @FXML
    private ImageView TemperatureView;

    @FXML
    private Button FacebookButton;

    @FXML
    private Button LinkedinButton;

    @FXML
    private Button GithubButton;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Tooltip for the buttons  (Github, Linkedin, Facebook)
        Tooltip tooltip = new Tooltip("Click to see my Github");
        Tooltip tooltip1 = new Tooltip("Click to see my Linkedin");
        Tooltip tooltip2 = new Tooltip("Click to see my Facebook");

        // Set the tooltip on the buttons (Github, Linkedin, Facebook)
        GithubButton.setTooltip(tooltip);
        LinkedinButton.setTooltip(tooltip1);
        FacebookButton.setTooltip(tooltip2);


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
    @FXML
    void FacebookAction (ActionEvent event){
        try{
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://www.facebook.com/WenDevLife"));
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @FXML
    void LinkedinAction(ActionEvent event){
        try{
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://www.linkedin.com/in/frouen-medina-jr-a75a69287/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void GithubAction (ActionEvent event){
        try{
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://github.com/WenDEVLIFE"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void SeeWeather (ActionEvent event) throws IOException {
        // Your code to fetch the weather goes here
        // This will also check if the internet is connected
        if (InternetStatus()){
            // Get the city and selected day
            String city = Cityfield.getText();
            String selectedDay = SelectDay.getValue();
            if (selectedDay.equals("Select a Day")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a day");
                alert.showAndWait();
            } else {
                fetchWeatherTracker(city, selectedDay);

            }
        }  else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please check your internet connection");
            alert.showAndWait();
        }
    }
    private void fetchWeatherTracker(String city, String selectedDay) throws UnsupportedEncodingException {
        String apiKey = "422eb99fdf62cd378b714a00c531e391";
        String cityapi = city; // Your city name
        String encodedCity = URLEncoder.encode(cityapi, java.nio.charset.StandardCharsets.UTF_8.toString());

        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            // Send the HTTP request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response
            JSONObject json = new JSONObject(response.body());
            int statusCode = response.statusCode();

            // Check if the status code is 200 i.e. OK
            if (statusCode == 200) {

                // Get the temperature, humidity and rain status from the JSON response
            double temperature = json.getJSONObject("main").getDouble("temp");
            double humidity = json.getJSONObject("main").getDouble("humidity");
            boolean isRaining = json.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("main")
                    .equalsIgnoreCase("Rain");

            UpdateInfo(temperature, humidity, isRaining, selectedDay);
            } else if (statusCode == 404) {
                // City not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("City not found");
                alert.showAndWait();
                // You can display an alert or handle this situation as needed
            } else {
                // Handle other HTTP status codes if necessary
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unexpected Status code error" + statusCode);
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean InternetStatus() {
        try {
            // This will check if the internet is connected
            InetAddress address = InetAddress.getByName("www.google.com");
            HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create("https://www.google.com")).build(), HttpResponse.BodyHandlers.ofString());

            // If the internet is connected, return true
            return true;
        } catch (IOException | InterruptedException e) {
            // If the internet is not connected, return false
            return false;
        }
    }

    private void UpdateInfo(double temperature, double humidity, boolean isRaining, String selectedDay) {
        // Update the labels with the fetched data
        double temperatureCelsius = temperature - 273.15;
        Tempnum.setText(temperatureCelsius + "°C");
       Humidity.setText(humidity + "%");
        GetDay.setText(selectedDay);

        // Check if it will rain and update the label
        if (isRaining) {
            RainStatus.setText("It will rain on " + selectedDay);
        } else {
            RainStatus.setText("No rain on " + selectedDay);
        }

        // Update the image based on the temperature value
        SwapImage(temperatureCelsius);

    }

    private void SwapImage(double temperatureCelsius) {
        String imagePath;

        // Set the image path based on the temperature
        if (temperatureCelsius < 10) {
            imagePath = "icons/cold.png";
        } else if (temperatureCelsius >= 10 && temperatureCelsius < 20) {
            imagePath = "icons/normal.png";
        } else if (temperatureCelsius >= 20 && temperatureCelsius < 30) {
            imagePath = "icons/normal.png";
        } else {
            imagePath = "icons/hot.png";
        }

        try {
            // Load and set the image on the ImageView
            InputStream inputStream = getClass().getResourceAsStream(imagePath);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                TemperatureView.setImage(image);
            } else {
                System.err.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}