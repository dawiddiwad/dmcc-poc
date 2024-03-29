package com.publicis.dmccpoc.utils.mailhandler;

import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailReceiver {
    private static final String POP3HOST = "pop.gmail.com";
    private static final String MAIL_STORE_TYPE = "pop3s";
    private static final String USERNAME = "dmccinboxqa@gmail.com";
    private static final String PASSWORD = "Qwerty1234$";

    public static String getLatestMessageBody(){
        Properties props = new Properties();
        String latestMessageBody;
        props.put("mail.pop3.host", POP3HOST);
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");
        props.put("mail.store.protocol", "pop3");

        Session session = Session.getInstance(props);
        try {
            Store mailStore = session.getStore(MAIL_STORE_TYPE);
            mailStore.connect(POP3HOST, USERNAME, PASSWORD);

            Folder folder = mailStore.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] emailMessages = folder.getMessages();
            if (emailMessages.length == 0){
                throw new Error("No new messages");
            }
            System.out.println("Total Messages - " + emailMessages.length);

            Message message = emailMessages[0];
            latestMessageBody = new MimeMessageParser((MimeMessage) message).parse().getPlainContent();
            if (latestMessageBody == null || latestMessageBody.equals("")){
                latestMessageBody = new MimeMessageParser((MimeMessage) message).parse().getHtmlContent();
            }

            folder.close(false);
            mailStore.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Not able to receive latest email");
        }
        return latestMessageBody;
    }
}
