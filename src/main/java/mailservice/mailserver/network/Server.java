package mailservice.mailserver.network;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mailservice.mailserver.controller.PersistenceController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int hostPort = 5000;
    private boolean running = true;
    private ServerSocket serverSocket;
    private PersistenceController persistenceController;
    private final ObservableList<String> logList = FXCollections.observableArrayList();

    // Getters
    public ObservableList<String> getLogList(){
        return logList;
    }

    // inizializza controller & socket + accetta connessioni + chiamata threads per client
    public void start() {
        persistenceController = new PersistenceController();
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(hostPort);
                System.out.println("Mail Server running at port: " + hostPort);
                while (running) {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected");
                    ClientTask handler = new ClientTask(socket, persistenceController, logList);
                    Thread client = new Thread(handler);
                    client.start();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    // stop accettazioni(run = false) + chiude socket
    public void stop(){
        running = false;
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}