package mailservice.mailserver.controller;

import mailservice.mailserver.persistence.PersistenceManager;
import mailservice.mailserver.model.*;

public class PersistenceController {
    private AccountList accounts;

    // costruttore
    public PersistenceController() {
        accounts = new AccountList();
    }

    public void loadAllMailboxes() {
        for (String email : accounts.getInboxList().keySet()) {
            Mailbox mailbox = PersistenceManager.loadMailbox(email);
            accounts.getInboxList().put(email, mailbox);
        }
    }

    public void saveMailboxes() {
        for(Mailbox inbox : accounts.getInboxList().values())
            PersistenceManager.saveMailbox(inbox);
    }

    public void saveMailbox(String email) {
        PersistenceManager.saveMailbox(accounts.getInbox(email));
    }

    public void saveMail(String email, Mail mail) {
        accounts.getInbox(email).addMail(mail);
        PersistenceManager.saveMail(accounts.getInbox(email), mail);
    }

    public void deleteMail(String email, long mailId) {
        accounts.getInboxList().get(email).removeMail(mailId);
        PersistenceManager.saveMailbox(accounts.getInbox(email));
    }
}
