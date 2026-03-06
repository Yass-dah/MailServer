package mailservice.mailserver.network;

import mailservice.mailserver.controller.PersistenceController;
import mailservice.mailserver.model.Mail;
import mailservice.mailserver.model.Mailbox;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientTask implements Runnable {
    private Socket socket;
    PersistenceController controller;

    private BufferedReader in;
    private PrintWriter out;

    public ClientTask(Socket socket, PersistenceController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String request;
            while ((request = in.readLine()) != null)
                handleRequest(request);
        } catch (IOException e) {
            out.println("Client disconnected");
        }
    }

    private void handleRequest(String request) {
        String[] parts = request.split("\\|");

        switch (parts[0]) {
            case "0":
                handleCheckConn();
                break;

            case "1":
                handleCheckEmail(parts[1]);
                break;

            case "2":
                handleSendMail(parts);
                break;

            case "3":
                handleGetMails(parts[1]);
                break;

            case "4":
                handleDeleteMail(parts[1], Long.parseLong(parts[2]));
                break;
        }
    }

    private void handleCheckConn(){
        if(out != null) out.println("OK");
    }

    private void handleCheckEmail(String email) {
        // 1|mail@mail.com
        if(out != null) {
            boolean emailFound = controller.getAccounts().containsAccount(email);
            if(emailFound) {
                out.println("OK");
                return;
            }
            if(controller.emailExists(email)){
                out.println("OK");
                controller.loadMailbox(email);
                return;
            }
            out.println("ERROR");
        }
    }

    private void handleSendMail(String[] parts) {
        // 1|from|to|subject|body
        String from = parts[1];
        String to = parts[2];
        String subject = parts[3];
        String body = parts[4];

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setBody(body);
        mail.setDate(LocalDateTime.now());

        controller.saveMail(to, mail);
        if(out != null)
            out.println("MAIL_SENT");
    }

    private void handleGetMails(String email) {
        Mailbox inbox = controller.getAccounts().getInbox(email);

        System.out.println(inbox);
        for (Mail mail : inbox.getInbox())
            out.println(mail.getId()+"|"
                    +mail.getFrom()+"|"
                    +mail.getTo()+"|"
                    +mail.getSubject()+"|"
                    +mail.getBody()+"|"+mail.getDate()+"|");
        out.println("END");
    }

    private void handleDeleteMail(String email, long id) {
        controller.deleteMail(email, id);
        out.println("MAIL_DELETED");
    }
}