package com.example.oxdfog;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            // Perform input/output operations on the SSL socket
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            // Example: Send data to the server
            /*out.println("sign up");
            out.flush();
            JSONObject json = new JSONObject();
            json.put("id","4");
            json.put("username","arshia");
            json.put("password","123");
            json.put("age",12);
            out.println(json);
            out.flush();
            System.out.println(in.readLine());*/
            gson = new Gson();
            launch(args);
            /*out.println("login");
            JSONObject json = new JSONObject();
            json.put("id", "4");
            json.put("password", "123");
            out.println(json);*/
            // Close the SSL socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Image image = new Image(logoAddress);
        Scene scene = new Scene(root);
        primaryStage.setTitle("OXDFog !!");
        primaryStage.getIcons().add(image);
        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
