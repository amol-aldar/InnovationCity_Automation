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
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class HomePage extends ReusableUtil {

    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    SoftAssert softAssert=new SoftAssert();
    public HomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("HomePage initialized");
    }
    @FindBy(css="div.override-splash")
    private WebElement loader;

    @FindBy(css=".none.navexStandardManager .slds-no-print.oneAppNavContainer")
    private WebElement navBar;

    // Update selector to be more robust and general
    @FindBy(css = ".slds-context-bar__secondary.navCenter a")
    private List<WebElement> navigationBar;

    @FindBy(css = "ul.branding-actions a.forceActionLink[title='New']")
    private List<WebElement> newLeadLinks;

    @FindBy(css = ".forceChangeRecordType h2")
    private WebElement newLeadTitle;

    @FindBy(css = ".changeRecordTypeRightColumn .slds-form-element__label")
    private List<WebElement> recordTypes;

    @FindBy(css = ".inlineFooter div button span")
    private List<WebElement> cancelNextCtas;



    @FindBy(css = ".right-align-buttons button[class*='slds-button slds-button']")
    private List<WebElement> ribbonCtas;

    @FindBy(css = "span.title.slds-path__title")
    private List<WebElement> leadStages;

    @FindBy(css = "div.modal-header.slds-modal__header h1")
    private WebElement convertLeadModalTitle;

    @FindBy(css = "div.modal-footer.slds-modal__footer button")
    private List<WebElement> convertLeadCtas;

    // Scrollable modal container
    By newLeadModal = By.cssSelector("div.modal-body.scrollable.slds-modal__content");

    @FindBy(xpath = "//input[@name='firstName']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@name='lastName']")
    private WebElement lastNameInput;

    private final By nationalityBox = By.xpath("//button[@name='Nationality' or @aria-label='Nationality']");

    String countryDropdown="//lightning-base-combobox-item//span/span";

    // Locator for the Entity Type button (can be reused)
    private final By entityTypeBox = By.xpath("//button[@aria-label='Entity Type']");
    // Locator for the dropdown options
    private String entityTypeDropdown = "//div[@aria-label='Entity Type']//span[contains(@class, 'slds-media__body')]";

    private final By activityGroupBox = By.xpath("//button[@aria-label='Activity Group']");

    private String activityGroupDropdown = "//div[@aria-label='Activity Group']//span[contains(@class, 'slds-media__body')]";

    private final By comapnyBox = By.xpath("//input[@name='Company']");

    private final By emailBox = By.xpath("//input[@name='Email']");

    private final By mobileBox = By.xpath("//input[@name='MobilePhone']");


    @FindBy(css = "ul li.slds-button-group-item.visible button")
    private List<WebElement> newLeadCtas;

    private final By toastMsgLeadCreated = By.xpath("//div[contains(@id,'toastDescription')]//span[contains(@class,'toastMessage') and contains(text(), 'was created.')]");

    By markCompleteBtn = By.xpath("//button[.//span[text()='Change Converted Status']]");

    @FindBy(css="div.title h2")
    private WebElement titleEle;


    @FindBy(css = "div.headerConvertedItem.slds-text-heading_large h3")
    private List<WebElement> convertedItems;

    @FindBy(css = "div.bodyConvertedItem div.primaryField.truncate a")
    private List<WebElement> primaryFields;


    public void clickOnNavigationBar(String keyword) throws IOException {
        logger.info("Clicking on navigation tab containing href: {}", keyword);

        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/NavigationBar.json");
        BasePage navBar = new BasePage(driver, "NavigationBar");

        clickElementWithJSFallback(navBar, "leadsTab", wait);

    }


    // --- Actions ---
    public void clickNewLeadButton(String keyword) throws IOException {
        logger.info("Clicking the 'New' button...");
        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/LeadsActions.json");
        BasePage leadBar = new BasePage(driver, "LeadsActions");

        clickElementWithJSFallback(leadBar, "newButton", wait);
    }


    public void selectRecordType(String recordType) {
        logger.info("Selecting record type: {}", recordType);
        waitForVisibility(newLeadTitle);

        WebElement matchedRecord = recordTypes.stream()
                .filter(record -> recordType != null && record != null
                        && record.getText().equalsIgnoreCase(recordType))
                .findFirst()
                .orElseThrow(() -> {
                    final String recordTypeFinal = recordType;
                    String message = "Record type '" + recordTypeFinal + "' not found.";
                    logger.error(message);
                    return new NoSuchElementException(message);
                });

        matchedRecord.click();
        logger.info("Record type '{}' selected successfully.", recordType);
    }

    public void clickCta(String cta) {
        logger.info("Clicking CTA button: {}", cta);
        WebElement button = cancelNextCtas.stream()
                .filter(ctas -> ctas.getText().equalsIgnoreCase(cta))
                .findFirst()
                .orElseThrow(() -> {
                    final String ctaFinal = cta;
                    String message = "CTA button '" + ctaFinal + "' not found.";
                    logger.error(message);
                    return new NoSuchElementException(message);
                });

        button.click();
        logger.info("CTA button '{}' clicked successfully.", cta);
    }

    public void enterFirstName(String fname) {
        logger.info("Entering first name: {}", fname);
        waitForVisibility(firstNameInput);
        firstNameInput.sendKeys(fname);

    }

    public void enterLastName(String lname) {
        logger.info("Entering last name: {}", lname);
        lastNameInput.clear();
        lastNameInput.sendKeys(lname);
    }

    public void selectNationality(String nationality) {
        logger.info("Selecting nationality: {}", nationality);
        WebElement nationalityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(nationalityBox));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", nationalityElement);
        try {
            nationalityElement.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, using JS click as fallback...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nationalityElement);
        }
        selectDropdownValue(countryDropdown, nationality);
        logger.info("Nationality '{}' selected successfully.", nationality);
    }



    //Selects an Entity Type from the dropdown.

    public void selectEntityType(String entityType) {
        logger.info("Selecting Entity Type: {}", entityType);
        WebElement entityTypeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(entityTypeBox));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", entityTypeElement);
        try {
            entityTypeElement.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, using JS click as fallback...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", entityTypeElement);
        }
        selectDropdownValue(entityTypeDropdown, entityType);
        logger.info("Entity Type '{}' selected successfully.", entityType);
    }

    public void selectActivityGroup(String activityGroup) {
        logger.info("Selecting Activity Group: {}", activityGroup);
        WebElement activityGroupElement = wait.until(ExpectedConditions.visibilityOfElementLocated(activityGroupBox));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", activityGroupElement);
        try {
            activityGroupElement.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, using JS click as fallback...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", activityGroupElement);
        }
        selectDropdownValue(activityGroupDropdown, activityGroup);
        logger.info("Activity Group '{}' selected successfully.", activityGroup);
    }

    public void enterCompanyName(String companyName) {
        logger.info("Entering Company name: {}", companyName);
        WebElement company = wait.until(ExpectedConditions.visibilityOfElementLocated(comapnyBox));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", company);
        company.clear();
        company.sendKeys(companyName);
        logger.info("Company name '{}' entered successfully.", companyName);
    }

    public void enterEmail(String email){
        logger.info("Entering Email: {}", email);
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(emailBox));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", emailField);
        emailField.clear();
        emailField.sendKeys(email);
        logger.info("Email '{}' entered successfully.", email);
    }

    public void enterMobileNumber(String mobileNumber) {
        logger.info("Entering Mobile Number: {}", mobileNumber);
        WebElement mobileField = wait.until(ExpectedConditions.visibilityOfElementLocated(mobileBox));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", mobileField);
        mobileField.clear();
        mobileField.sendKeys(mobileNumber);
        logger.info("Mobile Number '{}' entered successfully.", mobileNumber);
    }

    // ✅ Dropdown selection (with Shadow DOM support)
    public void selectDropdownValue(String dropdownItemsXpath, String value) {
        logger.info("Selecting dropdown value '{}' from '{}'", value, dropdownItemsXpath);
        List<WebElement> options;
        if (dropdownItemsXpath.contains("lightning-base-combobox-item")) {
            // Try to find options inside Shadow DOM
            try {
                WebElement host = driver.findElement(By.cssSelector("lightning-base-combobox-item"));
                WebElement shadowOption = findElementInShadowDom(By.cssSelector("lightning-base-combobox-item"), "span.slds-media__body");
                options = List.of(shadowOption); // If multiple, extend logic to collect all
            } catch (Exception e) {
                logger.warn("Shadow DOM dropdown option not found, falling back to regular XPath.");
                options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(dropdownItemsXpath)));
            }
        } else {
            options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(dropdownItemsXpath)));
        }
        WebElement option = options.stream()
                .filter(el -> el.getText().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> {
                    final String valueFinal = value;
                    return new NoSuchElementException(
                            "Dropdown value '" + valueFinal + "' not found using XPath: " + dropdownItemsXpath
                    );
                });
        scrollToElement(option);
        try {
            option.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Normal click failed, using JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }
        logger.info("Dropdown value '{}' selected successfully.", value);
    }

    public void clickRibbonCta(String ctaText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            // First try: locate by button text
            String xpathByText = "//button[normalize-space(text())='" + ctaText + "']";

            WebElement button = null;

            try {
                button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathByText)));
            } catch (Exception e) {
                logger.warn("Button '{}' not found by text, trying by name attribute...", ctaText);
            }

            // Second try: locate by name attribute (common in Salesforce Lightning)
            if (button == null) {
                String nameAttr = "";
                switch (ctaText.toLowerCase()) {
                    case "save":
                        nameAttr = "SaveEdit";
                        break;
                    case "save & new":
                        nameAttr = "SaveAndNew";
                        break;
                    case "cancel":
                        nameAttr = "CancelEdit";
                        break;
                    default:
                        throw new RuntimeException("Unknown CTA button: " + ctaText);
                }
                button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@name='" + nameAttr + "']")));
            }

            // Scroll to element
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);

            // Click with fallback
            try {
                button.click();
            } catch (Exception e) {
                logger.warn("Normal click failed for '{}', using JS click.", ctaText);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }

            logger.info("Ribbon CTA button '{}' clicked successfully.", ctaText);

        } catch (Exception e) {
            logger.error("Failed to click Ribbon CTA button '{}'", ctaText, e);
            throw new RuntimeException("Failed to click Ribbon CTA button: " + ctaText, e);
        }

    }

    /*public void convertLeadToOpportunity(String leadStage) throws IOException {
        logger.info("Attempting to convert lead to opportunity at stage: {}", leadStage);

        // Load JSON locators
        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/LeadToOpprtunityStages.json");

        // Base object matches JSON root
        BasePage salesPath = new BasePage(driver, "LeadSalesPath");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    }*/
    public void convertLeadToOpportunity(String stageName) throws IOException {
        logger.info("Attempting to convert lead to opportunity at stage: {}", stageName);

        // Wait for the Sales Path container
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.slds-path__nav")));

        // Find all stages
        List<WebElement> stageElements = driver.findElements(By.cssSelector("ul.slds-path__nav li a span.title.slds-path__title"));

        // Filter for the desired stage
        WebElement targetStage = stageElements.stream()
                .filter(stage -> stage.getText().equalsIgnoreCase(stageName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Stage '" + stageName + "' not found."));

        // Scroll into view (horizontal scroll for Salesforce path)
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", targetStage);

        // Wait until clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(targetStage));

        // Click with fallback
        try {
            targetStage.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Normal click failed, using JS click for stage '{}'", stageName);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetStage);
        }

        logger.info("✅ Clicked on Lead Stage: {}", stageName);
    }


    public void clickMarkStageComplete() {
        By markCompleteBtn = By.xpath("//button[.//span[text()='Select Converted Status']]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement button = null;

        try {
            // Wait for the button to be visible in DOM
            button = wait.until(ExpectedConditions.visibilityOfElementLocated(markCompleteBtn));

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);

            // Click with JS fallback
            try {
                button.click();
            } catch (ElementClickInterceptedException e) {
                logger.warn("Normal click failed, using JS click fallback");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }

            logger.info("Clicked 'Change Converted Status' successfully.");

        } catch (Exception e) {
            logger.error("Failed to click 'Change Converted Status'", e);
            throw new RuntimeException("Could not click 'Change Converted Status'", e);
        }
    }



    public void clickLeadModalCta(String ctaText) {
        logger.info("Clicking modal CTA button: {}", ctaText);

        // Base modal footer container
        By modalFooter = By.cssSelector("div.modal-footer.slds-modal__footer");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Wait for modal footer to be visible
            WebElement footer = wait.until(ExpectedConditions.visibilityOfElementLocated(modalFooter));

            // Find button inside modal footer by text
            WebElement button = footer.findElements(By.cssSelector(".modal-footer.slds-modal__footer button")).stream()
                    .filter(b -> b.getText().equalsIgnoreCase(ctaText))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Modal button '" + ctaText + "' not found."));

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);

            // Click with JS fallback
            try {
                button.click();
            } catch (ElementClickInterceptedException e) {
                logger.warn("Normal click failed, using JS click fallback for '{}'", ctaText);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }

            logger.info("Modal CTA '{}' clicked successfully.", ctaText);

        } catch (Exception e) {
            logger.error("Failed to click modal CTA '{}'", ctaText, e);
            throw new RuntimeException("Failed to click modal CTA: " + ctaText, e);
        }
    }




    public OpportunityPage goToAccountContactOpportunity(String itemName) {
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

}



