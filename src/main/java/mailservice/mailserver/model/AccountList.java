package mailservice.mailserver.model;

import java.util.HashMap;
import java.util.Map;

public class AccountList {
    private Map<String, Mailbox> inboxList;

    // costruttore
    public AccountList() {
        inboxList = new HashMap<>();
    }

    // Getter
    public Map<String, Mailbox> getInboxList() {
        return inboxList;
    }

    // operation methods
    public boolean addAccount(String email){
        return inboxList.putIfAbsent(email, new Mailbox(email)) == null;
    }

    public Mailbox deleteAccount(String email){
        return inboxList.remove(email);
    }

    public boolean containsAccount(String email){
        return inboxList.containsKey(email);
    }

    public Mailbox getInbox(String email){
        return inboxList.get(email);
    }

    @Override
    public String toString() {
        return "AccountList{" +
                "inboxList=" + inboxList +
                '}';
    }
}
