package mailservice.mailserver.model;

import java.util.ArrayList;

public class Mailbox {
    private long idCounter = 0;
    private String email;
    private ArrayList<Mail> inbox;

    // costruttore
    public Mailbox(String email) {
        this.email = email;
        this.inbox = new ArrayList<>();
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public ArrayList<Mail> getInbox() {
        return inbox;
    }

    // operation methods
    public synchronized void addMail(Mail mail) {
        if(mail == null) return;
        mail.setId(generateId());
        inbox.add(mail);
    }

    public synchronized void removeMail(long id) {
        inbox.removeIf(mail -> mail.getId() == id);
    }

    // inizializzatori
    private long generateId() {
        return idCounter++;
    }

    public void updateMaxId() {
        long max = 0;
        for (Mail mail : inbox)
            if (mail.getId() > max) max = mail.getId();
        idCounter = max + 1;
    }

    // stampa
    @Override
    public String toString() {
        return "Mailbox{" +
                "email='" + email + '\'' +
                ", inbox=" + inbox +
                '}';
    }
}