package mailreader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailParser {
    private final String mailBody;

    public MailParser(String mailBody){
        this.mailBody = mailBody;
    }

    private String parseUsingRegex(String regex, String source){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()){
            return matcher.group(1);
        } else {
            throw new Error("Unable to parse using regex:\n" + regex +
                    "\nfrom source string:\n" + this.mailBody);
        }
    }

    public String getFreezoneCode(){
        try {
            return parseUsingRegex("([0-9]{6})", this.mailBody);
        } catch (Error e){
            throw new Error("Unable to parse freezone code due to:\n" + e);
        }
    }

    public String getFreezoneSignupLink(){
        try {
            return parseUsingRegex("(https.+)", this.mailBody);
        } catch (Error e){
            throw new Error("Unable to parse freezone signup link due to:\n" + e);
        }
    }
}
