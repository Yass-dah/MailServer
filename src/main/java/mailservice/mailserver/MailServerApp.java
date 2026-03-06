package mailservice.mailserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mailservice.mailserver.controller.LogViewController;
import mailservice.mailserver.controller.PersistenceController;

import java.io.IOException;

public class MailServerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MailServerApp.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LogViewController viewController = fxmlLoader.getController();
        PersistenceController persController = new PersistenceController();
        persController.print();
        mailservice.mailserver.network.Server server = new mailservice.mailserver.network.Server();
        server.start();
        stage.setTitle("Mail Server");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}