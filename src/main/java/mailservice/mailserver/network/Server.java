package mailservice.mailserver.network;

import mailservice.mailserver.controller.PersistenceController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int hostPort = 5000;
    private boolean running = true;

    public void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(hostPort)) {
                System.out.println("Mail Server running at port: " + hostPort);
                while (running) {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected");

                    ClientTask handler = new ClientTask(socket, new PersistenceController());
                    Thread client = new Thread(handler);
                    client.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}