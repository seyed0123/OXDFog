package com.example.oxdfog;

import com.example.oxdfog.Server.Game;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.oxdfog.ClientApp.*;

public class SeeGame implements Initializable {
    public static Game game;
    @FXML
    private Label gameInfo;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameInfo.setText(game.toString());
    }
    public void download(ActionEvent e)
    {
        if(game.getMinAge()>user.getAge())
        {
            gameInfo.setText("This game is not suitable for your age group");
            return;
        }
        out.println("download game");
        out.println(game.getId());
        try {
            InputStream is = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] imageData = baos.toByteArray();
            File imageFile = new File("G:\\code\\java\\OXDFog\\src\\main\\java\\com\\example\\oxdfog\\Downloads\\"+game.getFilePath());
            FileOutputStream fos = new FileOutputStream(imageFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(imageData, 0, imageData.length);
            bos.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        gameInfo.setText("game downloaded");
    }
    public void back(ActionEvent e)
    {
        ObservableList<Window> openWindow=Window.getWindows();
        for(Window window :openWindow)
        {
            if(window instanceof Stage)
            {
                if (window.getScene().getProperties().get("name").equals("main window"))
                {
                    ((Stage) window).close();
                }
            }
        }
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        currentStage.close();
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Scene scene = new Scene(root);
        scene.getProperties().put("name","main window");
        scene.getStylesheets().add(getClass().getResource("MainWindow.css").toExternalForm());
        stage.getIcons().add(new Image(ClientApp.logoAddress));
        stage.setTitle("OXDFog !!");
        stage.setScene(scene);
        stage.show();
    }
}
