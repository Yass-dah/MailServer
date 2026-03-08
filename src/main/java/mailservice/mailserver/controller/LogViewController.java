package mailservice.mailserver.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import mailservice.mailserver.MailServerApp;

public class LogViewController {
    private MailServerApp main;
    @FXML
    private ListView<String> logList;

    // Getters & Setters
    public MailServerApp getMain() {
        return main;
    }

    public void setMain(MailServerApp main) {
        this.main = main;
    }

    @FXML
    protected void onStopButtonClick() {
        main.stopServer();
        Platform.exit();
    }
}