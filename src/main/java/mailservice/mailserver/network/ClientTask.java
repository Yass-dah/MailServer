package mailservice.mailserver.network;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import mailservice.mailserver.controller.PersistenceController;
import mailservice.mailserver.model.Mail;
import mailservice.mailserver.model.Mailbox;

import java.io.*;
import java.net.Socket;

public class ClientTask implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private final PersistenceController controller;
    private final ObservableList<String> logList;

    // costruttore
    public ClientTask(Socket socket, PersistenceController controller, ObservableList<String> logList) {
        this.socket = socket;
        this.controller = controller;
        this.logList = logList;
    }

    // main task
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String request;
            while ((request = in.readLine()) != null)
                handleRequest(request);
        } catch (IOException e) {
            out.println("Client disconnected");
        }
    }

    // handlers operazioni
    private void handleRequest(String request) {
        String[] parts = request.split("\\|");
        switch (parts[0]) {
            case "1":
                handleCheckEmail(parts[1]);
                break;

            case "2":
                handleSendMail(parts);
                break;

            case "3":
                handleGetMails(parts[1], Long.parseLong(parts[2]));
                break;

            case "4":
                handleDeleteMail(parts[1], Long.parseLong(parts[2]));
                break;
        }
    }

    private void handleCheckEmail(String email) {
        // 1|email
        if(out != null) {
            Platform.runLater(() -> logList.add("CLIENT LOGIN | checking email in data: " + email));
            boolean emailFound = controller.getAccounts().containsAccount(email);
            if(emailFound) {
                out.println("OK");
                return;
            }
            if(controller.emailExists(email)){
                controller.loadMailbox(email);
                out.println("OK");
                return;
            }
            out.println("ERROR");
        }
    }

    private void handleSendMail(String[] parts) {
        // 2|from|to|subject|body
        Platform.runLater(() -> logList.add("CLIENT SENDER | sending mail from " + parts[1] + " to " + parts[2]));
        String from = parts[1];
        String to = parts[2];
        String subject = parts[3];
        String body = parts[4];

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setBody(body);
        mail.initDateNow();
        controller.saveMail(to, mail);
        if(out != null)
            out.println("MAIL_SENT");
    }

    private void handleGetMails(String email, long lastId) {
        // 3|email|lastId
        controller.loadMailbox(email);
        Platform.runLater(() -> logList.add("CLIENT INBOX | delivering inbox for " + email));
        Mailbox inbox = controller.getAccounts().getInbox(email);
        if(inbox == null){
            out.println("END");
            return;
        }
        for(Mail mail : inbox.getInbox())
            if(mail.getId() > lastId)
                out.println(mail.getId() + "|"
                    + mail.getFrom() + "|"
                    + mail.getTo() + "|"
                    + mail.getSubject() + "|"
                    + mail.getBody() + "|" + mail.getDate() + "|");
        out.println("END");
    }

    private void handleDeleteMail(String email, long id) {
        // 4|email|id
        Platform.runLater(() -> logList.add("CLIENT INBOX | deleting mail " + id + " of " + email));
        controller.deleteMail(email, id);
        out.println("MAIL_DELETED");
    }
}