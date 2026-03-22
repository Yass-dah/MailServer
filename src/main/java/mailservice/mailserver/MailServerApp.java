package mailservice.mailserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mailservice.mailserver.controller.LogViewController;
import mailservice.mailserver.network.Server;

import java.io.IOException;

public class MailServerApp extends Application {
    private Stage mainStage;
    private Server server;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        server = new Server();
        FXMLLoader fxmlLoader = new FXMLLoader(MailServerApp.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LogViewController viewController = fxmlLoader.getController();
        viewController.setMain(this);
        viewController.setServer(server);
        server.start();
        mainStage.setTitle("Mail Server");
        mainStage.setScene(scene);
        mainStage.show();
        end();
    }

    // setAction per chiusura window
    public void end() {
        mainStage.setOnCloseRequest(event -> stopServer());
    }

    // stop running server
    public void stopServer(){
        server.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}