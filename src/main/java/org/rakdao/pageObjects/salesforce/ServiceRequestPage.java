package org.rakdao.pageObjects.salesforce;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class ServiceRequestPage extends ReusableUtil {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestPage.class);
    private final WebDriver driver;
    WebDriverWait wait;

    @FindBy(xpath="//input[@type='radio' and contains(@name,'Suggested_Company_Names')]")
    private List<WebElement> suggestedComapnyNames;

    By approverDecisionDropdown = By.xpath("//select[@name='Approver_s_Decision']");





    // Constructor
    public ServiceRequestPage(WebDriver driver) throws AWTException {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        logger.info("✅ ServiceRequestPage initialized successfully with driver: {}", driver);
    }

    public ContactPage goToContactOrAccount(String headerLabel) throws AWTException {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);

        logger.info("🔄 Navigating to {} page by header label: '{}'",
                headerLabel.contains("Contact") ? "Contact" : "Account", headerLabel);

        try {

            // Dynamic locator for Contact / Account link
            String linkXpath = "//p[@class='slds-text-title slds-truncate' and normalize-space(text())='"
                    + headerLabel + "']/following-sibling::p//a";

            WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(linkXpath)));
            scrollToElement(linkElement);

            logger.info("🔗 Link found for '{}'. Opening in new tab...", headerLabel);

            // === OPEN IN NEW TAB ===
            actions.keyDown(Keys.CONTROL)
                    .click(linkElement)
                    .keyUp(Keys.CONTROL)
                    .build()
                    .perform();

            logger.info("🆕 Link opened in new tab for '{}'", headerLabel);

            // === SWITCH TO NEW TAB ===
            switchToWindowByIndex(1);

            logger.info("🪟 Successfully switched to new tab for '{}'", headerLabel);

        } catch (Exception e) {
            logger.error("❌ Error navigating to {} page: {}", headerLabel, e.getMessage(), e);
            throw new RuntimeException("Failed to open " + headerLabel + " in new tab", e);
        }

        return new ContactPage(driver);
    }

    public void expandBackOfficeAccordion(String accordionText) {
        driver.navigate().refresh();
        try {
            String xpath = String.format(
                    "//button[contains(@class,'slds-accordion__summary-action')]" +
                            "[.//span[contains(normalize-space(),'%s')]]",
                    accordionText
            );

            WebElement accordionElement = new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

            // 1️⃣ Scroll into view
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                    accordionElement
            );

            // 2️⃣ Short pause for smooth scroll (important for Salesforce UI)
            Thread.sleep(300);

            // 3️⃣ Wait until clickable
            wait.until(ExpectedConditions.elementToBeClickable(accordionElement));

            // 4️⃣ Verify expand state
            String isExpanded = accordionElement.getAttribute("aria-expanded");

            if ("false".equals(isExpanded)) {
                logger.info("🔽 Collapsed → expanding accordion '{}'", accordionText);

                // 5️⃣ Click using Actions for better reliability
                Actions actions = new Actions(driver);
                actions.moveToElement(accordionElement).click().perform();

                // 6️⃣ Wait for expansion
                wait.until(driver1 ->
                        accordionElement.getAttribute("aria-expanded").equals("true")
                );

                logger.info("✅ Accordion '{}' expanded successfully.", accordionText);
            } else {
                logger.info("✔ Accordion '{}' already expanded.", accordionText);
            }

        } catch (Exception e) {
            logger.error("❌ Failed to expand accordion '{}': {}", accordionText, e.getMessage());
        }
    }
    public void clickBackOfficeInternalCompleteStep(String stepName) {


        try {
            // Locate the step container using the step name
            String xpath = "//a[@data-id='workStepName' and contains(text(),'" + stepName + "')]"
                    + "/ancestor::div[contains(@class,'workStep')]"
                    + "//a[contains(@title,'Complete the Step')]";

            WebElement completeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", completeBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", completeBtn);

            logger.info("Clicked Complete Step for: {}", stepName);

        } catch (Exception e) {
            logger.error("Failed to click Complete Step for: " + stepName + " | Error: " + e.getMessage());
            throw e;
        }
    }

    public void clickCtaButton(String cta) {

        String xpath = String.format("//button[normalize-space()='%s' and @type='button']", cta);
        By nextBtn = By.xpath(xpath);

        // 🔹 TRY NORMAL CLICK
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
            logger.info("👉 Trying normal click on CTA: {}", cta);
            btn.click();
            return;
        } catch (Exception e) {
            logger.warn("⚠ Normal click failed for '{}': {}", cta, e.getMessage());
        }

        // 🔹 TRY JS CLICK
        try {
            WebElement btn = driver.findElement(nextBtn);
            logger.info("👉 Trying JS click on CTA: {}", cta);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            return;
        } catch (Exception e) {
            logger.warn("⚠ JS click failed for '{}': {}", cta, e.getMessage());
        }

        // 🔹 TRY ACTIONS CLICK
        try {
            WebElement btn = driver.findElement(nextBtn);
            logger.info("👉 Trying Actions click on CTA: {}", cta);
            new Actions(driver).moveToElement(btn).click().perform();
            return;
        } catch (Exception e) {
            logger.error("❌ All click attempts failed for '{}': {}", cta, e.getMessage());
        }
    }


    public void selectFirstCompanyName() {
        try {

            // Get first radio button
            WebElement firstRadio = suggestedComapnyNames.get(0);

            // Scroll into view (important for LWC)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstRadio);

            // Get label text for logs
            String labelXpath = String.format(
                    "//label[@for='%s']//span[contains(@part,'formatted-rich-text')]",
                    firstRadio.getAttribute("id")
            );
            String labelText = driver.findElement(By.xpath(labelXpath)).getText();

            logger.info("👉 Selecting FIRST company name: {}", labelText);

            // Normal click → JS fallback
            try {
                firstRadio.click();
            } catch (Exception ex) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstRadio);
            }

            logger.info("✅ Successfully selected FIRST company: {}", labelText);

        } catch (Exception e) {
            logger.error("❌ Failed to select first company name: {}", e.getMessage());
        }
    }

    public void selectApproverDecision(String decision) {

        selectFromDropdown(approverDecisionDropdown, decision);
    }



}


