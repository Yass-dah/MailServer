package mailservice.mailserver.network;

import mailservice.mailserver.controller.PersistenceController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int hostPort = 5000;
    private boolean running = true;
    private ServerSocket serverSocket;
    private ArrayList<ClientTask> clients = new ArrayList<>();

    public void start() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(hostPort);
                System.out.println("Mail Server running at port: " + hostPort);
                while (running) {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected");

                    ClientTask handler = new ClientTask(socket, new PersistenceController());
                    clients.add(handler);
                    Thread client = new Thread(handler);
                    client.start();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    public void stop(){
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
            for(ClientTask c: clients)
                c.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}