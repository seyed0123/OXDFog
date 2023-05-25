package com.example.oxdfog;

import com.example.oxdfog.Server.Game;
import com.google.gson.reflect.TypeToken;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.oxdfog.ClientApp.*;

public class MainWindow implements Initializable {
    private ArrayList<Game> games = new ArrayList<>();
    @FXML
    private ListView allGamesList;
    @FXML
    private ListView ownGamesList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        out.println("games list");
        String res;
        try {
            res=in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Game> games = gson.fromJson(res, new TypeToken<ArrayList<Game>>(){}.getType());
        ArrayList<String> info= new ArrayList<>();
        for(Game game:games)
        {
            info.add(game.toString());
        }
        allGamesList.getItems().addAll(info);
        allGamesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                SeeGame.game = games.get(allGamesList.getSelectionModel().getSelectedIndex());
                Stage stage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("SeeGame.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("SeeGame.css").toExternalForm());
                scene.getProperties().put("name","See game");
                stage.getIcons().add(new Image(ClientApp.logoAddress));
                stage.setTitle("OXDFog !!");
                stage.setScene(scene);
                stage.show();
            }
        });
        File directory = new File("src/main/java/com/example/oxdfog/Downloads");
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".png");
            }
        });
        ArrayList<Game> ownGame= new ArrayList<>();
        ArrayList<String> ownGameInfo = new ArrayList<>();
        for(File file:files)
        {
            out.println("view game");
            out.println(file.getName().substring(0,file.getName().length()-4));
            try {
                res=in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Game temp = gson.fromJson(res,Game.class);
            ownGame.add(temp);
            temp.setFilePath(file.getPath());
            ownGameInfo.add(temp.toString());
        }
        ownGamesList.getItems().addAll(ownGameInfo);
        ownGamesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                PlayPanel.game = ownGame.get(ownGamesList.getSelectionModel().getSelectedIndex());
                Stage stage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("PlayPanel.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = new Scene(root);
                scene.getProperties().put("name","play game");
                scene.getStylesheets().add(getClass().getResource("PlayGame.css").toExternalForm());
                stage.getIcons().add(new Image(ClientApp.logoAddress));
                stage.setTitle("OXDFog !!");
                stage.setScene(scene);
                stage.show();
            }
        });
    }
    public void end(ActionEvent e)
    {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        currentStage.close();
        out.println("close");
    }
}
