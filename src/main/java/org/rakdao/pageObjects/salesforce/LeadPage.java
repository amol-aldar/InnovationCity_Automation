package org.rakdao.pageObjects.salesforce;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class LeadPage extends ReusableUtil {

    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(LeadPage.class);
    SoftAssert softAssert= new SoftAssert();

    public LeadPage(WebDriver driver) throws AWTException {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("✅ LeadPage initialized");
    }

    // -------------------- LOCATORS --------------------
    @FindBy(css = ".forceChangeRecordType h2")
    private WebElement newLeadTitle;

    @FindBy(css = ".changeRecordTypeRightColumn .slds-form-element__label")
    private List<WebElement> recordTypes;

    @FindBy(css = ".inlineFooter div button span")
    private List<WebElement> cancelNextCtas;

    @FindBy(xpath = "//input[@name='firstName']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@name='lastName']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@name='Company']")
    private WebElement companyInput;

    @FindBy(xpath = "//input[@name='Email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@name='MobilePhone']")
    private WebElement mobileInput;

    private final By nationalityBox = By.xpath("//button[@aria-label='Nationality']");
    private final By entityTypeBox = By.xpath("//button[@aria-label='Entity Type']");
    private final By activityGroupBox = By.xpath("//button[@aria-label='Activity Group']");
    private final String dropdownItemsXpath = "//lightning-base-combobox-item//span/span";

    // Lead Conversion confirmation
    @FindBy(css = "div.title h2")
    private WebElement titleEle;

    @FindBy(css = "div.headerConvertedItem.slds-text-heading_large h3")
    private List<WebElement> convertedItems;

    @FindBy(css = "div.bodyConvertedItem div.primaryField.truncate a")
    private List<WebElement> primaryFields;

    // -------------------- METHODS --------------------

    /** Select a record type */
    public void selectRecordType(String recordType) {
        waitForVisibility(newLeadTitle);
        logger.info("[selectRecordType] Selecting '{}'", recordType);

        WebElement type = recordTypes.stream()
                .filter(rt -> rt.getText().equalsIgnoreCase(recordType))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Record type not found: " + recordType));

        type.click();
        logger.info("[selectRecordType] ✅ Selected '{}'", recordType);
    }

    /** Click Next/Cancel CTA */
    public void clickNext(String ctaText) {
        WebElement cta = cancelNextCtas.stream()
                .filter(btn -> btn.getText().equalsIgnoreCase(ctaText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("CTA not found: " + ctaText));
        cta.click();
        logger.info("[clickNext] ✅ Clicked CTA '{}'", ctaText);
    }

    /** Enter lead details */
    public void enterLeadDetails(String firstName, String lastName, String company, String email, String mobile) {
        waitForVisibility(firstNameInput);
        firstNameInput.clear(); firstNameInput.sendKeys(firstName);
        lastNameInput.clear(); lastNameInput.sendKeys(lastName);
        companyInput.clear(); companyInput.sendKeys(company);
        mobileInput.clear(); mobileInput.sendKeys(mobile);
        emailInput.clear(); emailInput.sendKeys(email);
        logger.info("[enterLeadDetails] ✅ Entered lead info for '{} {}'", firstName, lastName);
    }

    // -------------------- Dropdowns --------------------
    public void selectEntityType(String entityType) {
        selectDropdownValue(entityTypeBox, dropdownItemsXpath, entityType);
    }

    public void selectActivityGroup(String group) {
        selectDropdownValue(activityGroupBox, dropdownItemsXpath, group);
    }

    public void selectNationality(String nationality) {
        selectDropdownValue(nationalityBox, dropdownItemsXpath, nationality);
    }


    public void selectRoleDetailsCheckbox(String labelName, boolean shouldSelect){
            try {
               String roleTypeXpath= "//span[@class='slds-form-element__label' and normalize-space(text())='" + labelName +
                        "']/ancestor::lightning-primitive-input-checkbox//input[@type='checkbox']";

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(roleTypeXpath)));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);

                boolean isChecked = checkbox.isSelected();
                if (shouldSelect && !isChecked) {
                    checkbox.click();
                    logger.info("Selected checkbox for label: " + labelName);
                } else if (!shouldSelect && isChecked) {
                    checkbox.click();
                    logger.info("Unselected checkbox for label: " + labelName);
                } else {
                    logger.info("Checkbox for label '" + labelName + "' is already in desired state.");
                }
            } catch (Exception e) {
                logger.error("Unable to interact with checkbox for label: " + labelName, e);
                throw new RuntimeException("Checkbox not found or clickable for: " + labelName, e);
            }
        }

    public void handleLeadConversionSection(String sectionName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            logger.info("🔍 Handling Lead Conversion section: {}", sectionName);

            // 1️⃣ Locate section header (Account / Opportunity)
            String sectionHeaderXpath = "//span[contains(@class,'displayLabel') and normalize-space(text())='" + sectionName + "']";
            WebElement sectionHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sectionHeaderXpath)));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", sectionHeader);
            logger.info("✅ Found section header: {}", sectionName);

            // 2️⃣ Expand accordion if collapsed
            WebElement accordionToggle = sectionHeader.findElement(By.xpath("./ancestor::a[contains(@class,'slds-accordion__summary-action')]"));
            String expanded = accordionToggle.getAttribute("aria-expanded");
            if ("false".equalsIgnoreCase(expanded)) {
                js.executeScript("arguments[0].click();", accordionToggle);
                logger.info("📂 Expanding accordion for: {}", sectionName);
                wait.until(ExpectedConditions.attributeToBe(accordionToggle, "aria-expanded", "true"));
                Thread.sleep(1500); // allow rendering to finish
            }

            // 3️⃣ Try locating dropdown (with retry)
            String recordTypeXpath = ".//span[normalize-space(text())='Record Type']/ancestor::div[contains(@class,'uiInputSelect')]//a[contains(@class,'select')]";
            WebElement recordTypeDropdown = null;
            try {
                recordTypeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(recordTypeXpath)));
            } catch (TimeoutException e1) {
                logger.warn("⚠️ Relative Record Type dropdown not found for '{}'. Retrying with global XPath...", sectionName);
                String globalXpath = "(//span[normalize-space(text())='Record Type']/ancestor::div[contains(@class,'uiInputSelect')]//a[contains(@class,'select')])[2]";
                recordTypeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(globalXpath)));
            }

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", recordTypeDropdown);
            js.executeScript("arguments[0].click();", recordTypeDropdown);
            logger.info("📋 Opened Record Type dropdown for {}", sectionName);

            // 4️⃣ Choose appropriate value
            String recordTypeValue = sectionName.equalsIgnoreCase("Account") ? "Channel Partner" : "Agent Onboarding";

            // 5️⃣ Wait for and select dropdown value
            String optionXpath = "//div[@role='listbox']//a[@role='option' and normalize-space(text())='" + recordTypeValue + "']";
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXpath)));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", option);
            js.executeScript("arguments[0].click();", option);
            logger.info("✅ Selected '{}' for {}", recordTypeValue, sectionName);

        } catch (Exception e) {
            logger.error("❌ Failed to handle section '{}': {}", sectionName, e.getMessage());
            throw new RuntimeException("Timeout or failure handling section: " + sectionName, e);
        }
    }







    // -------------------- CTA / Conversion Methods --------------------
    public void clickRibbonCta(String ctaText) {
        logger.info("[clickRibbonCta] Clicking CTA: {}", ctaText);
        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String xpath = "//button[normalize-space(.)='" + ctaText + "']";
        WebElement button = null;
        try { button = localWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))); } catch (Exception ignored) {}

        if (button == null) {
            String nameAttr = switch (ctaText.toLowerCase()) {
                case "save" -> "SaveEdit";
                case "save & new" -> "SaveAndNew";
                case "cancel" -> "CancelEdit";
                default -> null;
            };
            if (nameAttr != null) button = localWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@name='" + nameAttr + "']")));
        }

        if (button == null) throw new NoSuchElementException("CTA button not found: " + ctaText);
        scrollToElement(button);

        try { button.click(); }
        catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button); }

        logger.info("[clickRibbonCta] ✅ Clicked CTA '{}'", ctaText);
    }

    public void convertLeadToOpportunity(String stageName) {
        logger.info("[convertLeadToOpportunity] Converting lead to stage: {}", stageName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.slds-path__nav")));

        List<WebElement> stages = driver.findElements(By.cssSelector("ul.slds-path__nav li a span.title.slds-path__title"));
        WebElement targetStage = stages.stream()
                .filter(s -> s.getText().equalsIgnoreCase(stageName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Stage not found: " + stageName));

        scrollToElement(targetStage);
        try { targetStage.click(); }
        catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetStage);
        }
        logger.info("[convertLeadToOpportunity] ✅ Clicked stage '{}'", stageName);
    }

    public void clickMarkStageComplete() {
        logger.info("[clickMarkStageComplete] Clicking 'Change/Select Converted Status'...");
        By markBtn = By.xpath("//button[.//span[contains(text(),'Converted') or contains(text(),'Change Converted Status')]]");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(markBtn));

        scrollToElement(button);
        try { button.click(); } catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button); }
        logger.info("[clickMarkStageComplete] ✅ Button clicked");
    }

    public void clickLeadModalCta(String ctaText) {
        logger.info("[clickLeadModalCta] Clicking modal CTA '{}'", ctaText);
        By modalFooter = By.cssSelector("div.modal-footer.slds-modal__footer");
        WebElement footer = wait.until(ExpectedConditions.visibilityOfElementLocated(modalFooter));

        WebElement button = footer.findElements(By.cssSelector("button")).stream()
                .filter(b -> b.getText().equalsIgnoreCase(ctaText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Modal button not found: " + ctaText));

        scrollToElement(button);
        try { button.click(); } catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button); }

        logger.info("[clickLeadModalCta] ✅ Clicked modal CTA '{}'", ctaText);
    }

    public OpportunityPage goToAccountContactOpportunity(String itemName) throws AWTException {
        logger.info("Clicking primary field for converted item: {}", itemName);
        waitForVisibility(titleEle);
        softAssert.assertEquals(titleEle.getText(), "Your lead has been converted");

        try {
            AtomicInteger indexCounter = new AtomicInteger(0);

            // Find the primaryField corresponding to the convertedItem
            WebElement targetField = convertedItems.stream()
                    .filter(item -> {
                        int currentIndex = indexCounter.getAndIncrement();
                        return item.getText().equalsIgnoreCase(itemName);
                    })
                    .findFirst()
                    .map(item -> {
                        int index = convertedItems.indexOf(item);
                        return primaryFields.get(index);
                    })
                    .orElseThrow(() -> new NoSuchElementException(
                            "Converted item '" + itemName + "' not found"));

            // Scroll element into view
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", targetField);

            // Try normal click first
            try {
                targetField.click();
                logger.info("Clicked primary field for converted item '{}' via normal click", itemName);
            } catch (ElementNotInteractableException e) {
                logger.warn("Normal click failed, using JS click fallback for '{}'", itemName);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetField);
                logger.info("Clicked primary field for converted item '{}' via JS click", itemName);
            }

        } catch (Exception e) {
            logger.error("Failed to click primary field for converted item '{}'", itemName, e);
            throw new RuntimeException("Failed to click primary field for converted item: " + itemName, e);
        }
        return new OpportunityPage(driver);
    }

//    public OpportunityPage goToAccountContactOpportunity(String itemName) throws InterruptedException {
//        logger.info("[goToAccountContactOpportunity] Opening converted item: {}", itemName);
//        waitForVisibility(titleEle);
//        waitForClickability(convertedItems.get(2));
//
//
//        AtomicInteger counter = new AtomicInteger(0);
//        WebElement target = convertedItems.stream()
//                .filter(item -> counter.getAndIncrement() >= 0 && item.getText().equalsIgnoreCase(itemName))
//                .findFirst()
//                .map(item -> primaryFields.get(convertedItems.indexOf(item)))
//                .orElseThrow(() -> new NoSuchElementException("Converted item not found: " + itemName));
//
//        scrollToElement(target);
//        try {
//            waitForInvisibility(spinner);
//            target.click();
//        } catch (ElementClickInterceptedException e) {
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
//        }
//
//        logger.info("[goToAccountContactOpportunity] ✅ Clicked '{}'", itemName);
//        return new OpportunityPage(driver);
//    }
}
