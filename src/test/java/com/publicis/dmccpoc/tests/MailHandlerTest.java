package com.publicis.dmccpoc.tests;

import com.publicis.dmccpoc.utils.mailhandler.BodyParser;
import com.publicis.dmccpoc.utils.mailhandler.MailReceiver;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MailHandlerTest {
    @Test
    void shouldParseCodeFromEmail() {
        BodyParser bodyParser = new BodyParser(MailReceiver.getLatestMessageBody());
        assertTrue(Integer.parseInt(bodyParser.getFreezoneCode()) > 100000);
    }

    @Test
    void shouldParseSignupLinkFromEmail() {
        BodyParser bodyParser = new BodyParser(MailReceiver.getLatestMessageBody());
        try {
            new URL(bodyParser.getFreezoneSignupLink());
            System.out.println(bodyParser.getFreezoneSignupLink());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
}
