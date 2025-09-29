package org.rakdao;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.rakdao.pageObjects.website.WebLeadFormPageObjects;
import org.rakdao.utils.User;
import org.rakdao.utils.UserGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class WebLeadFormAutomation {

    private static final Logger logger = LoggerFactory.getLogger(WebLeadFormAutomation.class);
    WebDriver driver;
    WebLeadFormPageObjects leadFormPage;

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(dataProvider = "urls")
    public void leadFormTest(String url) {
        User user = UserGenerator.generateUser();
        logger.info("🌐 Starting lead submission for URL: {}", url);

        driver.get(url);
        leadFormPage = new WebLeadFormPageObjects(driver);

        boolean allSuccess = leadFormPage.submitLeadForAllEnquiryTypes(
                user.getFirstName(), user.getLastName()
        );

        if (allSuccess) {
            logger.info("✅ All leads submitted successfully for URL: {}", url);
        } else {
            logger.error("❌ Some leads failed for URL: {}", url);
        }

        Assert.assertTrue(allSuccess, "❌ Some enquiry types failed during lead submission at " + url);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("🛑 Browser closed after execution");
        }
    }

    @DataProvider(name = "urls")
    public Object[][] getURL() {
        return new Object[][]{
                {"https://launchwith-staging.innovationcity.com/"},
                {"https://launchwith-staging.innovationcity.com/?utm_medium=cpc&utm_campaign=remarketing"},
                {"https://staging.innovationcity.com/#contact"},
                {"https://staging.innovationcity.com/?utm_source=fb-ig&utm_medium=paid-social&utm_campaign=remarketing"}
        };
    }
}
