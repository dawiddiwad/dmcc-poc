package com.publicis.dmccpoc.utils;

import com.microsoft.playwright.Page;
import com.publicis.dmccpoc.utils.mailhandler.BodyParser;
import com.publicis.dmccpoc.utils.mailhandler.MailReceiver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class TestData {
    public static String getRandomFirstName(){
        return "firstNm" + new Random().nextInt(999999);
    }

    public static String getRandomLastName(){
        return "lastNm" + new Random().nextInt(999999);
    }

    public static String getRandomCompanyName(){
        return "CompanyNm " + new Random().nextInt(999999);
    }

    public static String getRandomPhoneNymber(){
        return String.valueOf(new Random().nextInt(999999999));
    }

    public static String getDummyPassword(){
        return "Qwerty1234$";
    }

    public static void getUrlAndNavigateToPortalSingupUsing(Page page){
        await()
            .atMost(30000, TimeUnit.MILLISECONDS)
            .pollInSameThread()
            .until(() -> {
                BodyParser bodyParser;
                try {
                    bodyParser = new BodyParser(MailReceiver.getLatestMessageBody());
                    new URL(bodyParser.getFreezoneSignupLink());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new Error();
                } catch (Error e){
                    System.out.println("No mail received yet...");
                    return false;
                }
                page.navigate(bodyParser.getFreezoneSignupLink());
                return true;
            });
    }
}
