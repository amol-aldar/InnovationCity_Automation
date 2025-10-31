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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    By proceedButtonEle= By.xpath("//footer[contains(@class,'dao-modal-footer')]//button[normalize-space()='PROCEED']");

    //Residential Address
    @FindBy(xpath = "//input[contains(@id,'City_Town_Village')]")
    private WebElement cityTownInput;

    @FindBy(xpath = "//input[contains(@id,'Area')]")
    private WebElement areaInput;

    @FindBy(xpath = "//input[contains(@id,'Street')]")
    private WebElement streetNameInput;

    @FindBy(xpath = "//input[contains(@id,'Building')]")
    private WebElement buildingNameInput;

    @FindBy(xpath = "//input[contains(@id,'Flat')]")
    private WebElement flatNumberInput;

    @FindBy(xpath = "//input[contains(@id,'Postal_Code')]")
    private WebElement postalCodeInput;

    By yearAddBtnEle= By.xpath("//button[@value='add']");

    @FindBy(xpath = "//label[text()='Same as residential address']")
    private WebElement sameResiAddressCheckboxEle;

    //Shareholder Roles

    @FindBy(xpath = "//label[text()='Is UBO?']")
    private WebElement isUBOCheckBoxEle;

    @FindBy(xpath = "//label[text()='Owns or controls 25% or more of the shares or voting rights.']")
    private WebElement ownVotingRightCheckBoxEle;

    @FindBy(xpath = "//label[text()='Is this shareholder a Manager for this company?']")
    private WebElement isManagerCheckBoxEle;

    @FindBy(xpath = "//label[text()='Is this shareholder a Director for this company?']")
    private WebElement isDirectorCheckBoxEle;

    @FindBy(xpath = "//label[text()='Is this shareholder an Authorized Signatory?']")
    private WebElement isAuthorizedSignatoryCheckBoxEle;

    @FindBy(xpath = "//input[starts-with(@id,'Number_of_Shares')]")
    private WebElement ownedSharesInput;

    @FindBy(xpath = "//footer[contains(@class,'dao-modal-footer')]//button[normalize-space()='SUBMIT']")
    private WebElement submitButtonEle;















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
        enterTelephoneNumber("Telephone Number");
        selectCompanyType(typeName);
        selectLicenseIssuedCountry(countryName);
    }

    public void enterLicenseNumber() {
        String licenseNumber = "LIC-" + (100000 + random.nextInt(900000));
        typeAndLog(licenseNumberInput, licenseNumber, "License Number");
    }

    public void enterTelephoneNumber(String fieldName) {
        String xpath = String.format("//label[normalize-space(text())='%s']/preceding::input[@type='tel']", fieldName);
        WebElement phoneField = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        enterPhoneNumber(phoneField, "Telephone Number");
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


    public void clickPortalApplicationCTA(String ctaLabel) {
        String xpath = String.format("//div[contains(@class,'slds-grid')]//button[.//span[normalize-space()='%s']]", ctaLabel);
        By ctaLocator = By.xpath(xpath);

        try {
            logger.info("🪄 Waiting for '{}' CTA button to be clickable...", ctaLabel);
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(ctaLocator));

            try {
                button.click();
                logger.info("✅ '{}' button clicked successfully.", ctaLabel);
            } catch (ElementClickInterceptedException e) {
                logger.warn("⚠️ Normal click failed for '{}', trying JS click...", ctaLabel);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                logger.info("✅ JS click executed for '{}'.", ctaLabel);
            }

        } catch (TimeoutException e) {
            logger.error("❌ '{}' button not found within timeout.", ctaLabel);
        } catch (NoSuchElementException e) {
            logger.error("❌ '{}' button not found in DOM.", ctaLabel);
        } catch (Exception e) {
            logger.error("❌ Unexpected error while clicking '{}': {}", ctaLabel, e.getMessage());
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
        clickShareholderField("Nationality");
        By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + country + "']");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

        scrollToElement(option);
        option.click();

    }

    public void selectPassportIssueCountry(String country){
        clickShareholderField("Passport Issue Country");
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



    public void clickShareholderField(String fieldName){
        // XPath to get the button above the label
        String xpath = String.format("//label[text()='%s']/preceding-sibling::button", fieldName);

        WebElement button = driver.findElement(By.xpath(xpath));

        button.click();
    }

    public void enterDateOfBirth() {
        String dob = getRandomDate("DOB");
        WebElement dobField = driver.findElement(By.xpath("//input[contains(@id,'Date_of_Birth')]"));
        selectDate(dobField, dob, "Date of Birth");
    }

    public void enterPassportIssueDate() {
        String issueDate = getRandomDate("ISSUE");
        WebElement issueDateField = driver.findElement(By.xpath("//input[contains(@id,'Passport_Issue_Date')]"));
        selectDate(issueDateField, issueDate, "Passport Issue Date");
    }

    public void enterPassportExpiryDate() {
        String expiryDate = getRandomDate("EXPIRY");
        WebElement expiryDateField = driver.findElement(By.xpath("//input[contains(@id,'Passport_Expiry_Date')]"));
        selectDate(expiryDateField, expiryDate, "Passport Expiry Date");
    }

    public void clickProceedButton() {
        WebElement proceedBtn=driver.findElement(proceedButtonEle);
        scrollToElement(proceedBtn);
        waitForClickability(proceedBtn);
        proceedBtn.click();

    }

    //Select Visa

    public void selectVisaType(String visaType){
            clickShareholderField("Current UAE Visa Status");
            By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + visaType + "']");
            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));
            scrollToElement(option);
            option.click();

    }

    // 🔹 Shareholder Contact Details
    public void enterShareholderPrimaryEmail(String fieldName) {
        String xpath = String.format("//label[text()='%s']/preceding-sibling::input", fieldName);

        WebElement fieldInput = driver.findElement(By.xpath(xpath));
        // Generate random email address
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com"};
        String randomName = "user" + System.currentTimeMillis(); // unique per run
        String randomDomain = domains[new Random().nextInt(domains.length)];
        String randomEmail = randomName + "@" + randomDomain;

        typeAndLog(fieldInput, randomEmail, "Shareholder Primary Email");
    }

    public void enterShareholderPrimaryMobileNum(String fieldName) {
        String xpath = String.format("//label[normalize-space(text())='%s']/preceding::input[@type='tel'][1]", fieldName);
        WebElement phoneField = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

        enterPhoneNumber(phoneField, "Shareholder Primary Mobile Number");
    }

    private void enterPhoneNumber(WebElement phoneInput, String fieldName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Random random = new Random();

        try {
            // 📱 Generate random UAE-style number: 05XXXXXXXX
            String randomPhone = "05" + (10000000 + random.nextInt(89999999));
            logger.info("📞 Generated random number for '{}': {}", fieldName, randomPhone);

            // Wait for element visibility and interactivity
            wait.until(ExpectedConditions.visibilityOf(phoneInput));
            wait.until(ExpectedConditions.elementToBeClickable(phoneInput));
            scrollToElement(phoneInput);

            // === Attempt 1: Normal sendKeys ===
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", phoneInput);
                phoneInput.click();
                phoneInput.clear();

                // Offset click to avoid country flag overlays (if intl-tel-input used)
                new Actions(driver).moveToElement(phoneInput, 120, 5).click().perform();

                phoneInput.sendKeys(randomPhone);
                phoneInput.sendKeys(Keys.TAB);
                Thread.sleep(700);

                String actualValue = phoneInput.getAttribute("value");
                if (actualValue != null && actualValue.contains("05")) {
                    logger.info("✅ {} entered successfully via sendKeys: {}", fieldName, actualValue);
                    return;
                } else {
                    logger.warn("⚠️ sendKeys executed but value not reflected. Trying JS fallback...");
                }
            } catch (Exception e1) {
                logger.warn("⚠️ sendKeys failed for '{}': {}. Trying JS fallback...", fieldName, e1.getMessage());
            }

            // === Attempt 2: JavaScript fallback ===
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                String script =
                        "arguments[0].focus();" +
                                "arguments[0].value = arguments[1];" +
                                "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                                "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));" +
                                "arguments[0].blur();";
                js.executeScript(script, phoneInput, randomPhone);

                Thread.sleep(700);
                String actualValue = phoneInput.getAttribute("value");
                if (actualValue != null && actualValue.contains("05")) {
                    logger.info("✅ {} entered successfully via JavaScript: {}", fieldName, actualValue);
                    return;
                } else {
                    logger.warn("⚠️ JS executed but value not reflected. Trying keyboard simulation...");
                }
            } catch (Exception e2) {
                logger.warn("⚠️ JS fallback failed for '{}': {}. Trying keyboard simulation...", fieldName, e2.getMessage());
            }

            // === Attempt 3: Keyboard simulation (char-by-char typing) ===
            try {
                phoneInput.click();
                for (char c : randomPhone.toCharArray()) {
                    phoneInput.sendKeys(Character.toString(c));
                    Thread.sleep(50);
                }
                phoneInput.sendKeys(Keys.TAB);
                Thread.sleep(700);

                String actualValue = phoneInput.getAttribute("value");
                if (actualValue != null && actualValue.contains("05")) {
                    logger.info("✅ {} entered successfully via keyboard simulation: {}", fieldName, actualValue);
                    return;
                }
            } catch (Exception e3) {
                logger.error("⚠️ Keyboard simulation failed for '{}': {}", fieldName, e3.getMessage());
            }

            // === All Fallbacks Failed ===
            logger.error("❌ All methods failed to enter {}.", fieldName);
            throw new RuntimeException("Failed to enter phone number for: " + fieldName);

        } catch (Exception e) {
            logger.error("❌ Exception while entering {}: {}", fieldName, e.getMessage());
            throw new RuntimeException(e);
        }
    }




    //Residential Address proof

    public void selectResidentialCountry(String country){
        clickShareholderField("Select Country");
        By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + country + "']");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

        scrollToElement(option);
        option.click();
    }

    public void selectResidentialProvince(String stateProvice){
        clickShareholderField("Select State/Province");
        By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + stateProvice + "']");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

        scrollToElement(option);
        option.click();

    }

    public void enterResidentialCityVillage() {
        String[] cities = {"Dubai", "Abu Dhabi", "Sharjah", "Ajman", "Fujairah", "Al Ain", "Ras Al Khaimah", "Umm Al Quwain"};
        String randomCity = cities[new Random().nextInt(cities.length)];

        logger.info("🏙️ Attempting to enter City/Town/Village: {}", randomCity);
        try {
            typeAndLog(cityTownInput, randomCity, "City/Town/Village");
        } catch (Exception e) {
            logger.warn("⚠️ sendKeys failed for City. Trying JS fallback... {}", e.getMessage());
            setValueUsingJS(cityTownInput, randomCity, "City/Town/Village");
        }
    }

    public void enterResidentialAreaName() {
        String[] areas = {"Jumeirah", "Deira", "Karama", "Mirdif", "Business Bay", "Al Nahda", "Marina", "Downtown"};
        String randomArea = areas[new Random().nextInt(areas.length)];

        logger.info("🏘️ Attempting to enter Area: {}", randomArea);
        try {
            typeAndLog(areaInput, randomArea, "Area");
        } catch (Exception e) {
            logger.warn("⚠️ sendKeys failed for Area. Trying JS fallback... {}", e.getMessage());
            setValueUsingJS(areaInput, randomArea, "Area");
        }
    }

    public void enterResidentialStreetName() {
        String[] streetPrefixes = {"Main", "Palm", "Sunset", "Ocean", "King", "Creek", "Lake", "Desert"};
        String randomStreet = streetPrefixes[new Random().nextInt(streetPrefixes.length)] + " Street";

        logger.info("🚗 Attempting to enter Street Name: {}", randomStreet);
        try {
            typeAndLog(streetNameInput, randomStreet, "Street Name");
        } catch (Exception e) {
            logger.warn("⚠️ sendKeys failed for Street. Trying JS fallback... {}", e.getMessage());
            setValueUsingJS(streetNameInput, randomStreet, "Street Name");
        }
    }

    public void enterResidentialBuildingName() {
        String[] buildingNames = {"Skyline Tower", "Palm Residence", "Bay View", "Ocean Heights", "Golden Sands", "Rosewood", "Silver Tower"};
        String randomBuilding = buildingNames[new Random().nextInt(buildingNames.length)];

        logger.info("🏢 Attempting to enter Building Name: {}", randomBuilding);
        try {
            typeAndLog(buildingNameInput, randomBuilding, "Building Name");
        } catch (Exception e) {
            logger.warn("⚠️ sendKeys failed for Building. Trying JS fallback... {}", e.getMessage());
            setValueUsingJS(buildingNameInput, randomBuilding, "Building Name");
        }
    }

    public void enterResidentialFlatNumber() {
        int randomFlat = 100 + new Random().nextInt(900); // e.g., 101–999
        String flatNumber = "Flat " + randomFlat;

        logger.info("🏠 Attempting to enter Flat Number: {}", flatNumber);
        try {
            typeAndLog(flatNumberInput, flatNumber, "Flat Number");
        } catch (Exception e) {
            logger.warn("⚠️ sendKeys failed for Flat Number. Trying JS fallback... {}", e.getMessage());
            setValueUsingJS(flatNumberInput, flatNumber, "Flat Number");
        }
    }

    public void enterResidentialPostalCode() {

        String postal = String.valueOf(10000 + new Random().nextInt(89999)); // 10000–99999

        logger.info("📮 Attempting to enter Postal Code: {}", postal);
        try {
            typeAndLog(postalCodeInput, postal, "Postal Code");
        } catch (Exception e) {
            logger.warn("⚠️ sendKeys failed for Postal Code. Trying JS fallback... {}", e.getMessage());
            setValueUsingJS(postalCodeInput, postal, "Postal Code");
        }
    }

    public void selectYearsLiving() {
        logger.info("⏱️ Selecting years of residence (5 clicks on + button)");

        try {
            WebElement addButton = driver.findElement(By.xpath("(//button[@value='add'])[1]"));

            scrollToElement(addButton);
            waitForClickability(addButton);

            for (int i = 0; i < 5; i++) {
                try {
                    addButton.click();
                    logger.info("🟢 Clicked + button (iteration {})", i + 1);
                } catch (ElementClickInterceptedException e) {
                    logger.warn("⚠️ Click intercepted on attempt {} — retrying with JS click...", i + 1);
                    jsClick(addButton);
                } catch (Exception e) {
                    logger.error("❌ Unexpected error clicking + button at iteration {}: {}", i + 1, e.getMessage());
                }
                Thread.sleep(300); // small delay between clicks
            }

            logger.info("✅ Successfully selected 5 years of residence.");
        } catch (Exception e) {
            logger.error("❌ Failed while selecting years of residence: {}", e.getMessage());
        }
    }

    public void jsClick(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("🟢 JS click performed successfully on element: {}", element);
        } catch (Exception e) {
            logger.error("❌ JS click failed: {}", e.getMessage());
        }
    }

    public void selectResidentialAddressCheckbox() {
        logger.info("🏡 Selecting 'Same as Residential Address' checkbox...");

        try {
            scrollToElement(sameResiAddressCheckboxEle);
            waitForClickability(sameResiAddressCheckboxEle);

            try {
                sameResiAddressCheckboxEle.click();
                logger.info("✅ Checkbox clicked successfully using standard click.");
            } catch (ElementClickInterceptedException e) {
                logger.warn("⚠️ Click intercepted, retrying with JavaScript click...");
                jsClick(sameResiAddressCheckboxEle);
                logger.info("✅ Checkbox clicked successfully using JavaScript fallback.");
            }

        } catch (Exception e) {
            logger.error("❌ Failed to click 'Same as Residential Address' checkbox: {}", e.getMessage());
        }
    }



     // Individual methods for each checkbox

    public void selectUBOCheckbox() {
        clickCheckboxWithFallback(isUBOCheckBoxEle, "Is UBO?");
    }

    public void selectVotingRightCheckbox() {
        clickCheckboxWithFallback(ownVotingRightCheckBoxEle, "Owns or controls 25% or more of the shares or voting rights");
    }

    public void selectManagerCheckbox() {
        clickCheckboxWithFallback(isManagerCheckBoxEle, "Is this shareholder a Manager for this company?");
    }

    public void selectDirectorCheckbox() {
        clickCheckboxWithFallback(isDirectorCheckBoxEle, "Is this shareholder a Director for this company?");
    }

    public void selectAuthorizedSignatoryCheckbox() {
        clickCheckboxWithFallback(isAuthorizedSignatoryCheckBoxEle, "Is this shareholder an Authorized Signatory?");
    }

    public void selectNatureOfOwnership(String ownershipNature) {
        //As a Nominee
        // Control through other means e.g. holds decision or veto rights and /or controls the rights of others
        logger.info("🏢 Selecting Nature of Ownership: {}", ownershipNature);
        try {
            clickShareholderField("Select Nature of Ownership");

            By dropdownOption = By.xpath("//li[contains(@class,'dao-input-combo-options')][normalize-space(text())='" + ownershipNature + "']");
            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));

            scrollToElement(option);
            waitForClickability(option);
            option.click();

            logger.info("✅ Successfully selected Nature of Ownership: {}", ownershipNature);

        } catch (Exception e) {
            logger.error("⚠️ Click failed for Nature of Ownership: {} | Trying JavaScript fallback...", ownershipNature);
            try {
                WebElement fallbackOption = driver.findElement(By.xpath("//li[normalize-space(text())='" + ownershipNature + "']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fallbackOption);
                logger.info("✅ Nature of Ownership selected via JavaScript fallback: {}", ownershipNature);
            } catch (Exception jsEx) {
                logger.error("❌ Failed to select Nature of Ownership '{}': {}", ownershipNature, jsEx.getMessage());
            }
        }
    }

    public void enterOwnedShares() {
//        int shares = 1000 + new Random().nextInt(9000); // random between 1000–9999
        int shares = 1000;
        String shareText = String.valueOf(shares);
        logger.info("💰 Entering Owned Shares: {}", shareText);
        try {
            typeAndLog(ownedSharesInput, shareText, "Owned Shares");
            logger.info("✅ Owned Shares entered successfully: {}", shareText);
        } catch (Exception e) {
            logger.error("⚠️ Failed to type Owned Shares normally, trying JS fallback: {}", e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", ownedSharesInput, shareText);
                logger.info("✅ Owned Shares set successfully using JS fallback: {}", shareText);
            } catch (Exception jsEx) {
                logger.error("❌ JS fallback also failed for Owned Shares: {}", jsEx.getMessage());
            }
        }
    }

    public void clickSubmitButton() {
        logger.info("🖱️ Attempting to click Submit button...");
        try {
            scrollToElement(submitButtonEle);
            waitForClickability(submitButtonEle);
            submitButtonEle.click();
            logger.info("✅ Submit button clicked successfully.");
        } catch (Exception e) {
            logger.error("⚠️ Normal click failed for Submit button, attempting JS fallback: {}", e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButtonEle);
                logger.info("✅ Submit button clicked successfully using JS fallback.");
            } catch (Exception jsEx) {
                logger.error("❌ Failed to click Submit button even with JS fallback: {}", jsEx.getMessage());
            }
        }
    }



    private void clickCheckboxWithFallback(WebElement element, String checkboxName) {
        logger.info("☑️ Attempting to select '{}' checkbox...", checkboxName);
        try {
            scrollToElement(element);
            waitForClickability(element);

            try {
                element.click();
                logger.info("✅ '{}' checkbox selected successfully (normal click).", checkboxName);
            } catch (ElementClickInterceptedException e) {
                logger.warn("⚠️ '{}' checkbox click intercepted, retrying with JavaScript...", checkboxName);
                jsClick(element);
                logger.info("✅ '{}' checkbox selected successfully (JS fallback).", checkboxName);
            }

        } catch (Exception e) {
            logger.error("❌ Failed to select '{}' checkbox: {}", checkboxName, e.getMessage());
        }
    }




    // JS fallback for setting input field value when sendKeys fails.

    private void setValueUsingJS(WebElement element, String value, String fieldName) {
        Logger logger = LoggerFactory.getLogger(getClass());
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('change'));", element, value);
            logger.info("✅ Successfully set {} using JavaScript fallback.", fieldName);
        } catch (Exception e) {
            logger.error("❌ JS fallback also failed for {}: {}", fieldName, e.getMessage());
            throw new RuntimeException("Failed to enter value for " + fieldName, e);
        }
    }






    // Generates random date strings (yyyy-MM-dd) depending on type.

    private String getRandomDate(String type) {
        Random random = new Random();
        LocalDate randomDate;

        switch (type.toUpperCase()) {
            case "DOB":
                // Between 1970 and 2007
                int startYearDOB = 1970;
                int endYearDOB = 2007;
                randomDate = LocalDate.of(startYearDOB + random.nextInt(endYearDOB - startYearDOB + 1),
                        1 + random.nextInt(12),
                        1 + random.nextInt(28));
                break;

            case "ISSUE":
                // Within last 10 years
                randomDate = LocalDate.now().minusDays(random.nextInt(365 * 10));
                break;

            case "EXPIRY":
                // 5–10 years in the future
                randomDate = LocalDate.now().plusDays(365 * (5 + random.nextInt(5)))
                        .withDayOfMonth(1 + random.nextInt(28));
                break;

            default:
                randomDate = LocalDate.now();
                break;
        }

        return randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    public void selectDate(WebElement dateField, String dateValue, String fieldName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        logger.info("📅 Attempting to enter {}: {}", fieldName, dateValue);

        try {
            wait.until(ExpectedConditions.visibilityOf(dateField));

            // ✅ OPTION 1: Direct sendKeys
            try {
//                dateField.click();
//                dateField.clear();
                dateField.sendKeys(dateValue);
                dateField.sendKeys(Keys.TAB);

                if (dateField.getAttribute("value").equals(dateValue)) {
                    logger.info("✅ Successfully entered {} using sendKeys.", fieldName);
                    return;
                } else {
                    logger.warn("⚠️ sendKeys executed but value not reflected in {}. Trying JS fallback...", fieldName);
                }
            } catch (Exception e1) {
                logger.warn("⚠️ sendKeys failed for {}: {}. Trying JS fallback...", fieldName, e1.getMessage());
            }

            // ✅ OPTION 2: JavaScript Fallback
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value='" + dateValue + "';", dateField);
                js.executeScript("arguments[0].dispatchEvent(new Event('change'));", dateField);

                if (dateField.getAttribute("value").equals(dateValue)) {
                    logger.info("✅ Successfully set {} using JavaScript.", fieldName);
                    return;
                } else {
                    logger.warn("⚠️ JS executed but value not reflected in {}. Trying keyboard navigation...", fieldName);
                }
            } catch (Exception e2) {
                logger.warn("⚠️ JavaScript fallback failed for {}: {}. Trying keyboard navigation...", fieldName, e2.getMessage());
            }

            // ✅ OPTION 3: Keyboard Navigation
            try {
                dateField.click();
                dateField.sendKeys(Keys.ARROW_DOWN);
                dateField.sendKeys(Keys.ENTER);

                if (!dateField.getAttribute("value").isEmpty()) {
                    logger.info("✅ Successfully selected {} using keyboard navigation.", fieldName);
                    return;
                } else {
                    logger.warn("⚠️ Keyboard input didn’t change {}. Trying calendar click...", fieldName);
                }
            } catch (Exception e3) {
                logger.warn("⚠️ Keyboard fallback failed for {}: {}. Trying calendar click...", fieldName, e3.getMessage());
            }

            // ✅ OPTION 4: Direct Calendar Click
            try {
                String day = dateValue.split("-")[2];
                String xpath = String.format("//td[contains(@data-value,'%s') or text()='%s']", dateValue, Integer.parseInt(day));

                WebElement dateElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                dateElement.click();
                logger.info("✅ Successfully clicked date '{}' for {}.", dateValue, fieldName);
                return;
            } catch (Exception e4) {
                logger.error("❌ All fallback methods failed for {}: {}", fieldName, e4.getMessage());
                throw new RuntimeException("Failed to set " + fieldName + " for value: " + dateValue, e4);
            }

        } catch (Exception e) {
            logger.error("❌ Exception while handling {}: {}", fieldName, e.getMessage());
            throw new RuntimeException(fieldName + " entry failed for value: " + dateValue, e);
        }
    }



}

