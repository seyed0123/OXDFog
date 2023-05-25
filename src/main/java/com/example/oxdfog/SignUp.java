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

public class SignUp {
    @FXML
    private Button submit;
    @FXML
    private TextField usernameBar;
    @FXML
    private PasswordField passwordBar;
    @FXML
    private TextField ageBar;
    @FXML
    private Label idLabel;
    @FXML
    private Button mainButton;
    public void signUp(ActionEvent e)
    {

        String username=usernameBar.getText();
        String password = passwordBar.getText();
        try{
            int age = Integer.parseInt(ageBar.getText());
            if(age<=0)
                throw new Exception();
            JSONObject json = new JSONObject();
            json.put("username",username);
            json.put("password",password);
            json.put("age",age);
            out.println("sign up");
            out.println(json);
            out.flush();
            String id=in.readLine();
            idLabel.setText(id);
            user = new User(id,username,password,age);
            mainButton.setVisible(true);
        }catch (Exception p)
        {
            p.printStackTrace();
            ageBar.setText("enter valid number");
        }
    }
    public void mainPage(ActionEvent e) throws IOException {
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
}
