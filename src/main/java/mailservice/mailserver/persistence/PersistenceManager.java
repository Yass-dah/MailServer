package mailservice.mailserver.persistence;

import mailservice.mailserver.model.Mail;
import mailservice.mailserver.model.Mailbox;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PersistenceManager {
    private static final String DATA_DIR = "../../../../../../../Data";

    // Persistence methods
    public synchronized static Mailbox loadMailbox(String email) {
        Mailbox mailbox = new Mailbox(email);
        Path path = Path.of(DATA_DIR, email + ".txt");

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null)
                mailbox.addMail(getMailParsed(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mailbox;
    }

    public synchronized static void saveMail(Mailbox mailbox, Mail mail) {
        mailbox.addMail(mail);
        File file = new File(DATA_DIR, mailbox.getEmail() + ".txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) { // true = append
            bw.write(parseMailString(mail));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void saveMailbox(Mailbox mailbox) {
        File file = new File(DATA_DIR, mailbox.getEmail() + ".txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Mail mail : mailbox.getInbox()) {
                bw.write(parseMailString(mail));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // operation methods
    private static Mail getMailParsed(String line){
        String[] parts = line.split("\\|", -1);
        if (parts.length < 6) return null;

        Mail mail = new Mail();
        mail.setId(Long.parseLong(parts[0]));
        mail.setFrom(parts[1]);
        mail.setTo(getReceiversParsed(parts[2]));
        mail.setSubject(parts[3]);
        mail.setBody(parts[4]);
        mail.setDate(LocalDateTime.parse(parts[5]));
        return mail;
    }

    private static ArrayList<String> getReceiversParsed(String receivers){
        String[] toArr = receivers.split(",");
        ArrayList<String> to = new ArrayList<>();
        for (String t : toArr)
            to.add(t);
        return to;
    }

    private static String parseMailString(Mail mail){
        String toList = String.join(",", mail.getTo());
        return mail.getId() + "|" + mail.getFrom() + "|" + toList + "|" +
                mail.getSubject() + "|" + mail.getBody() + "|" +
                mail.getDate().toString();
    }
}