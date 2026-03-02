package mailservice.mailserver.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Mail {
    private long id;
    private String from;
    private ArrayList<String> to;
    private String subject;
    private String body;
    private LocalDateTime date;

    // costruttori
    public Mail() {}

    protected Mail(long id, String from, ArrayList<String> to, String subject, String body, LocalDateTime date) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.date = date;
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public ArrayList<String> getTo() {
        return to;
    }

    public void setTo(ArrayList<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // inizializzatori
    public void initDateNow() {
        setDate(LocalDateTime.now());
    }

    // stampa
    @Override
    public String toString() {
        return subject + " FROM " + from;
    }
}
