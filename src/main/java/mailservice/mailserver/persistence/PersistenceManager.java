package mailservice.mailserver.persistence;

import mailservice.mailserver.model.Mail;
import mailservice.mailserver.model.Mailbox;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class PersistenceManager {
    private static final String DATA_DIR = "data";

    // mail esistente nel file sys
    public synchronized static boolean emailExists(String email) {
        email = email.toLowerCase();
        File f = new File(DATA_DIR + "/" + email + ".txt");
        return f.exists();
    }

    // Persistence methods
    public synchronized static Mailbox loadMailbox(String email) {
        Mailbox mailbox = new Mailbox(email);
        Path path = Path.of(DATA_DIR, email + ".txt");

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null)
                mailbox.addMail(getMailParsed(line));
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
        return mailbox;
    }

    public synchronized static void saveMail(String to, Mail mail) {
        for(String dest : getReceiversParsed(to)) {
            File file = new File(DATA_DIR, dest + ".txt");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) { // true = append
                bw.newLine();
                bw.write(parseMailString(mail));
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public synchronized static void saveMailbox(Mailbox mailbox) {
        File file = new File(DATA_DIR, mailbox.getEmail() + ".txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Mail mail : mailbox.getInbox()) {
                bw.newLine();
                bw.write(parseMailString(mail));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // operation methods per formattazione
    private static Mail getMailParsed(String line){
        String[] parts = line.split("\\|", -1);

        if (parts.length < 6) return null;
        Mail mail = new Mail();
        mail.setId(Long.parseLong(parts[0]));
        mail.setFrom(parts[1]);
        mail.setTo(parts[2]);
        mail.setSubject(parts[3]);
        mail.setBody(parts[4]);
        mail.setDate(LocalDateTime.parse(parts[5]));
        return mail;
    }

    public static ArrayList<String> getReceiversParsed(String receivers){
        String[] toArr = receivers.split(",");
        ArrayList<String> to = new ArrayList<>();
        Collections.addAll(to, toArr);
        return to;
    }

    private static String parseMailString(Mail mail){
        String toList = String.join(",", mail.getTo());
        return mail.getId() + "|" + mail.getFrom() + "|" + toList + "|" +
                mail.getSubject() + "|" + mail.getBody() + "|" +
                mail.getDate().toString();
    }
}