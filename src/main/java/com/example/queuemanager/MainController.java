package com.example.queuemanager;

import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.*;

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
    private TextArea console;
    public static PrintStream ps ;

    public void initialize() {
        ps = new PrintStream(new Console(console)) ;
    }


    @FXML
    private void handleStartSimulation(ActionEvent event) throws IOException {

        Parent newPage = FXMLLoader.load(getClass().getClassLoader().getResource("queues.fxml"));

        // Get the current scene and set the new page as the root
        Scene currentScene = toQueues.getScene();
        currentScene.setRoot(newPage);

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

    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }
}
