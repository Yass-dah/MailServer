package mailservice.mailserver.controller;

import mailservice.mailserver.persistence.PersistenceManager;
import mailservice.mailserver.model.*;

public class PersistenceController {
    private AccountList accounts;

    // costruttore
    public PersistenceController() {
        accounts = new AccountList();
    }

    // Getters & Setters
    public AccountList getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountList accounts) {
        this.accounts = accounts;
    }

    public boolean emailExists(String email) {
        return PersistenceManager.emailExists(email);
    }

    public void loadMailbox(String email) {
        Mailbox mailbox = PersistenceManager.loadMailbox(email);
        accounts.getInboxList().put(mailbox.getEmail(), mailbox);
    }

    public void saveMailboxes() {
        for(Mailbox inbox : accounts.getInboxList().values())
            PersistenceManager.saveMailbox(inbox);
    }

    public void saveMailbox(String email) {
        PersistenceManager.saveMailbox(accounts.getInbox(email));
    }

    public void saveMail(String emails, Mail mail) {
        for(String dest : PersistenceManager.getReceiversParsed(emails))
            accounts.getInbox(dest).addMail(mail);
        PersistenceManager.saveMail(emails, mail);
    }

    public void deleteMail(String email, long mailId) {
        accounts.getInboxList().get(email).removeMail(mailId);
        PersistenceManager.saveMailbox(accounts.getInbox(email));
    }

    public void print(){
        for(String email : accounts.getInboxList().keySet())
            System.out.println(accounts.getInbox(email));
    }
}
