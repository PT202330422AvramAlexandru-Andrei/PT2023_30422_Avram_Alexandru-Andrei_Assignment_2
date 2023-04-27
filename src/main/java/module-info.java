module graphicaluserinterface {
    requires javafx.controls;
    requires javafx.fxml;


    opens graphicaluserinterface to javafx.fxml;
    exports graphicaluserinterface;
}