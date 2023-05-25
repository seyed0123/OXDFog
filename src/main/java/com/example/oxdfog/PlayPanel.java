package com.example.oxdfog;

import com.example.oxdfog.Server.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayPanel implements Initializable {
    public static Game game;
    @FXML
    private Label Label;
    @FXML
    private ImageView currentGame;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Image image = new Image("G:\\code\\java\\OXDFog\\src\\main\\java\\com\\example\\oxdfog\\Downloads\\"+game.getId()+".png");
            currentGame.setImage(image);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Label.setText("you are now playing "+game.getTitle()+". enjoy it.");
        Label.setTextFill(Color.WHITE);
    }
    public void end(ActionEvent e)
    {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
