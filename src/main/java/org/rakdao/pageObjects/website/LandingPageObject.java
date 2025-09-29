package org.rakdao.pageObjects.website;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class LandingPageObject {
    private static final Logger logger = LoggerFactory.getLogger(LandingPageObject.class);
    private WebDriver driver;
    private WebDriverWait wait;

    public LandingPageObject(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ====== Locators ======
    @FindBy(css = "div[class*='container item'] input[name='firstName']")
    private WebElement firstNameEle;

    @FindBy(css = "div[class*='container item'] input[name='lastName']")
    private WebElement lastNameEle;

    @FindBy(css = "div[class*='container item'] input[name='phoneNumber']")
    private WebElement phoneNumberEle;

    @FindBy(css = "div[class*='container item'] input[name='email']")
    private WebElement emailEle;

    @FindBy(css = "#_R_ikannb_-form-item")
    private WebElement checkBoxEle;

    @FindBy(css = "div[class*='container item'] button[type='submit']")
    private WebElement submitButtonEle;

    // ====== Actions ======

    public void setFirstName(String firstName) {
        WebElement ele = wait.until(ExpectedConditions.visibilityOf(firstNameEle));
        ele.clear();
        ele.sendKeys(firstName);
        logger.info("✅ Entered First Name: {}", firstName);
    }

    public void setLastName(String lastName) {
        WebElement ele = wait.until(ExpectedConditions.visibilityOf(lastNameEle));
        ele.clear();
        ele.sendKeys(lastName);
        logger.info("✅ Entered Last Name: {}", lastName);
    }

    public void setPhoneNumber(String phone) {
        WebElement ele = wait.until(ExpectedConditions.visibilityOf(phoneNumberEle));
        ele.clear();
        ele.sendKeys(phone);
        logger.info("✅ Entered Phone Number: {}", phone);
    }

    public void setEmail(String email) {
        WebElement ele = wait.until(ExpectedConditions.visibilityOf(emailEle));
        ele.clear();
        ele.sendKeys(email);
        logger.info("✅ Entered Email: {}", email);
    }

    public void clickCheckBox() {
        try {
            WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(checkBoxEle));
            scrollToElement(ele);
            ele.click();
            logger.info("✅ Checkbox clicked");
        } catch (Exception e) {
            logger.warn("⚠️ Normal click failed on checkbox, trying JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkBoxEle);
        }
    }

    public void clickSubmit() {
        try {
            WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(submitButtonEle));
            scrollToElement(ele);
            ele.click();
            logger.info("✅ Submit button clicked");
        } catch (Exception e) {
            logger.warn("⚠️ Normal click failed on Submit, trying JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButtonEle);
        }
    }

    // ====== Helper ======
    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
