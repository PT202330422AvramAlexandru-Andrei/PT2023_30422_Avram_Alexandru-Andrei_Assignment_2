package com.example.queuemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //System.out.println(getClass().getResource("/view.fxml"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view.fxml"));
        Scene scene = new Scene(root, 600, 350);
        stage.setTitle("Queue Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}