package com.example.oxdfog;

import com.example.oxdfog.Server.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import static com.example.oxdfog.ClientApp.*;

public class Login {
    @FXML
    private Button signUpButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField idBar;
    @FXML
    private PasswordField passwordBar;
    @FXML
    private Label loginStatus;
    public void login(ActionEvent e) throws IOException {
        String id = idBar.getText();
        String password = passwordBar.getText();
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("password",password);
        out.println("login");
        out.println(json);
        String response = in.readLine();
        if(Objects.equals(response, "login failed"))
        {
            loginStatus.setText("login failed");
            return;
        }
        user = gson.fromJson(response,User.class);
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Scene scene = new Scene(root);
        scene.getProperties().put("name","main window");
        scene.getStylesheets().add(getClass().getResource("MainWindow.css").toExternalForm());
        stage.getIcons().add(new Image(ClientApp.logoAddress));
        stage.setTitle("OXDFog !!");
        stage.setScene(scene);
        stage.show();
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        currentStage.close();
    }
    public void signUp(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
        currentStage.setScene(scene);
        currentStage.show();
    }
}
