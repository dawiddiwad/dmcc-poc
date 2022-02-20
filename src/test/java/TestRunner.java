import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

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
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
}