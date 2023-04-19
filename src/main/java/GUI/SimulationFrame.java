package GUI;

import BusinessLogic.SimulationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private void handleStartSimulation() {
        int timeLimit = Integer.parseInt(timeLimitTextField.getText());
        int maxProcessingTime = Integer.parseInt(maxProcessingTimeTextField.getText());
        int minProcessingTime = Integer.parseInt(minProcessingTimeTextField.getText());
        int numServers = Integer.parseInt(numServersTextField.getText());

        // Start the simulation with the retrieved input values
        SimulationManager simulationManager = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime, numServers);
        simulationManager.run();
    }

}
