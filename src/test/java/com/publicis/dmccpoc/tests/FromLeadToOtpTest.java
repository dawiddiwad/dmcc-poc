package com.publicis.dmccpoc.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.publicis.dmccpoc.locators.portal.PortalPassword;
import com.publicis.dmccpoc.locators.sfdc.*;
import com.publicis.dmccpoc.locators.sfdc.editview.Lead;
import com.publicis.dmccpoc.locators.sfdc.editview.Opportunity;
import com.publicis.dmccpoc.utils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Paths;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class FromLeadToOtpTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void createContextAndPage() {
        playwright = Playwright.create();
        browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions()
            .setScreenSize(1366, 768)
            .setRecordVideoDir(Paths.get("videos/"))
            .setRecordVideoSize(1366, 768));
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("traces/trace - " + new Date().getTime() +".zip")));
        context.close();
        playwright.close();
    }

    @RepeatedTest(1)
    void shouldCreateLeadAndReceiveCode(){
        //Authenticate SFDC sandbox
        LoginPage.authenticateUsing(page, LoginPage.Environment.QA);

        //Navigate to Leads listview
        page.click(NavigationBar.APP_LAUNCHER);
        page.fill(NavigationBar.APP_LAUNCHER_SEARCH_FIELD, "Leads");
        page.click(NavigationBar.getAppLauncherSearchResultsItemByLabel("Leads"));

        //Create new Lead
        page.click(ListView.NEW_BUTTON);
        page.click(Modal.NEXT_BUTTON);

        //Handle picklists
        Lead.fillPicklistWithValue(page, "Lead Source", "Web");
        Lead.fillPicklistWithValue(page, "Origin Country", "Afghanistan");
        Lead.fillPicklistWithValue(page, "Company Type", "New Company");
        Lead.fillPicklistWithValue(page, "Activity Type", "Service");
        Lead.fillPicklistWithValue(page, "Company Setup", "Immediately");
        Lead.fillPicklistWithValue(page, "How did you hear about us (1)", "Advertising / News / Editorial");
        Lead.fillPicklistWithValue(page, "How did you hear about us (2)", "Email Advertising");
        Lead.fillPicklistWithValue(page, "Address Country", "Afghanistan");

        //Fill out rest of mandatory fields and save Lead
        page.fill(Lead.FIRST_NAME, TestData.getRandomFirstName());
        page.fill(Lead.LAST_NAME, TestData.getRandomLastName());
        page.fill(Lead.EMAIL, TestData.getDummyEmailAddress());
        page.fill(Lead.COMPANY, TestData.getRandomCompanyName());
        page.fill(Lead.COUNTRY_CODE, "92");
        page.fill(Lead.AREA_CODE, "92");
        page.fill(Lead.PHONE_NUMBER, TestData.getRandomPhoneNymber());
        page.fill(Lead.DESCRIPTION, "QA services");
        page.click(Lead.SAVE_BUTTON);

        //Convert Lead
        page.click(HighlightsPanel.CONVERT_BUTTON);
        page.click(LeadConvert.CONVERT_BUTTON, new Page.ClickOptions().setDelay(2000));
        page.click(LeadConvert.OPPORTUNITY_LINK);

        //Edit Opportunity
        page.click(HighlightsPanel.EDIT_BUTTON);
        Opportunity.selectItemOnLookup(page, "SR Template", "201-New Company Application L2L");
        page.fill(Opportunity.LEAD_EMAIL, TestData.getDummyEmailAddress());
        page.click(Opportunity.SAVE_BUTTON);

        //Save Opportunity as a 'Convincing Customer'
        page.click(StagesPath.CHANGE_CLOSED_STAGE_BUTTON);
        StagesPath.fillPicklistWithValue(page, "StageName", "Convincing Customer");
        page.click(StagesPath.DONE_BUTTON);

        //fetch Freezone Portal signup link and navigate to it
        TestData.getUrlAndNavigateToPortalSingupUsing(page);

        //Fill out passwords and proceed to the OTP page
        page.fill(PortalPassword.PASSWORD, TestData.getDummyPassword());
        page.fill(PortalPassword.PASSWORD_CONFIRM, TestData.getDummyPassword());
        page.click(PortalPassword.SIGNUP_BUTTON);

        //Take screenshot on OTP page
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/SignupPortal.png")));
    }
}