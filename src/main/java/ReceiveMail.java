import mailreader.MailParser;
import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * This class is used to receive simple email.
 * @author w3spoint
 */
public class ReceiveMail {
    public static void receiveEmail(String pop3Host,
                                    String storeType, String user, String password){
        Properties props = new Properties();
        props.put("mail.pop3.host", pop3Host);
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");
        props.put("mail.store.protocol", "pop3");

        Session session = Session.getInstance(props);
        try {
            Store mailStore = session.getStore(storeType);
            mailStore.connect(pop3Host, user, password);

            Folder folder = mailStore.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] emailMessages = folder.getMessages();
            System.out.println("Total Message - "
                    + emailMessages.length);

            //Iterate the messages
            for (int i = 0; i < emailMessages.length; i++) {
                Message message = emailMessages[i];
                Address[] toAddress =
                        message.getRecipients(Message.RecipientType.TO);
                System.out.println();
                System.out.println("Email " + (i+1) + "-");
                System.out.println("Subject - " + message.getSubject());
                System.out.println("From - " + message.getFrom()[0]);

                if (message.isMimeType("multipart/alternative")){
                    MailParser mailParser = new MailParser(new MimeMessageParser((MimeMessage) message).parse().getPlainContent());
//                    System.out.println("freezone code is: " + mailParser.getFreezoneCode());
                    System.out.println("freezone signup link is: " + mailParser.getFreezoneSignupLink());

                    mailParser = new MailParser(new MimeMessageParser((MimeMessage) message).parse().getHtmlContent());
//                    System.out.println("freezone code is: " + mailParser.getFreezoneCode());
                    System.out.println("freezone signup link is: " + mailParser.getFreezoneSignupLink());

                    System.out.println(new MimeMessageParser((MimeMessage) message).parse().getPlainContent());
                    System.out.println(new MimeMessageParser((MimeMessage) message).parse().getHtmlContent());
                } else {
                    System.out.println("Text - "
                        + message.getContent().toString());
                }

                System.out.println("To - ");
                for (Address address : toAddress) {
                    System.out.println(address.toString());
                }
            }

            folder.close(false);
            mailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in receiving email.");
        }
    }

    public static void main(String[] args) {
        String pop3Host = "pop.gmail.com";
        String mailStoreType = "pop3s";
        final String userName = "dmccinboxqa@gmail.com";
        final String password = "Qwerty1234$";

        //call receiveEmail
        receiveEmail(pop3Host, mailStoreType, userName, password);
    }
}

//    Your confirmation code is below
//
//        407000
//
//        Enter it in your open browser window and we will help you get signed in.
//
//        Best regards,
//        DMCC
//
//        <div dir="ltr"><div id="gmail-m_-3096846049198372106divRplyFwdMsg" dir="ltr"><div> </div></div><div>Your confirmation code is below<br><br>407000<br><br>Enter it in your open browser window and we will help you get signed in.<br><br>Best regards,<br>DMCC</div></div>

//Hi,
//
//        Use this link to Signup to Freezone portal.
//        https://publicis-dmccpoc.cs58.force.com/ncascreenflow/s/freezonesignup?id=0010l00001PPQ6a
//
//        Best regards,
//
//        DMCC
//
//<div dir="ltr"><span style="color:rgb(80,0,80)">Hi,</span><br style="color:rgb(80,0,80)"><br style="color:rgb(80,0,80)"><span style="color:rgb(80,0,80)">Use this link to Signup to Freezone portal.</span><br style="color:rgb(80,0,80)"><a href="https://publicis-dmccpoc.cs58.force.com/ncascreenflow/s/freezonesignup?id=0010l00001PPQ6a" target="_blank">https://publicis-dmccpoc.cs58.force.com/ncascreenflow/s/freezonesignup?id=0010l00001PPQ6a</a><a><br><br>Best regards,<br><br>DMCC</a><br></div>
