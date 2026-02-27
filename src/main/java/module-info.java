module mailservice.mailserver.mailserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens mailservice.mailserver.mailserver to javafx.fxml;
    exports mailservice.mailserver.mailserver;
}