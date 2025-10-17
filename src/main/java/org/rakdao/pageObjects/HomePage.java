package org.rakdao.pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.JsonLocatorReader;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class HomePage extends ReusableUtil {

    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    public HomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("✅ HomePage initialized");
    }

    @FindBy(css = "div.override-splash")
    private WebElement loader;

    @FindBy(css = ".slds-context-bar__secondary.navCenter a")
    private List<WebElement> navigationTabs;

    @FindBy(css = "ul.branding-actions a.forceActionLink[title='New']")
    private List<WebElement> newLeadLinks;

    /**
     * Clicks on the navigation bar tab such as 'Leads', 'Accounts', etc.

    public void clickNavigationTab(String tabName) throws IOException {
        logger.info("[clickNavigationTab] Clicking navigation tab: {}", tabName);

        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/NavigationBar.json");
        BasePage navBar = new BasePage(driver, "NavigationBar");

        clickElementWithJSFallback(navBar, tabName + "Tab", wait);
        logger.info("[clickNavigationTab] ✅ Clicked '{}'", tabName);
    }*/

    /**
     * Clicks on the navigation bar tab such as 'Leads', 'Accounts', etc.
     * Optimized for speed: tries primary + first visible fallback quickly.
     */
    public void clickNavigationTab(String tabName) throws IOException {
        logger.info("[clickNavigationTab] Clicking navigation tab: {}", tabName);

        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/NavigationBar.json");
        BasePage navBar = new BasePage(driver, "NavigationBar");

        List<By> locators = navBar.getAllLocators(tabName.toLowerCase() + "Tab");

        WebElement element = null;
        for (By locator : locators) {
            try {
                element = new WebDriverWait(driver, Duration.ofSeconds(8))
                        .until(ExpectedConditions.elementToBeClickable(locator));
                logger.debug("[clickNavigationTab] Found tab '{}' using locator: {}", tabName, locator);
                break;
            } catch (Exception ignored) {}
        }

        if (element == null)
            throw new RuntimeException("Unable to find navigation tab: " + tabName);

        // Salesforce-safe JS click with visibility check
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'center'});", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            logger.info("[clickNavigationTab] ✅ Clicked '{}' via JS.", tabName);
        } catch (JavascriptException e) {
            logger.warn("[clickNavigationTab] JS click failed, retrying with Selenium click...");
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(element)).click();
        }
    }



    /*public void clickNavigationTab(String tabName) {
        logger.info("[clickNavigationTab] Attempting to click navigation tab: '{}'", tabName);
        int retryCount = 0;
        final int maxRetries = 3;

        while (retryCount < maxRetries) {
            try {
                List<WebElement> navBarTabs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.cssSelector(".slds-context-bar__secondary.navCenter a span.slds-truncate")
                ));

                WebElement targetTab = navBarTabs.stream()
                        .filter(tab -> tab.getText().trim().equalsIgnoreCase(tabName))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Navigation tab not found: " + tabName));

                scrollToElement(targetTab);

                try {
                    targetTab.click();
                    logger.info("[clickNavigationTab] ✅ Clicked navigation tab '{}'", tabName);
                } catch (ElementClickInterceptedException e) {
                    logger.warn("[clickNavigationTab] Click intercepted, using JS fallback...");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetTab);
                    logger.info("[clickNavigationTab] ✅ Clicked navigation tab '{}' via JS fallback", tabName);
                }

                return; // exit after successful click
            } catch (TimeoutException e) {
                retryCount++;
                logger.warn("[clickNavigationTab] Attempt {} failed due to timeout. Retrying...", retryCount);
                if (retryCount == maxRetries) {
                    logger.error("[clickNavigationTab] ❌ Failed after {} retries — Navigation bar not found.", maxRetries, e);
                    throw e;
                }
            } catch (NoSuchElementException e) {
                logger.error("[clickNavigationTab] ❌ Tab '{}' not found in navigation.", tabName, e);
                throw e;
            } catch (Exception e) {
                logger.error("[clickNavigationTab] ❌ Unexpected error while clicking tab '{}'", tabName, e);
                throw new RuntimeException(e);
            }
        }
    }*/





    /**
     * Clicks on the “New Lead” button from the Leads home view.
     */
    public void clickNewLeadButton() throws IOException {
        logger.info("[clickNewLead] Clicking 'New Lead' button...");

        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/LeadsActions.json");
        BasePage leadBar = new BasePage(driver, "LeadsActions");

        clickElementWithJSFallback(leadBar, "newButton", wait);
        logger.info("[clickNewLead] ✅ New Lead modal opened.");
    }

    /**
     * After clicking “New Lead”, switch control to LeadPage.
     */
    public LeadPage goToLeadPage() {
        logger.info("[goToLeadPage] Switching to LeadPage object...");
        return new LeadPage(driver);
    }
}
