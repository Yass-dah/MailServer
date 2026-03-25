package mailservice.mailserver.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import mailservice.mailserver.MailServerApp;
import mailservice.mailserver.network.Server;

public class LogViewController {
    private MailServerApp main;
    private Server server;

    @FXML
    private ListView<String> logList;

    // Setters
    public void setMain(MailServerApp main) {
        this.main = main;
    }

    public void setServer(Server server) {
        this.server = server;
        logList.setItems(server.getLogList());
    }

    @FXML
    protected void onStopButtonClick() {
        main.stopServer();
        Platform.exit();
    }
}