import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRunner {
    // Shared between all tests in this class.
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //browser = playwright.webkit().launch();
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/")));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void shouldClickButton() {
        page.navigate("data:text/html,<script>var result;</script><button onclick='result=\"Clicked\"'>Go</button>");
        page.click("button");
        assertEquals("Clicked", page.evaluate("result"));
    }

    @Test
    void shouldCheckTheBox() {
        page.setContent("<input id='checkbox' type='checkbox'></input>");
        page.check("input");
        assertTrue((Boolean) page.evaluate("() => window['checkbox'].checked"));
    }

    @Test
    void shouldSearchWiki() {
        page.navigate("https://en.wikipedia.org/");
        page.click("input[name=\"search\"]");
        page.fill("input[name=\"search\"]", "playwright");
        page.press("input[name=\"search\"]", "Enter");
        page.click("//a[@title = 'Playwright' and descendant::span[@class = 'searchmatch']]");
        page.waitForLoadState();
        assertEquals("https://en.wikipedia.org/wiki/Playwright", page.url());
    }

    @Test
    void shouldCreateLeadAndReceiveCode(){
        page.navigate("https://test.salesforce.com/");
        page.fill("//input[@id='username']", "sys.admin@dmcc.qa");
        page.fill("//input[@id='password']", "Salesforce@3");
        page.click("//input[@id='Login']");
        //page.waitForLoadState(LoadState.NETWORKIDLE);

        page.click("//button[descendant::*[contains(text(), 'App Launcher')]]");
//        page.click("//button[descendant::*[contains(text(), 'App Launcher')]]", new Page.ClickOptions().setDelay(2000));
        page.fill("//input[contains(@type, 'search') and ancestor::one-app-launcher-menu]", "Leads");
        page.click("//one-app-launcher-menu-item[descendant::*[@*='Leads']]");

        page.click("//div[@title='New']");
        page.click("//span[text()='Next']");

        page.click("(//button[ancestor::div[preceding-sibling::label[text()='Lead Source']]])[1]");
        page.click("//lightning-base-combobox-item[@data-value='Web']");

        page.click("(//button[ancestor::div[preceding-sibling::label[text()='Origin Country']]])[1]");
        page.click("//lightning-base-combobox-item[@data-value='Afghanistan']");

        page.click("(//button[ancestor::div[preceding-sibling::label[text()='Company Type']]])[1]");
        page.click("//lightning-base-combobox-item[@data-value='New Company']");

        page.click("(//button[ancestor::div[preceding-sibling::label[text()='Activity Type']]])[1]");
        page.click("//lightning-base-combobox-item[@data-value='Service']");

        page.click("(//button[ancestor::div[preceding-sibling::label[text()='Company Setup']]])[1]");
        page.click("//lightning-base-combobox-item[@data-value='Immediately']");

        page.click("(//button[ancestor::div[preceding-sibling::label[text()='How did you hear about us (1)']]])[1]");
        page.click("//lightning-base-combobox-item[@data-value='Advertising / News / Editorial']");

        page.click("(//button[ancestor::div[preceding-sibling::label[text()='How did you hear about us (2)']]])[1]");
        page.click("//lightning-base-combobox-item[@data-value='Email Advertising']");

        page.click("(//button[ancestor::div[preceding-sibling::label[text()='Address Country']]])[1]");
        page.click("(//lightning-base-combobox-item[@data-value='Afghanistan'])[2]");

        page.fill("//input[@name='firstName']", "DMCC automation poc " + new Date());
        page.fill("//input[@name='lastName']", "DMCC automation poc " + new Date());

        page.fill("//input[@name='Email']", "dmccinboxqa@gmail.com");
        page.fill("//input[@name='Company']", "DMCC automation poc " + new Date());

        page.fill("//input[@name='Country_code__c']", "92");
        page.fill("//input[@name='Area_code__c']", "92");
        page.fill("//input[@name='Phone_No__c']", String.valueOf(Math.random()*10));

        page.fill("(//textarea[ancestor::div[preceding-sibling::label[text()='Description']]])[1]", "QA services");

        page.click("//button[@name='SaveEdit']");

        page.click("//button[@name='Convert']");

        page.click("//button[descendant::span[text()='Convert']]", new Page.ClickOptions().setDelay(2000));
        page.click("(//a[@data-refid='recordId' and ancestor::div[contains(@class, 'primaryField')] and ancestor::div[descendant::div[text()='Opportunity']]])[last()]");

        page.click("(//button[@name='Edit'])[last()]");
        page.fill("//input[ancestor::div[preceding-sibling::label[text()='SR Template']]]", "201");
        page.click("//input[ancestor::div[preceding-sibling::label[text()='SR Template']]]");
        page.click("//lightning-base-combobox-formatted-text[@title='201-New Company Application L2L']");
        page.fill("//input[@name='Lead_Email__c']", "dmccinboxqa@gmail.com");
        page.click("//button[@name='SaveEdit']");

        page.click("//button[descendant::span[text()='Change Closed Stage']]");
        page.click("//button[@name='StageName']");
        page.click("//lightning-base-combobox-item[descendant::span[text()='Convincing Customer']]");
        page.click("//button[text()='Done']");

        await()
                .atMost(30000, TimeUnit.MILLISECONDS)
                .pollInSameThread()
                .until(() -> {
                    BodyParser bodyParser = new BodyParser(MailReceiver.getLatestMessageBody());
                    try {
                        new URL(bodyParser.getFreezoneSignupLink());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        throw new Error();
                    } catch (Error e){
                        System.out.println("No mail received yet...");
                    }
                    page.navigate(bodyParser.getFreezoneSignupLink());
                    return true;
                });

        page.fill("//input[@name='inpPassword']", "Qwerty1234$");
        page.fill("//input[@name='inpConfirmPassword']", "Qwerty1234$");
        page.click("//button[text()='Sign up']");

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/SignupPortal.png")));
    }

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