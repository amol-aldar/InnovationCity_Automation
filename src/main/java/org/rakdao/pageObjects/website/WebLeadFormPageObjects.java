package org.rakdao.pageObjects.website;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.CounterUtil;
import org.rakdao.utils.PhoneUtil;
import org.rakdao.utils.ReusableUtil;
import org.rakdao.utils.WriteToExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class WebLeadFormPageObjects extends ReusableUtil {
    private String email;
    private String phone;

    private static final Logger logger = LoggerFactory.getLogger(WebLeadFormPageObjects.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // ====== Locators ======
    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(css = "#phone")
    private WebElement mobileInput;

    @FindBy(id = "enquiryType")
    private WebElement enquiryDropdown;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(css = ".thankyou-content h1")
    private WebElement successMsg;

    @FindBy(css = ".thankyou-content a")
    private WebElement backToHomeLink;

    @FindBy(css = "div.header-bar a.contact-button")
    private WebElement getStartedButtonEle;

    // ====== Constructor ======
    public WebLeadFormPageObjects(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ====== Actions ======
    public void setFirstName(String firstName) {
        waitForVisibility(firstNameInput).clear();
        firstNameInput.sendKeys(firstName);
        logger.info("✅ Entered First Name: {}", firstName);
    }

    public void setLastName(String lastName) {
        waitForVisibility(lastNameInput).clear();
        lastNameInput.sendKeys(lastName);
        logger.info("✅ Entered Last Name: {}", lastName);
    }

    public void setEmail(String email) {
        waitForVisibility(emailInput).clear();
        emailInput.sendKeys(email);
        logger.info("✅ Entered Email: {}", email);
    }

    public void setMobile(String mobile) {
        waitForVisibility(mobileInput).clear();
        mobileInput.sendKeys(mobile);
        logger.info("✅ Entered Mobile: {}", mobile);
    }

    public void selectEnquiryType(String enquiryType) {
        Select select = new Select(waitForVisibility(enquiryDropdown));
        select.selectByVisibleText(enquiryType);
        logger.info("✅ Selected Enquiry Type: {}", enquiryType);
    }

    public void submitForm() {
        scrollAndClick(submitButton);
        logger.info("✅ Submitted the form");
    }

    /**
     * Handles submissions for different URLs.
     * - "launchwith" → runs ONCE using Landing Page
     * - "utm" / "staging" → runs for ALL enquiry types
     */
    public boolean submitLeadForAllEnquiryTypes(String firstName, String lastName) {
        String currentUrl = driver.getCurrentUrl();
        logger.info("🌐 Current URL: {}", currentUrl);

        if (currentUrl.contains("launchwith")) {
            logger.info("🚀 Running LaunchWith Landing Page submission (ONCE)");
            submitLeadWithLandingPage(firstName, lastName);
            return true;
        }

        if (currentUrl.contains("utm")) {
            logger.info("📌 UTM URL detected → Clicking Get Started button first");
            scrollAndClick(getStartedButtonEle);
        }

        // For staging forms with enquiry dropdown
        wait.until(ExpectedConditions.visibilityOf(enquiryDropdown));
        Select select = new Select(enquiryDropdown);
        List<WebElement> allOptions = select.getOptions();

        boolean allSuccess = true;

        for (int i = 1; i < allOptions.size(); i++) { // skip "Select"
            String enquiryType = allOptions.get(i).getText().trim();
            logger.info("🔄 Submitting lead for Enquiry Type: {}", enquiryType);

            int count = CounterUtil.getNextCount();
            email = "a.aldar+" + count + "@innovationcity.com";
            phone = PhoneUtil.generateRandomPhone("58");

            try {
                setFirstName(firstName);
                setLastName(lastName);
                setEmail(email);
                setMobile(phone);

                select.selectByVisibleText(enquiryType);
                submitForm();

                waitForVisibility(successMsg);
                WriteToExcel.writeRunData(
                        driver.getCurrentUrl(), enquiryType,
                        firstName, lastName, email, phone, true
                );
                logger.info("✅ Lead submitted successfully for Enquiry Type: {}", enquiryType);

                Thread.sleep(1000);
                scrollAndClick(backToHomeLink);

                // Re-init page factory after reload
                PageFactory.initElements(driver, this);

                // Refresh dropdown list for next iteration
                wait.until(ExpectedConditions.visibilityOf(enquiryDropdown));
                select = new Select(enquiryDropdown);
                allOptions = select.getOptions();

            } catch (Exception e) {
                logger.error("❌ Failed to submit lead for Enquiry Type: {}", enquiryType, e);
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    // ====== Utility ======
//    private WebElement waitUntilVisible(WebElement element) {
//        return wait.until(ExpectedConditions.visibilityOf(element));
//    }
//
//    private WebElement waitUntilClickable(WebElement element) {
//        return wait.until(ExpectedConditions.elementToBeClickable(element));
//    }

    private void scrollAndClick(WebElement element) {
        try {
            waitForVisibility(element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            waitForClickability(element).click();
        } catch (Exception e) {
            logger.warn("⚠️ Normal click failed, using JS fallback for {}", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public void submitLeadWithLandingPage(String firstName, String lastName) {
        LandingPageObject landingPage = new LandingPageObject(driver);
        int count = CounterUtil.getNextCount();
        email = "a.aldar+" + count + "@innovationcity.com";
        phone = PhoneUtil.generateRandomPhone("58");

        landingPage.setFirstName(firstName);
        landingPage.setLastName(lastName);
        landingPage.setPhoneNumber(phone);
        landingPage.setEmail(email);
        landingPage.clickCheckBox();
        landingPage.clickSubmit();

        logger.info("✅ Lead submitted successfully via LaunchWith Landing Page");
        WriteToExcel.writeRunData(
                driver.getCurrentUrl(), "LandingPage", firstName, lastName, email, phone, true
        );

        waitForVisibility(backToHomeLink);
        backToHomeLink.click();
        waitForVisibility(getStartedButtonEle);
        getStartedButtonEle.click();
        submitLeadForAllEnquiryTypes( firstName,  lastName);
    }
}
