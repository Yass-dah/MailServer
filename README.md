# MailServer
A simple mail server developed with JavaFX to handle sending mails, inbox operations between different clients connected with java socket communication and simple file system with no database ([data](/data) each email inbox is a .txt file).

This project is managed with Maven. To run it:
```
mvn clean javafx:run
```

It works as its purpose only if you have the MailClient one installed [https://github.com/Yass-dah/MailClient].

### Possible ameliorations to this project: 
-  lock on each personal inbox to improve communication and decrease overhead
-  email domain registration
-  credentials on login
-  implement IMAP/SMTP communication protocol



##### Warning: always check address and port of socket(default=<localhost, 50000>) before running server or client

