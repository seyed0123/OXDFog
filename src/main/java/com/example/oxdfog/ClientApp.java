package com.example.oxdfog;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import com.example.oxdfog.Server.User;

public class ClientApp extends Application {
    static String host = "localhost";
    static int port = 1056; // default HTTPS port
    static User user;
    static Socket socket;
    static BufferedReader in;
    static PrintWriter out;
    static Gson gson;
    public static final String logoAddress="G:\\code\\java\\OXDFog\\src\\main\\resources\\com\\example\\oxdfog\\OXDFog.jpeg";

    public static void main(String[] args) {
        try {

            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            gson = new Gson();
            launch(args);
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        File file = new File("src/main/resources/com/example/oxdfog/OXDFog.mp4");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitHeight(350);
        mediaView.setFitWidth(700);
        HBox hBox = new HBox();
        hBox.getChildren().add(mediaView);
        Scene scene2 = new Scene(hBox);
        stage.setScene(scene2);
        stage.show();
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(stage::close);
        stage.setOnHiding(event -> {
            mediaPlayer.pause();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage primaryStage = new Stage();
            Image image = new Image(logoAddress);
            Scene scene = new Scene(root);
            primaryStage.setTitle("OXDFog !!");
            primaryStage.getIcons().add(image);
            scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }
}
