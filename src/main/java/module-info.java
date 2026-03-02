module mailservice.mailserver.mailserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens mailservice.mailserver to javafx.fxml;
    exports mailservice.mailserver;
    exports mailservice.mailserver.controller;
    opens mailservice.mailserver.controller to javafx.fxml;
}