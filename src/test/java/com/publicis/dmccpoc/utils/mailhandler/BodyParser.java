package com.publicis.dmccpoc.utils.mailhandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BodyParser {
    private final String mailBody;

    public BodyParser(String mailBody){
        this.mailBody = mailBody;
    }

    private String parseBodyUsingRegex(String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.mailBody);
        if (matcher.find()){
            return matcher.group(1);
        } else {
            throw new Error("Unable to parse using regex:\n" + regex +
                    "\nfrom source string:\n" + this.mailBody);
        }
    }

    public String getFreezoneCode(){
        try {
            String codePattern = "([0-9]{6})";
            return parseBodyUsingRegex(codePattern);
        } catch (Error e){
            throw new Error("Unable to parse freezone code due to:\n" + e);
        }
    }

    public String getFreezoneSignupLink(){
        try {
            String signupLinkPattern = "(https.+)";
            return parseBodyUsingRegex(signupLinkPattern);
        } catch (Error e){
            throw new Error("Unable to parse freezone signup link due to:\n" + e);
        }
    }
}
