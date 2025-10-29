package org.rakdao.pageObjects;

import ch.qos.logback.core.CoreConstants;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class PortalApplicationPage extends ReusableUtil {

    private static final Logger logger = LoggerFactory.getLogger(PortalApplicationPage.class);
    private final WebDriver driver;
    private final Random random = new Random();

    public PortalApplicationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("✅ PortalApplicationPage initialized successfully with driver: {}", driver);
    }

    // ========== 🔹 LOCATORS ==========

    //Portal Elements
    @FindBy(xpath = "//button[text()='START NOW']")
    private WebElement startNowButton;

    @FindBy(xpath = "//input[@name='companyName' or @placeholder='Enter Company Name']")
    private WebElement companyNameInput;

    @FindBy(xpath = "//input[contains(@id,'RD_Agent_License_Number')]")
    private WebElement licenseNumberInput;

    @FindBy(xpath = "//input[contains(@id,'Phone')]")
    private WebElement mobileInput;



    @FindBy(xpath = "//label[normalize-space(text())='License Issued Country']/preceding-sibling::button[contains(@class,'dao-input-button')]")
    private WebElement licenseIssueCountryBox;

    By dropdownBox=By.cssSelector("div[class*='dao-input-'][class*='combo-dropdown']");

    @FindBy(css = "div[class*='dao-input-'][class*='combo-dropdown'] ul li[class*='dao-input-combo-options']")
    private List<WebElement> licenseIssueCountriesListDropdown;

    @FindBy(xpath = "//input[contains(@id,'RD_Official_Email_Address')]")
    private WebElement emailInput;

    @FindBy(xpath = "//input[contains(@id,'Website')]")
    private WebElement websiteInput;

    @FindBy(xpath = "//input[contains(@id,'RD_Tax_Registration_Number')]")
    private WebElement taxRegiInput;

    @FindBy(xpath = "//label[normalize-space(text())='Company Type']/preceding-sibling::button[contains(@class,'dao-input-button')]")
    private WebElement companyTypeBox;

    @FindBy(css = "div[class*='dao-input-'][class*='combo-dropdown'] ul li[class*='dao-input-combo-options']")
    private List<WebElement> companyTypeListDropdown;

    private By dropdownContainerLocator = By.cssSelector("div.dao-input-combo-dropdown, div.dao-input-top-combo-dropdown");

    private By dropdownOptionsLocator = By.cssSelector("ul.dao-input-combo-options-wrapper > li.dao-input-combo-options");


    @FindBy(xpath = "//input[contains(@id,'RD_Full_Registered_Office_Number')]")
    private WebElement registeredOfficeAddressInput;

    // ========== 🔹 BANK DETAILS SECTION ==========

    @FindBy(xpath = "//input[contains(@id,'Bank_Name')]")
    private WebElement bankNameInput;

    @FindBy(xpath = "//input[contains(@id,'Bank_Account_Number')]")
    private WebElement bankAccountNumberInput;

    @FindBy(xpath = "//input[contains(@id,'IBAN')]")
    private WebElement ibanInput;

    @FindBy(xpath = "//input[contains(@id,'SWIFT_BIC_Code')]")
    private WebElement swiftCodeInput;

    @FindBy(xpath = "//input[contains(@id,'Branch')]")
    private WebElement branchInput;

    @FindBy(xpath = "//input[contains(@id,'Bank_Address')]")
    private WebElement bankAddressInput;

    @FindBy(css = "div.slds-grid button[type='button']")
    private List<WebElement> ctas;

    //Shareholder details
    @FindBy(xpath = "//input[contains(@id,'Total_Number_of_Shares')]")
    private WebElement shareNumberInput;

    @FindBy(xpath = "//input[contains(@id,'Value_Per_Share')]")
    private WebElement shareValueInput;

    @FindBy(xpath = "//div[contains(@class,'dao-shareholder-details-box') and contains(@data-name,'Shareholder')]")
    private List<WebElement> shareholderTypeBoxes;

    @FindBy(xpath = "//input[contains(@id,'FirstName')]")
    private WebElement shareholderFirstNameInput;

    @FindBy(xpath = "//input[contains(@id,'LastName')]")
    private WebElement shareholderLastNameInput;


    @FindBy(xpath = "//input[contains(@id,'Birth_City')]")
    private WebElement shareholderBirthPlaceInput;

    @FindBy(xpath = "//input[contains(@id,'Passport_Number')]")
    private WebElement shareholderPassportNumInput;












    // ========== 🔹 ACTION METHOD ==========

    public void clickStartNowButton() {
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
                return;
            } catch (ElementClickInterceptedException e) {
                logger.warn("⚠️ Standard click intercepted — retrying with JavaScript click...");
                js.executeScript("arguments[0].click();", startNowButton);
                logger.info("✅ Clicked 'Start Now' button using JavaScript fallback.");
                return;
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
    }

    public void fillCompanyDetails(String countryName,String typeName){
        enterLicenseNumber();
        enterOfficialEmail();
        enterWebsite();
        enterTaxRegistrationNumber();
        enterRegisteredOfficeAddress();
        enterTelephoneNumber();
        selectCompanyType(typeName);
        selectLicenseIssuedCountry(countryName);
    }

    public void enterLicenseNumber() {
        String licenseNumber = "LIC-" + (100000 + random.nextInt(900000));
        typeAndLog(licenseNumberInput, licenseNumber, "License Number");
    }

    public void enterTelephoneNumber() {
        try {
            // ✅ Generate random number
            String mobileString = "05" + (10000000 + random.nextInt(89999999));
            logger.info("📱 Preparing to enter Telephone Number: {}", mobileString);

            // 🔢 Convert to numeric (int)
            // Removing leading zero for conversion
            long mobileInt = Long.parseLong(mobileString);  // using long to avoid overflow

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // ✅ Ensure correct element reference
            wait.until(ExpectedConditions.visibilityOf(mobileInput));
            wait.until(ExpectedConditions.elementToBeClickable(mobileInput));
            scrollToElement(mobileInput);

            // ✅ Bring focus to input
            ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", mobileInput);
            mobileInput.clear();

            // ✅ Offset click (avoid country flag overlay)
            Actions actions = new Actions(driver);
            actions.moveToElement(mobileInput, 120, 5).click().perform();

            // ✅ Send as numeric value — converting back to string for sendKeys
            mobileInput.sendKeys(String.valueOf(mobileInt));
            logger.info("✅ Successfully entered telephone number: {}", mobileInt);

        } catch (ElementNotInteractableException e) {
            logger.warn("⚠️ Telephone field not interactable, retrying with JS...");
            String mobileString = "05" + (10000000 + random.nextInt(89999999));
            long mobileInt = Long.parseLong(mobileString);
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", mobileInput, String.valueOf(mobileInt));
            logger.info("✅ Entered telephone number via JS fallback: {}", mobileInt);

        } catch (Exception e) {
            logger.error("❌ Failed to enter telephone number: {}", e.getMessage());
            throw new RuntimeException("Telephone number entry failed", e);
        }
    }



    public void selectLicenseIssuedCountry(String countryName) {
        try {
            logger.info("🌍 Selecting License Issued Country: {}", countryName);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // 1️⃣ Click the dropdown button
            WebElement countryDropdownButton = driver.findElement(By.xpath(
                    "//label[normalize-space()='License Issued Country']/preceding-sibling::button[contains(@class,'dao-input-button')]"
            ));
            wait.until(ExpectedConditions.elementToBeClickable(countryDropdownButton)).click();

            // 2️⃣ Wait for dropdown options and locate the matching one
            By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + countryName + "']");
            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

            // 3️⃣ Scroll and click the desired option
            scrollToElement(option);
            option.click();

            logger.info("✅ Successfully selected License Issued Country '{}'", countryName);

        } catch (Exception e) {
            logger.error("❌ Failed to select License Issued Country '{}': {}", countryName, e.getMessage());
            throw new RuntimeException("License Issued Country selection failed for: " + countryName, e);
        }
    }



    public void enterOfficialEmail() {
        String email = "qa.company" + random.nextInt(999) + "@example.com";
        typeAndLog(emailInput, email, "Official Email");
    }

    public void enterWebsite() {
        String website = "https://www." + ("company" + random.nextInt(999)).toLowerCase() + ".com";
        typeAndLog(websiteInput, website, "Company Website");
    }

    public void enterTaxRegistrationNumber() {
        String taxReg = "TRN-" + (10000 + random.nextInt(90000));
        typeAndLog(taxRegiInput, taxReg, "Tax Registration Number");
    }

    public void selectCompanyType(String companyType) {
        try {
            logger.info("🔽 Selecting Company Type: {}", companyType);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // 1️⃣ Click dropdown button
            WebElement companyTypeButton = driver.findElement(By.xpath(
                    "//label[normalize-space()='Company Type']/preceding-sibling::button[contains(@class,'dao-input-button')]"
            ));
            wait.until(ExpectedConditions.elementToBeClickable(companyTypeButton)).click();

            // 2️⃣ Wait for dropdown list and select the matching option
            By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + companyType + "']");
            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

            scrollToElement(option);
            option.click();

            logger.info("✅ Successfully selected company type '{}'", companyType);

        } catch (Exception e) {
            logger.error("❌ Failed to select Company Type '{}': {}", companyType, e.getMessage());
            throw new RuntimeException("Company type selection failed for: " + companyType, e);
        }
    }




    public void enterRegisteredOfficeAddress() {
        String address = "Suite " + (100 + random.nextInt(400)) + ", Business Bay";
        typeAndLog(registeredOfficeAddressInput, address, "Registered Office Address");
    }

    public void clickSaveInfoButton(){
        try {
            scrollToElement(ctas.get(0));
            waitForClickability(ctas.get(0));
            ctas.get(0).click();
            logger.info("➡️ Clicked Save info button.");
        } catch (Exception e) {
            logger.error("❌ Failed to click Save info button: {}", e.getMessage());
        }
    }

    public void clickContinueButton() {
        try {
            scrollToElement(ctas.get(1));
            waitForClickability(ctas.get(1));
            ctas.get(1).click();
            logger.info("➡️ Clicked CONTINUE to proceed.");
        } catch (Exception e) {
            logger.error("❌ Failed to click CONTINUE: {}", e.getMessage());
        }
    }

    // ---------- 🔹 COMMON HELPER ----------
    private void typeAndLog(WebElement element, String value, String fieldName) {
        try {
            waitForVisibility(element);
            scrollToElement(element);
            element.clear();
            element.sendKeys(value);
            logger.info("📝 Filled '{}' with '{}'", fieldName, value);
        } catch (Exception e) {
            logger.warn("⚠️ Could not fill '{}': {}", fieldName, e.getMessage());
        }
    }

    public void fillBankDetails() {
        try {
            logger.info("🏦 Filling out Bank Details section...");

            String bankName = "RAK Bank " + (char) ('A' + random.nextInt(26));
            String accountNumber = "AE" + (100000000 + random.nextInt(900000000));
            String iban = "AE" + (100000 + random.nextInt(900000)) + (10000000 + random.nextInt(89999999));
            String swift = "RAK" + (1000 + random.nextInt(9000));
            String branch = "Branch-" + (10 + random.nextInt(90));
            String address = "Building " + (1 + random.nextInt(50)) + ", Al Qusais, Dubai";

            typeAndLog(bankNameInput, bankName, "Bank Name");
            typeAndLog(bankAccountNumberInput, accountNumber, "Bank Account Number");
            typeAndLog(ibanInput, iban, "IBAN");
            typeAndLog(swiftCodeInput, swift, "Swift Code");
            typeAndLog(branchInput, branch, "Branch");
            typeAndLog(bankAddressInput, address, "Bank Address");

            logger.info("✅ Bank Details section filled successfully.");

        } catch (Exception e) {
            logger.error("❌ Failed to fill Bank Details section: {}", e.getMessage(), e);
            throw new RuntimeException("Error while filling Bank Details", e);
        }
    }


     // Example: Fills a single field dynamically by field name.

    public void fillBankField(String fieldName, String value) {
        try {
            WebElement targetField = switch (fieldName.toLowerCase()) {
                case "bank name" -> bankNameInput;
                case "bank account number" -> bankAccountNumberInput;
                case "iban" -> ibanInput;
                case "swift code" -> swiftCodeInput;
                case "branch" -> branchInput;
                case "bank address" -> bankAddressInput;
                default -> throw new IllegalArgumentException("Unknown bank field: " + fieldName);
            };

            typeAndLog(targetField, value, fieldName);
        } catch (Exception e) {
            logger.error("❌ Failed to fill bank field '{}': {}", fieldName, e.getMessage());
        }
    }


    //Shareholder details

    public void enterNumberOfShares(){
        String shareNumber="1000";
        typeAndLog(shareNumberInput, shareNumber, "Number Of Shares");
    }

    public void enterShareValue(){
        String shareValue="100";
        typeAndLog(shareValueInput, shareValue, "Each Share Value");
    }

    public void clickAddShareholder(String type) {
        String formattedType = type.trim().equalsIgnoreCase("individual") ? "Individual" : "Corporate";
        String dynamicXpath = String.format("//div[contains(@class,'dao-shareholder-details-box') and normalize-space(@data-name)='%s Shareholder']", formattedType);

        logger.info("🧾 Attempting to click on '{}' Shareholder tile...", formattedType);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement target = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXpath)));

            scrollToElement(target);
            waitForClickability(target);

            try {
                target.click();
                logger.info("✅ Successfully clicked '{}' Shareholder using standard click.", formattedType);
            } catch (ElementClickInterceptedException e) {
                logger.warn("⚠️ Standard click failed — retrying with JavaScript for '{}' Shareholder.", formattedType);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
                logger.info("✅ Clicked '{}' Shareholder using JS fallback.", formattedType);
            }

        } catch (TimeoutException e) {
            logger.error("❌ '{}' Shareholder option not visible after timeout.", formattedType);
            throw new RuntimeException("Shareholder option not found: " + formattedType, e);
        } catch (Exception e) {
            logger.error("❌ Unexpected error clicking '{}' Shareholder: {}", formattedType, e.getMessage());
            throw new RuntimeException("Error clicking shareholder option: " + formattedType, e);
        }
    }


    public void enterShareholderFname() {
        String[] firstNames = {"John", "Alice", "Michael", "Emma", "David", "Sophia", "Liam", "Olivia"};
        Random random = new Random();
        String firstName = firstNames[random.nextInt(firstNames.length)];

        typeAndLog(shareholderFirstNameInput, firstName, "Shareholder First Name");
    }

    public void enterShareholderLname() {
        String[] lastNames = {"Smith", "Johnson", "Brown", "Taylor", "Anderson", "Thomas", "Jackson", "White"};
        Random random = new Random();
        String lastName = lastNames[random.nextInt(lastNames.length)];

        typeAndLog(shareholderLastNameInput, lastName, "Shareholder Last Name");
    }

    public void selectGender(String gender){
        clickShareholderField("Gender");

        By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + gender + "']");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

        scrollToElement(option);
        option.click();
    }

    public void selectBirthCountry(String country){
        clickShareholderField("Birth Country");
        By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + country + "']");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

        scrollToElement(option);
        option.click();

    }

    public void selectNationality(String country){
        clickShareholderField("Birth Country");
        By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + country + "']");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

        scrollToElement(option);
        option.click();

    }

    public void selectPassportIssueCountry(String country){
        clickShareholderField("Birth Country");
        By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + country + "']");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

        scrollToElement(option);
        option.click();

    }

    public void enterShareholderPlaceOfBirth() {
        String[] places = {
                "Dubai", "Abu Dhabi", "London", "New York", "Mumbai",
                "Sydney", "Toronto", "Singapore", "Paris", "Berlin",
                "Doha", "Tokyo", "Cairo", "Rome", "Istanbul"
        };
        Random random = new Random();
        String randomPlace = places[random.nextInt(places.length)];

        typeAndLog(shareholderBirthPlaceInput, randomPlace, "Shareholder Place of Birth");
    }


    public void enterShareholderPassportNum() {
        Random random = new Random();

        // Example: Generate random passport format like "A1234567" or "N9876543"
        char prefix = (char) ('A' + random.nextInt(26)); // random letter A-Z
        int number = 1000000 + random.nextInt(9000000);  // 7-digit number
        String passportNum = prefix + String.valueOf(number);

        typeAndLog(shareholderPassportNumInput, passportNum, "Shareholder Passport Number");
    }


    public void selectDOB() {
        clickShareholderField("Date of Birth");

    }


    public void clickShareholderField(String fieldName){
        // XPath to get the button above the label
        String xpath = String.format("//label[text()='%s']/preceding-sibling::button", fieldName);

        WebElement button = driver.findElement(By.xpath(xpath));

        button.click();
    }


}

