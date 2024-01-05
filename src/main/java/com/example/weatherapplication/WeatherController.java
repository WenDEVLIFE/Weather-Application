package com.example.weatherapplication;

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

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.image.ImageView;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class WeatherController {

    @FXML
    private Label WeatherLabels;

    @FXML
    private Label Tempnum;

    @FXML
    private Label TuesdayTemp;

    @FXML
    private Label WednesdayTemp;

    @FXML
    private Label Thursdaytemp;

    @FXML
    private Label Fridaytemp;

    @FXML
    private Label SaturdayTemp;

    @FXML
    private Label SundayTemp;

    @FXML
    private Label Humidity;

    @FXML
    private Label TuesdayHumidity;

    @FXML
    private Label WednesdayHumidity;

    @FXML
    private Label ThuHumidity;

    @FXML
    private Label FriHumidity;

    @FXML
    private Label SatHumidity;

    @FXML
    private Label SunHumidity;

    @FXML
    private Label RainStatus;

    @FXML
    private Label RainStatusTuesday;

    @FXML
    private Label RainStatusWed;

    @FXML
    private Label RainStatusThu;

    @FXML
    private Label RainStatusFri;

    @FXML
    private Label RainStatusSat;

    @FXML
    private Label RainStatusSun;


    @FXML
    private Label CityName;


    @FXML
    private Label DateMon;

    @FXML
    private Label DateTue;

    @FXML
    private Label DateWed;

    @FXML
    private Label DateThu;

    @FXML
    private Label DateFri;

    @FXML
    private Label DateSat;

    @FXML
    private Label DateSun;

    @FXML
    private AnchorPane anchorpane;

    @FXML
    private Button Track_Button;


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

    @FXML
    private TabPane WeatherTab;

    @FXML
    private Tab WeatherMain;

    @FXML
    private Tab WeatherInfo;

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


            fetchWeatherForecast(city);
            WeatherTab.getSelectionModel().select( WeatherInfo);

        }  else{
            WeatherTab.getSelectionModel().select( WeatherInfo);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please check your internet connection");
            alert.showAndWait();
        }
    }
    private void fetchWeatherForecast(String city) throws UnsupportedEncodingException {
        String apiKey = "422eb99fdf62cd378b714a00c531e391";
        String cityapi = city;
        String encodedCity = URLEncoder.encode(cityapi, java.nio.charset.StandardCharsets.UTF_8.toString());

        String apiUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" + encodedCity + "&appid=" + apiKey;

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
                // Extract forecast data for each day
                JSONArray forecastList = json.getJSONArray("list");

                for (int i = 0; i < forecastList.length(); i++) {
                    JSONObject forecast = forecastList.getJSONObject(i);
                    String dateTimeString = forecast.getString("dt_txt");
                    LocalDate date = LocalDate.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    // Check if the date is within the next week (Monday to Sunday)
                    if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusWeeks(1))) {
                        String dayOfWeek = date.getDayOfWeek().toString();
                        double temperature = forecast.getJSONObject("main").getDouble("temp");
                        double humidity = forecast.getJSONObject("main").getDouble("humidity");
                        boolean isRaining = forecast.getJSONArray("weather")
                                .getJSONObject(0)
                                .getString("main")
                                .equalsIgnoreCase("Rain");

                        // Debugging statement
                        System.out.println("Day: " + dayOfWeek + ", Date: " + date);

                        // Update labels for each day
                        UpdateInfo(dayOfWeek, temperature, humidity, isRaining, city,date);
                    }
                }
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

    private void UpdateInfo(String dayOfWeek, double temperature, double humidity, boolean isRaining, String city, LocalDate date) {
        // Debugging statement
        System.out.println("Updating info for Day: " + dayOfWeek);

        // Format temperature with one decimal place
        String temperatureText = String.format("%.1f°C", temperature - 273.15);

        // Update the labels with the fetched data
        CityName.setText(city);

        switch (dayOfWeek) {
            case "MONDAY":
                Tempnum.setText(temperatureText);
                Humidity.setText(humidity + "%");
                RainStatus.setText(isRaining ? "It will rain" : "No rain");
                SwapImage(temperature - 273.15);
                DateMon.setText(date.toString());  // Set the date label for Monday
                break;
            case "TUESDAY":
                TuesdayTemp.setText(temperatureText);
                TuesdayHumidity.setText("" + humidity + "%");
                RainStatusTuesday.setText(isRaining ? "It will rain" : "No rain");
                DateTue.setText(date.toString());
                break;
            case "WEDNESDAY":
                WednesdayTemp.setText(temperatureText);
                WednesdayHumidity.setText(humidity + "%");
                RainStatusWed.setText(isRaining ? "It will rain" : "No rain");
                DateWed.setText(date.toString());
                break;
            case "THURSDAY":
                Thursdaytemp.setText(temperatureText);
                ThuHumidity.setText(humidity + "%");
                RainStatusThu.setText(isRaining ? "It will rain" : "No rain");
                DateThu.setText(date.toString());
                break;
            case "FRIDAY":
                Fridaytemp.setText(temperatureText);
                FriHumidity.setText(humidity + "%");
                RainStatusFri.setText(isRaining ? "It will rain" : "No rain");
                DateFri.setText(date.toString());
                break;
            case "SATURDAY":
                SaturdayTemp.setText(temperatureText);
                SatHumidity.setText(humidity + "%");
                RainStatusSat.setText(isRaining ? "It will rain" : "No rain");
                DateSat.setText(date.toString());
                break;
            case "SUNDAY":
                SundayTemp.setText(temperatureText);
                SunHumidity.setText(humidity + "%");
                RainStatusSun.setText(isRaining ? "It will rain" : "No rain");
                DateSun.setText(date.toString());
                break;
            default:
                // Handle unexpected day
                break;
        }
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
 @FXML
 // This will go back to Track the weather
    void TrackAction (ActionEvent event){
        WeatherTab.getSelectionModel().select(WeatherMain);
 }

}