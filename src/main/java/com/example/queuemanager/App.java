package com.example.queuemanager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //System.out.println(getClass().getResource("/view.fxml"));

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view.fxml"));
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Queue Manager");
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        stage.setResizable(true);
        stage.setMinHeight(350);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void update(Label labelTime, int time) {
        Platform.runLater(() -> {
            labelTime.setText("Time: " + time);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}