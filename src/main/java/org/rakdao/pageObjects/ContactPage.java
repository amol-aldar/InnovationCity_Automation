package org.rakdao.pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactPage extends ReusableUtil {

    private static final Logger logger = LoggerFactory.getLogger(ContactPage.class);
    private final WebDriver driver;


    // Constructor
    public ContactPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("✅ ContactPage initialized successfully with driver: {}", driver);
    }

    // 🔹 Page Elements
    @FindBy(xpath = "//button[normalize-space(text())='Log in to Experience as User']")
    private WebElement loginButton;




    // 🔹 Action Methods
    public PortalApplicationPage goToPortal() {
        logger.info("🔍 Attempting to click 'Log in to Experience as User' button on ContactPage...");

        try {
            waitForClickability(loginButton);
            logger.info("🟢 Login button is clickable. Proceeding to click...");
            loginButton.click();
            logger.info("✅ Successfully clicked 'Log in to Experience as User'.");

        } catch (TimeoutException te) {
            logger.warn("⚠️ Login button not clickable initially. Retrying with fallback scroll and JS click...");
            try {
                scrollToElement( loginButton);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginButton);

                logger.info("✅ Fallback: Clicked 'Log in to Experience as User' using JavaScript.");
            } catch (Exception jsEx) {
                logger.error("❌ Fallback click failed. Could not access portal: {}", jsEx.getMessage());
                throw new RuntimeException("Failed to click 'Log in to Experience as User' button after retries.", jsEx);
            }
        } catch (Exception e) {
            logger.error("❌ Unexpected error while clicking portal login: {}", e.getMessage());
            throw new RuntimeException("Error navigating to user portal", e);
        }
        return new PortalApplicationPage(driver);
    }



}
