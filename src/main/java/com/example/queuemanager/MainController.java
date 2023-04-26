package com.example.queuemanager;

import BusinessLogic.FileWrite;
import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

public class MainController {

    @FXML
    TextField timeLimitTextField;

    @FXML
    TextField maxProcessingTimeTextField;

    @FXML
    TextField minProcessingTimeTextField;

    @FXML
    TextField maxArrivalTimeTextField;

    @FXML
    TextField minArrivalTimeTextField;

    @FXML
    TextField numServersTextField;

    @FXML
    TextField NTextField;

    @FXML
    ComboBox<String> strategyField;

    @FXML
    Button toQueues;

    @FXML
    public static Label queueLengthLabel = new Label();

    @FXML
    public static Label waitTimeLabel = new Label();

    @FXML
    public static Label timee = new Label();

    @FXML
    private void handleStartSimulation(ActionEvent event) throws IOException {

        Parent newPage = FXMLLoader.load(getClass().getClassLoader().getResource("queues.fxml"));

        // Get the current scene and set the new page as the root
        Scene currentScene = toQueues.getScene();
        currentScene.setRoot(newPage);


        //SimulationManager simulationManager = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime, numServers);

        //simulationManager.run();

        //FileWrite.write("test.txt", Runtime.getRuntime().exec("ls -l").getInputStream().toString());

        // Create a new thread to run the simulation
        Thread simulationThread = new Thread(() -> {
            int timeLimit = Integer.parseInt(timeLimitTextField.getText());
            int maxProcessingTime = Integer.parseInt(maxProcessingTimeTextField.getText());
            int minProcessingTime = Integer.parseInt(minProcessingTimeTextField.getText());
            int maxArrivalTime = Integer.parseInt(maxArrivalTimeTextField.getText());
            int minArrivalTime = Integer.parseInt(minArrivalTimeTextField.getText());
            int numServers = Integer.parseInt(numServersTextField.getText());
            int N = Integer.parseInt(NTextField.getText());
            SelectionPolicy strategy = SelectionPolicy.valueOf(strategyField.getValue());

            SimulationManager simulationManager = new SimulationManager(N, timeLimit, maxProcessingTime, minProcessingTime, maxArrivalTime, minArrivalTime, numServers, strategy);

            simulationManager.run();
        });

        // Start the simulation thread
        simulationThread.start();
    }

    public static void updateLabels(int currentTime) {

        App.update(timee, currentTime);
    }
}
