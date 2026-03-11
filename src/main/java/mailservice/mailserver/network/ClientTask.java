package mailservice.mailserver.network;

import mailservice.mailserver.controller.PersistenceController;
import mailservice.mailserver.model.Mail;
import mailservice.mailserver.model.Mailbox;

import java.io.*;
import java.net.Socket;

public class ClientTask implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private final PersistenceController controller;

    // costruttore
    public ClientTask(Socket socket, PersistenceController controller) {
        this.socket = socket;
        this.controller = controller;
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
        Mailbox inbox = controller.getAccounts().getInbox(email);
        if(inbox == null){
            out.println("END");
            return;
        }
        System.out.println("last iddd: " + lastId);
        for (Mail mail : inbox.getInbox())
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
        controller.deleteMail(email, id);
        out.println("MAIL_DELETED");
    }
}