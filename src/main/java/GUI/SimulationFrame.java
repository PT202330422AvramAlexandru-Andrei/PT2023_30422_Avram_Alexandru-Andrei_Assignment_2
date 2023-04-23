package GUI;

import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SimulationFrame {

    @FXML
    TextField timeLimitTextField;

    @FXML
    TextField maxProcessingTimeTextField;

    @FXML
    TextField minProcessingTimeTextField;

    @FXML
    TextField numServersTextField;

    @FXML
    ComboBox<String> strategyField;

    @FXML
    private void handleStartSimulation() {
        int timeLimit = Integer.parseInt(timeLimitTextField.getText());
        int maxProcessingTime = Integer.parseInt(maxProcessingTimeTextField.getText());
        int minProcessingTime = Integer.parseInt(minProcessingTimeTextField.getText());
        int numServers = Integer.parseInt(numServersTextField.getText());
        SelectionPolicy selectionPolicy = SelectionPolicy.valueOf(strategyField.getValue());


        // Start the simulation with the retrieved input values
        SimulationManager simulationManager = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime, numServers, selectionPolicy);
        simulationManager.run();
    }

}
