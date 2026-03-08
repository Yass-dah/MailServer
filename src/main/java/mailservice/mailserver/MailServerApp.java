package mailservice.mailserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mailservice.mailserver.controller.LogViewController;
import mailservice.mailserver.controller.PersistenceController;
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
        PersistenceController persController = new PersistenceController();
        persController.print();
        server.start();
        mainStage.setTitle("Mail Server");
        mainStage.setScene(scene);
        mainStage.show();
        end();
    }

    public void end() {
        mainStage.setOnCloseRequest(event -> {
            stopServer();
        });
    }

    public void stopServer(){
        server.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}