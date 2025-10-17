package org.rakdao.pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class OpportunityPage extends ReusableUtil {

    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(OpportunityPage.class);
    SoftAssert softAssert = new SoftAssert();

    public OpportunityPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("✅ OpportunityPage initialized with driver: {}", driver);
    }

    @FindBy(css = "div.container.forceRelatedListSingleContainer div[title='Add Products']")
    private WebElement addProductButtonEle;

    @FindBy(css = "modal-container.slds-modal__container table.slds-table")
    private WebElement addProductsModalEle;

    @FindBy(css = "div.modal-header.slds-modal__header h1")
    private WebElement priceBookModalTitleEle;

    @FindBy(css = "div.title.forceMultiAddMultiEditHeader h2")
    private WebElement addProductsModalTitleEle;

    @FindBy(css = "input[title='Search Products']")
    private WebElement productSearchBoxEle;

    @FindBy(xpath = "//span[@part='formatted-rich-text' and contains(., 'visa') and contains(., 'year')]")
    private List<WebElement> productDropdown;

    // Table element
    @FindBy(css = ".slds-grid.listDisplays.safari-workaround-anchor table.slds-table")
    private WebElement productTable;

    // Headers
    @FindBy(css = "thead tr th div a span:nth-of-type(2)")
    private List<WebElement> tableHeaders;

    // Rows
    @FindBy(css = "tbody tr th span a")
    private List<WebElement> tableRows;

    @FindBy(css = "th.slds-cell-edit.cellContainer a")
    private List<WebElement> cells;

    // Scroll container
    @FindBy(css = "div[class='uiScroller scroller-wrapper scroll-bidirectional native']")
    private WebElement scrollContainer;

    @FindBy(css = ".modal-footer.slds-modal__footer button span")
    List<WebElement> priceBookCtasEle;

    @FindBy(xpath = "//h2[text()='Edit Selected Products']")
    private WebElement editProductTextEle;

    @FindBy(css = ".modal-footer.slds-modal__footer button span")
    List<WebElement> editProductModalCtasEle;

    @FindBy(css="ul.slds-path__nav")
    private WebElement opportunityNavStageBarEle;

    @FindBy(css = "ul.slds-path__nav li a span.title.slds-path__title")
    List<WebElement> stagesEle;

    @FindBy(xpath="//span[text()='Mark as Current Stage']")
    private WebElement MarkCompleteCurrentStageEle;

    @FindBy(css="div.selectionCountString button[class='slds-button slds-button']")
    private WebElement productCountAddedEle;

    //Add Inventory Button
    @FindBy(xpath="//button[@name='Opportunity.Add_Inventory']")
    private WebElement addInventoryEle;

    @FindBy(xpath="//h2[@class='slds-modal__title']")
    private WebElement addInventoryModalTitleTextEle;

    @FindBy(xpath = "//label[text()='Entity Type']/following::button[contains(@id,'combobox-button')][1]")
    private WebElement entityTypeDropdown;

    @FindBy(xpath = "//label[text()='Customer Looking For']/following::button[contains(@id,'combobox-button')][1]")
    private WebElement customerLookingForDropdown;

    @FindBy(xpath = "//label[text()='Resource Type']/following::button[contains(@id,'combobox-button')][1]")
    private WebElement resourceTypeDropdown;

    @FindBy(xpath = "//div[contains(@class,'slds-listbox')]")
    private List<WebElement> dropdownOptions;

    @FindBy(xpath = "//button[text()='Get Inventory']")
    private WebElement getInventoryButtonEle;

    @FindBy(xpath = "//h2[normalize-space(text())='No record found.']")
    private WebElement noRecordsTextEle;

    @FindBy(xpath = "//span[contains(normalize-space(.), 'Total Records')]")
    private WebElement totalRecordsTextEle;

    @FindBy(xpath = "//button[text()='Add Selected Inventory']")
    private WebElement addSelectedInventoryButtonEle;



    // ----------------- METHODS -----------------

    public void clickAddProduct() {
        logger.info("[clickAddProduct] Clicking 'Add Products' button...");
        By companyNameEle=By.xpath("//span[text()='Company Name']");
        scrollToElement(driver.findElement(companyNameEle));

        waitForVisibility(addProductButtonEle);
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                    addProductButtonEle);
            waitForClickability(addProductButtonEle);

            try {
                addProductButtonEle.click();
                logger.info("[clickAddProduct] 'Add Products' button clicked successfully.");
            } catch (ElementClickInterceptedException e) {
                logger.warn("[clickAddProduct] Normal click intercepted, trying JS click.");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addProductButtonEle);
                logger.info("[clickAddProduct] 'Add Products' button clicked via JS fallback.");
            }
        } catch (Exception e) {
            logger.error("[clickAddProduct] Failed to click 'Add Products' button.", e);
            throw new RuntimeException("Failed to click 'Add Products' button.", e);
        }
    }

    public void goToProductListingModal(String ctaText) {
        logger.info("[goToProductListingModal] Selecting product from Price Book modal, CTA: {}", ctaText);
        waitForVisibility(priceBookModalTitleEle);
        softAssert.assertEquals(priceBookModalTitleEle.getText(), "Choose Price Book");

        try {
            logger.info("[goToProductListingModal] Price Book modal title: {}", priceBookModalTitleEle.getText());

            Optional<WebElement> buttonOpt = priceBookCtasEle.stream()
                    .filter(btn -> btn.getText().trim().equalsIgnoreCase(ctaText))
                    .findFirst();

            if (buttonOpt.isEmpty()) {
                throw new NoSuchElementException("CTA button with text '" + ctaText + "' not found");
            }

            WebElement button = buttonOpt.get();
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(button)).click();
                logger.info("[goToProductListingModal] Clicked Price Book CTA: '{}'", ctaText);
            } catch (ElementClickInterceptedException e) {
                logger.warn("[goToProductListingModal] Normal click failed for '{}', using JS fallback", ctaText);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }

        } catch (Exception e) {
            logger.error("[goToProductListingModal] Failed to select product CTA '{}'", ctaText, e);
            throw new RuntimeException("Could not click Price Book CTA: " + ctaText, e);
        }
    }

    public void chooseProductFromStandardBook(String productName) {
        logger.info("[chooseProductFromStandardBook] Attempting to select product: {}", productName);

        try {
            waitForVisibility(productTable);
            logger.debug("[chooseProductFromStandardBook] Product table visible.");

            productSearchBoxEle.sendKeys(productName);
            Thread.sleep(1000);
            logger.info("[chooseProductFromStandardBook] Entered '{}' into search box.", productName);

            By listBoxLocator=By.xpath("//div[@role='listbox']");
            logger.debug("[chooseProductFromStandardBook] Waiting for listbox...");
            waitForVisibility(driver.findElement(listBoxLocator));

            Optional<WebElement> matchedProduct = productDropdown.stream()
                    .filter(product -> product.getText().trim().equalsIgnoreCase(productName))
                    .findFirst();

            if (matchedProduct.isPresent()) {
                WebElement productEle = matchedProduct.get();
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", productEle);
                productEle.click();
                logger.info("[chooseProductFromStandardBook] ✅ Selected product '{}'", productName);
            } else {
                logger.error("[chooseProductFromStandardBook] ❌ Product '{}' not found!", productName);
                throw new NoSuchElementException("Product not found: " + productName);
            }
        } catch (Exception e) {
            logger.error("[chooseProductFromStandardBook] ❌ Failed to select '{}'", productName, e);
            throw new RuntimeException("Failed to select product: " + productName, e);
        }
        waitForVisibility(productCountAddedEle);
    }

    public void clickOnCta(String ctaText) {
        logger.info("[clickOnCta] Attempting to click CTA: {}", ctaText);

        try {
            WebElement ctaElement = priceBookCtasEle.stream()
                    .filter(cta -> cta.getText().trim().equalsIgnoreCase(ctaText))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("CTA with text '" + ctaText + "' not found."));

            waitForClickability(ctaElement);

            try {
                ctaElement.click();
                logger.info("[clickOnCta] ✅ Clicked CTA: {}", ctaText);
            } catch (ElementClickInterceptedException | TimeoutException e) {
                logger.warn("[clickOnCta] Normal click failed on '{}', retrying with JS", ctaText);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ctaElement);
                logger.info("[clickOnCta] ✅ CTA '{}' clicked via JS fallback.", ctaText);
            }
        } catch (Exception e) {
            logger.error("[clickOnCta] ❌ Failed to click CTA '{}'", ctaText, e);
            throw new RuntimeException("Failed to click CTA: " + ctaText, e);
        }
    }

    public void clickEditProductModalCta(String ctaText) {
        logger.info("[clickEditProductModalCta] Clicking CTA in edit modal: {}", ctaText);
        waitForVisibility(editProductTextEle);

        try {
            WebElement ctaElement = editProductModalCtasEle.stream()
                    .filter(cta -> cta.getText().trim().equalsIgnoreCase(ctaText))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("CTA with text '" + ctaText + "' not found."));

            waitForClickability(ctaElement);

            try {
                ctaElement.click();
                logger.info("[clickEditProductModalCta] ✅ CTA '{}' clicked.", ctaText);
            } catch (ElementClickInterceptedException | TimeoutException e) {
                logger.warn("[clickEditProductModalCta] Normal click failed on '{}', retrying JS...", ctaText);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ctaElement);
                logger.info("[clickEditProductModalCta] ✅ CTA '{}' clicked via JS.", ctaText);
            }
        } catch (Exception e) {
            logger.error("[clickEditProductModalCta] ❌ Failed to click CTA '{}'", ctaText, e);
            throw new RuntimeException("Failed to click CTA: " + ctaText, e);
        }
        waitForInvisibility(spinner);
    }

//    public void clickAddInventoryButton() {
//        logger.info("[clickAddInventoryButton] Clicking 'Add Inventory'...");
//        try {
//            driver.navigate().refresh();
//            scrollToElement(addInventoryEle);
//            waitForClickability(addInventoryEle);
//            addInventoryEle.click();
//            logger.info("[clickAddInventoryButton] ✅ 'Add Inventory' clicked.");
//        } catch (ElementClickInterceptedException e) {
//            logger.warn("[clickAddInventoryButton] Normal click failed, retrying JS.", e);
//            try {
//                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addInventoryEle);
//                logger.info("[clickAddInventoryButton] ✅ JS click succeeded.");
//            } catch (Exception jsEx) {
//                logger.error("[clickAddInventoryButton] ❌ JS click also failed.", jsEx);
//                throw new RuntimeException("Add Inventory click failed with normal and JS.", jsEx);
//            }
//        } catch (TimeoutException te) {
//            logger.error("[clickAddInventoryButton] ❌ Timeout waiting for button.", te);
//            throw new RuntimeException("Add Inventory button not clickable.", te);
//        } catch (Exception ex) {
//            logger.error("[clickAddInventoryButton] ❌ Unexpected error.", ex);
//            throw new RuntimeException("Failed to click Add Inventory.", ex);
//        }
//    }

    public void clickAddInventoryButton() {
        logger.info("[clickAddInventoryButton] Attempting to click 'Add Inventory'...");

        try {
            // Step 1: Try normal click first
            scrollToElement(addInventoryEle);
            waitForClickability(addInventoryEle);

            try {
                addInventoryEle.click();
                logger.info("[clickAddInventoryButton] ✅ Normal click succeeded.");
                return; // Success, exit early
            } catch (ElementClickInterceptedException e) {
                logger.warn("[clickAddInventoryButton] Normal click intercepted, trying JS click...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addInventoryEle);
                logger.info("[clickAddInventoryButton] ✅ JS click succeeded on first attempt.");
                return;
            }

        } catch (Exception firstAttemptEx) {
            logger.warn("[clickAddInventoryButton] First attempt failed, refreshing page and retrying...", firstAttemptEx);
        }

        // Step 2: Refresh and retry if first attempt failed
        try {
            driver.navigate().refresh();
            logger.info("[clickAddInventoryButton] Page refreshed, retrying click...");

            scrollToElement(addInventoryEle);
            waitForClickability(addInventoryEle);

            try {
                addInventoryEle.click();
                logger.info("[clickAddInventoryButton] ✅ Normal click succeeded after refresh.");
            } catch (ElementClickInterceptedException e2) {
                logger.warn("[clickAddInventoryButton] Second normal click intercepted, retrying JS...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addInventoryEle);
                logger.info("[clickAddInventoryButton] ✅ JS click succeeded after refresh.");
            }

        } catch (Exception finalEx) {
            logger.error("[clickAddInventoryButton] ❌ Click failed even after refresh.", finalEx);
            throw new RuntimeException("Failed to click 'Add Inventory' even after refresh.", finalEx);
        }
    }


    private void selectDropdownValue(WebElement dropdown, String value) {
        try {
            logger.info("[selectDropdownValue] Attempting to select '{}'...", value);
            scrollToElement(dropdown);
            waitForClickability(dropdown);

            String currentValue = dropdown.getText().trim();
            if (currentValue.equalsIgnoreCase(value)) {
                logger.info("[selectDropdownValue] '{}' already selected.", value);
                return;
            }

            dropdown.click();
            WebElement option = driver.findElement(By.xpath("//lightning-base-combobox-item//span[@title='" + value + "']"));
            waitForClickability(option);
            option.click();

            logger.info("[selectDropdownValue] ✅ '{}' selected successfully.", value);
        } catch (Exception e) {
            logger.error("[selectDropdownValue] ❌ Failed to select '{}'.", value, e);
            throw new RuntimeException("Dropdown selection failed: " + value, e);
        }
    }

    public void ensureEntityType(String value) {
        logger.debug("[ensureEntityType] Setting Entity Type to '{}'", value);

        selectDropdownValue(entityTypeDropdown, value);
    }

    public void ensureCustomerLookingFor(String value) {
        logger.debug("[ensureCustomerLookingFor] Setting Customer Looking For to '{}'", value);
        selectDropdownValue(customerLookingForDropdown, value);
    }

    public void ensureResourceType(String value) {
        logger.debug("[ensureResourceType] Setting Resource Type to '{}'", value);
        selectDropdownValue(resourceTypeDropdown, value);
    }

    public void clickGetInventoryButton(){
        getInventoryButtonEle.click();
    }

    public void  selectSpecificInventory(){
        waitForInvisibility(noRecordsTextEle);
        waitForVisibility(totalRecordsTextEle);
        String text = totalRecordsTextEle.getText(); // e.g. "Total Records : 388"
        String count = text.replaceAll("\\D+", ""); // extracts only digits
        System.out.println("Record count: " + count);
    }


    public void selectInventoryByRentalAmount(String expectedAmount) {
        logger.info("[selectInventoryByRentalAmount] 🔍 Searching for rental amount: {}", expectedAmount);

        By tableLocator = By.cssSelector("table.slds-table.slds-table_bordered.slds-table_cell-buffer");
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

        int columnIndex = getColumnIndexByHeader("RENTAL AMOUNT", table);
        logger.debug("[selectInventoryByRentalAmount] 'RENTAL AMOUNT' column index resolved to: {}", columnIndex);

        int maxPages = 20; // safety limit
        boolean found = false;

        for (int page = 1; page <= maxPages; page++) {
            logger.debug("[selectInventoryByRentalAmount] Checking page {}...", page);

            List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
            logger.debug("[selectInventoryByRentalAmount] Page {} loaded with {} rows", page, rows.size());

            for (int i = 0; i < rows.size(); i++) {
                WebElement row = rows.get(i);
                try {
                    // ✅ Always use ./ to scope within row
                    String cellXpath = "./th[" + columnIndex + "]//div";
                    WebElement cell = row.findElement(By.xpath(cellXpath));

                    // Prefer getText() if title attribute is missing
                    String cellValue = cell.getAttribute("title") != null
                            ? cell.getAttribute("title").trim()
                            : cell.getText().trim();

                    logger.debug("Row {} column value: '{}'", i + 1, cellValue);

                    if (cellValue.equals(expectedAmount)) {
                        logger.info("✅ Found rental amount '{}' at row {} on page {}", expectedAmount, i + 1, page);
                        WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                        scrollToElement(checkbox);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                        found = true;
                        break;
                    }
                } catch (NoSuchElementException e) {
                    // Skip if cell not found in this row
                }
            }

            if (found) break;

            // 🔁 Handle pagination
            WebElement nextButton = driver.findElement(By.xpath("//button[contains(text(),'Next')]"));
            if (nextButton.isDisplayed()) {
                logger.info("➡️ Clicking Next Page (page {})...", page + 1);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);
                table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
            } else {
                logger.warn("⚠️ No more pages. Rental amount '{}' not found.", expectedAmount);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("Rental amount '" + expectedAmount + "' not found in table.");
        }
    }

//    public void clickAddSelectedInveButton(){
//        By addSelectedInveEle=By.xpath("//button[text()='Add Selected Inventory']");
//        scrollToElement(driver.findElement(addSelectedInveEle));
//        waitForClickability(driver.findElement(addSelectedInveEle));
//        driver.findElement(addSelectedInveEle).click();
//    }

    public void clickAddSelectedInveButton() {
        logger.info("[clickAddSelectedInveButton] Clicking 'Add Selected Inventory'...");
        scrollToElement(addSelectedInventoryButtonEle);
        waitForClickability(addSelectedInventoryButtonEle);
        addSelectedInventoryButtonEle.click();
        logger.info("[clickAddSelectedInveButton] ✅ Clicked successfully.");
    }


    private int getColumnIndexByHeader(String headerName, WebElement table) {
        List<WebElement> headers = table.findElements(By.xpath(".//thead//th"));
        for (int i = 0; i < headers.size(); i++) {
            String title = headers.get(i).getText().trim();
            if (title.equalsIgnoreCase(headerName)) {
                return i + 1; // XPath index starts at 1
            }
        }
        throw new NoSuchElementException("Header '" + headerName + "' not found in table.");
    }





    public void clickOpportunityStage(String stageName) {
        logger.info("[clickOpportunityStage] Clicking stage: {}", stageName);
        driver.navigate().refresh();
        By entityTypeEle = By.xpath("//p[text()='Entity Type']");
        scrollToElement(driver.findElement(entityTypeEle));

        try {
            logger.debug("[clickOpportunityStage] Waiting for spinner...");
            waitForInvisibility(spinner);
            logger.debug("[clickOpportunityStage] Spinner gone, path visible.");
            waitForVisibility(opportunityNavStageBarEle);

            Optional<WebElement> targetStage = stagesEle.stream()
                    .filter(stage -> stage.getText().equalsIgnoreCase(stageName))
                    .findFirst();

            if (targetStage.isPresent()) {
                WebElement stage = targetStage.get();
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", stage);

                try {
                    stage.click();
                    logger.info("[clickOpportunityStage] ✅ Stage '{}' clicked.", stageName);
                } catch (ElementClickInterceptedException e) {
                    logger.warn("[clickOpportunityStage] Normal click failed, retrying JS for '{}'", stageName);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stage);
                    logger.info("[clickOpportunityStage] ✅ Stage '{}' clicked via JS.", stageName);
                }
            } else {
                throw new NoSuchElementException("Stage '" + stageName + "' not found.");
            }
        } catch (TimeoutException te) {
            logger.error("[clickOpportunityStage] ❌ Timeout waiting for stage path.", te);
            throw new RuntimeException("Failed to click stage: " + stageName, te);
        }
    }

    public void clickOpportunityCompleteButton() {
        logger.info("[clickOpportunityCompleteButton] Clicking 'Mark Complete'...");
        try {
            waitForClickability(MarkCompleteCurrentStageEle);
            MarkCompleteCurrentStageEle.click();
            logger.info("[clickOpportunityCompleteButton] ✅ 'Mark Complete' clicked.");
        } catch (Exception e) {
            logger.warn("[clickOpportunityCompleteButton] Normal click failed, retrying JS. Error: {}", e.getMessage());
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", MarkCompleteCurrentStageEle);
                logger.info("[clickOpportunityCompleteButton] ✅ JS fallback succeeded.");
            } catch (Exception jsEx) {
                logger.error("[clickOpportunityCompleteButton] ❌ Both normal & JS click failed.", jsEx);
                throw new RuntimeException("Failed to click 'Mark Complete'.", jsEx);
            }
        }
    }

    public void loginToPortal(){
        List<WebElement> HeaderFieldsEle=driver.findElements(By.cssSelector("div[class*='windowViewMode-normal'] div p.slds-text-title.slds-truncate"));

    }

    public void clickLinkBelowHeader(String headerLabel) {
        try {
            // Locate the field by its label dynamically (e.g., Primary Contact, Account Name)
            String dynamicXPath = "//p[@class='slds-text-title slds-truncate' and normalize-space(text())='"
                    + headerLabel + "']/following-sibling::p//a";

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dynamicXPath)));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", linkElement);
            linkElement.click();
            logger.info("Clicked link below header: " + headerLabel);
        } catch (TimeoutException e) {
            logger.error("Link not found for header: " + headerLabel);
            throw new RuntimeException("Unable to find link below header: " + headerLabel, e);
        }
    }

}
