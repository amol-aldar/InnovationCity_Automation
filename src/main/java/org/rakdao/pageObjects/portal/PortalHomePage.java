package org.rakdao.pageObjects.portal;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.Duration;

public class PortalHomePage extends ReusableUtil {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(PortalHomePage.class);

    @FindBy(xpath = "//button[.//span[normalize-space()='Continue with application']]")
    private WebElement contiApplButtonEle;

    @FindBy(xpath = "//button[normalize-space()='START NOW']")
    private WebElement startNowButtonEle;

    public PortalHomePage(WebDriver driver) throws AWTException {
        super(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }


    public PortalApplicationPage clickStartNowButton() throws AWTException {
        logger.info("🟢 Attempting to click 'Start Now' button...");
        safeClickWithJSFallback(startNowButtonEle, "Start Now");
        return new PortalApplicationPage(driver);
    }

    public PortalApplicationPage clickContinueWithApplication() throws AWTException {
        logger.info("🟢 Attempting to click 'Continue with application' button...");
        safeClickWithJSFallback(contiApplButtonEle, "Continue with application");
        return new PortalApplicationPage(driver);
    }
}
