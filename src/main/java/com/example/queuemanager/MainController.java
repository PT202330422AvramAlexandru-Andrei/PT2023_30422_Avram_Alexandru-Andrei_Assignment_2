package com.example.queuemanager;

import BusinessLogic.FileWrite;
import BusinessLogic.SimulationManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    TextField timeLimitTextField;

    @FXML
    TextField maxProcessingTimeTextField;

    @FXML
    TextField minProcessingTimeTextField;

    @FXML
    TextField numServersTextField;

    @FXML
    TextField outputTextArea;

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

        int timeLimit = Integer.parseInt(timeLimitTextField.getText());
        int maxProcessingTime = Integer.parseInt(maxProcessingTimeTextField.getText());
        int minProcessingTime = Integer.parseInt(minProcessingTimeTextField.getText());
        int numServers = Integer.parseInt(numServersTextField.getText());

        //SimulationManager simulationManager = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime, numServers);

        //simulationManager.run();

        //FileWrite.write("test.txt", Runtime.getRuntime().exec("ls -l").getInputStream().toString());

        // Create a new thread to run the simulation
        Thread simulationThread = new Thread(() -> {
            SimulationManager simulationManager = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime, numServers);
            simulationManager.run();

            /*Platform.runLater(() -> {
                queueLengthLabel.setText(Integer.toString(simulationManager.getAverageQueueLength()));
                waitTimeLabel.setText(Integer.toString(simulationManager.getAverageWaitingTime()));

                timee.setText(Integer.toString(simulationManager.getCurrentTime()));
            });*/

            Thread timeThread = new Thread(() -> {
                while (simulationManager.getCurrentTime() < timeLimit) {
                    int currentTime = simulationManager.getCurrentTime();
                    String outputText = "Time: " + currentTime + "\n";
                    final String finalOutputText = outputText;
                    Platform.runLater(() -> {
                        timee.setText(Integer.toString(currentTime));
                    });
                }
            });

            timeThread.start();
        });

        // Start the simulation thread
        simulationThread.start();
    }

    public static synchronized void updateLabels(int averageQueueLength, int averageWaitingTime, int currentTime) {
        queueLengthLabel.setText(Integer.toString(averageQueueLength));
        waitTimeLabel.setText(Integer.toString(averageWaitingTime));
        timee.setText("Time" + Integer.toString(currentTime));
    }
}
