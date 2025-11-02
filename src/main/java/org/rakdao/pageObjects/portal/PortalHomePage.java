package org.rakdao.pageObjects.portal;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PortalHomePage extends ReusableUtil {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(PortalHomePage.class);

    @FindBy(xpath = "//button[.//span[normalize-space()='Continue with application']]")
    private WebElement contiApplButton;

    @FindBy(xpath = "//button[.//span[normalize-space()='START NOW']]")
    private WebElement startNowButton;

    public PortalHomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }


     // Clicks on the "Continue with application" button safely.
     public PortalApplicationPage clickStartNowButton() {
         logger.info("🟢 Attempting to click 'Start Now' button...");

//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
         JavascriptExecutor js = (JavascriptExecutor) driver;

         try {
             // 1️⃣ Wait until button appears
             waitForVisibility(startNowButton);
             waitForClickability(startNowButton);
             scrollToElement(startNowButton);

             // 2️⃣ Verify the element is enabled and displayed
             if (!startNowButton.isDisplayed() || !startNowButton.isEnabled()) {
                 logger.warn("⚠️ 'Start Now' button is not fully ready (displayed={}, enabled={})",
                         startNowButton.isDisplayed(), startNowButton.isEnabled());
                 waitForClickability(startNowButton);
             }

             // 3️⃣ Try standard click
             try {
                 startNowButton.click();
                 logger.info("✅ Successfully clicked 'Start Now' button using standard click.");
                 return null;
             } catch (ElementClickInterceptedException e) {
                 logger.warn("⚠️ Standard click intercepted — retrying with JavaScript click...");
                 js.executeScript("arguments[0].click();", startNowButton);
                 logger.info("✅ Clicked 'Start Now' button using JavaScript fallback.");
                 return null;
             }

         } catch (TimeoutException te) {
             logger.error("❌ Timeout while waiting for 'Start Now' button: {}", te.getMessage());
             throw new RuntimeException("Start Now button not clickable or not found.", te);

         } catch (StaleElementReferenceException se) {
             logger.warn("♻️ StaleElementReferenceException caught — retrying lookup for 'Start Now' button...");
             try {
                 WebElement refreshedButton = driver.findElement(By.xpath("//button[contains(.,'Start Now')]"));
                 js.executeScript("arguments[0].scrollIntoView({block:'center'});", refreshedButton);
                 js.executeScript("arguments[0].click();", refreshedButton);
                 logger.info("✅ Successfully clicked 'Start Now' button after stale element refresh.");
             } catch (Exception e2) {
                 logger.error("❌ Retry failed after stale element refresh: {}", e2.getMessage());
                 throw new RuntimeException("Failed to click Start Now after retry.", e2);
             }

         } catch (Exception e) {
             logger.error("❌ Unexpected error while clicking 'Start Now' button: {}", e.getMessage(), e);
             throw new RuntimeException("Error clicking Start Now button.", e);
         }

         return new PortalApplicationPage(driver);
     }

    public PortalApplicationPage clickContinueWithApplication() {
        try {
            waitForClickability(contiApplButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", contiApplButton);
            contiApplButton.click();
            logger.info("✅ Clicked on 'Continue with application' button successfully.");
        } catch (ElementClickInterceptedException e) {
            logger.warn("⚠️ Normal click intercepted, trying JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", contiApplButton);
            logger.info("✅ JS click successful on 'Continue with application' button.");
        } catch (Exception e) {
            logger.error("❌ Failed to click 'Continue with application' button: {}", e.getMessage());
            throw e;
        }
        return new  PortalApplicationPage(driver);
    }

}
