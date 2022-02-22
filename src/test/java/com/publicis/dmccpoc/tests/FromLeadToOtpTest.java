package com.publicis.dmccpoc.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.publicis.dmccpoc.locators.portal.PortalPassword;
import com.publicis.dmccpoc.locators.sfdc.*;
import com.publicis.dmccpoc.locators.sfdc.editview.Lead;
import com.publicis.dmccpoc.locators.sfdc.editview.Opportunity;
import com.publicis.dmccpoc.utils.TestData;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

public class FromLeadToOtpTest {
    static Playwright playwright;
    static Browser browser;

    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions()
            .setScreenSize(1366, 768)
            .setRecordVideoDir(Paths.get("videos/"))
            .setRecordVideoSize(1366, 768));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void shouldCreateLeadAndReceiveCode(){
        LoginPage.authorizeOn(page, LoginPage.Environment.QA);

        page.click(NavigationBar.APP_LAUNCHER);
        page.fill(NavigationBar.APP_LAUNCHER_SEARCH_FIELD, "Leads");
        page.click(NavigationBar.getAppLauncherSearchResultsItemByLabel("Leads"));

        page.click(ListView.NEW_BUTTON);
        page.click(Modal.NEXT_BUTTON);

        Lead.fillPicklistWithValue(page, "Lead Source", "Web");
        Lead.fillPicklistWithValue(page, "Origin Country", "Afghanistan");
        Lead.fillPicklistWithValue(page, "Company Type", "New Company");
        Lead.fillPicklistWithValue(page, "Activity Type", "Service");
        Lead.fillPicklistWithValue(page, "Company Setup", "Immediately");
        Lead.fillPicklistWithValue(page, "How did you hear about us (1)", "Advertising / News / Editorial");
        Lead.fillPicklistWithValue(page, "How did you hear about us (2)", "Email Advertising");
        Lead.fillPicklistWithValue(page, "Address Country", "Afghanistan");

        page.fill(Lead.FIRST_NAME, TestData.getRandomFirstName());
        page.fill(Lead.LAST_NAME, TestData.getRandomLastName());
        page.fill(Lead.EMAIL, "dmccinboxqa@gmail.com");
        page.fill(Lead.COMPANY, TestData.getRandomCompanyName());
        page.fill(Lead.COUNTRY_CODE, "92");
        page.fill(Lead.AREA_CODE, "92");
        page.fill(Lead.PHONE_NUMBER, TestData.getRandomPhoneNymber());
        page.fill(Lead.DESCRIPTION, "QA services");
        page.click(Lead.SAVE_BUTTON);

        page.click(HighlightsPanel.CONVERT_BUTTON);
        page.click(LeadConvert.CONVERT_BUTTON, new Page.ClickOptions().setDelay(2000));
        page.click(LeadConvert.OPPORTUNITY_LINK);

        page.click(HighlightsPanel.EDIT_BUTTON);
        Opportunity.selectItemOnLookup(page, "SR Template", "201-New Company Application L2L");
        page.fill(Opportunity.LEAD_EMAIL, "dmccinboxqa@gmail.com");
        page.click(Opportunity.SAVE_BUTTON);

        page.click(StagesPath.CHANGE_CLOSED_STAGE_BUTTON);
        StagesPath.fillPicklistWithValue(page, "StageName", "Convincing Customer");
        page.click(StagesPath.DONE_BUTTON);

        TestData.getUrlAndNavigateToPortalSingupUsing(page);

        page.fill(PortalPassword.PASSWORD, TestData.getDummyPassword());
        page.fill(PortalPassword.PASSWORD_CONFIRM, TestData.getDummyPassword());
        page.click(PortalPassword.SIGNUP_BUTTON);

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/SignupPortal.png")));
    }
}