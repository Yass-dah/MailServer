package mailservice.mailserver.model;

import java.util.ArrayList;

public class Mailbox {
    private long IdCounter = 0;
    private String email;
    private ArrayList<Mail> inbox;

    // costruttore
    public Mailbox(String email) {
        this.email = email;
        this.inbox = new ArrayList<>();
    }

    // Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Mail> getInbox() {
        return inbox;
    }

    public void setInbox(ArrayList<Mail> inbox) {
        this.inbox = inbox;
    }

    // inbox operation methods
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
        return IdCounter++;
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
