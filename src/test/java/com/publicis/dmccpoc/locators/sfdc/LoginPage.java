package com.publicis.dmccpoc.locators.sfdc;

import com.microsoft.playwright.Page;

public class LoginPage {
    public enum Environment {
        QA,
        UAT,
        PROD
    }

    public static final String TEST_URL = "https://test.salesforce.com/";
    public static final String PRODUCTION_URL = "https://login.salesforce.com/";

    public static final String USERNAME = "//input[@id='username']";
    public static final String PASSWORD = "//input[@id='password']";

    public static final String LOGIN_BUTTON = "//input[@id='Login']";

    private static void login(Page page, String url, String username, String password){
        page.navigate(url);
        page.fill(USERNAME, username);
        page.fill(PASSWORD, password);
        page.click(LOGIN_BUTTON);
    }

    public static void authenticateUsing(Page page, Environment environment){
        switch (environment){
            case QA:
                login(page, TEST_URL ,"sys.admin@dmcc.qa", "Salesforce@4");
                break;
            case UAT:
                login(page, TEST_URL ,"sanchit.mittal@dmcc.uat", "Sapient@23");
                break;
            case PROD:
                login(page, PRODUCTION_URL, "placeholder", "placeholder");
            default: break;
        }
    }
}
