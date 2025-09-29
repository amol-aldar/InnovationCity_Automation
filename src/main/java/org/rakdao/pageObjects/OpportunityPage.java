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
SoftAssert softAssert= new SoftAssert();
    public OpportunityPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "div.container.forceRelatedListSingleContainer div[title='Add Products']")
    private WebElement addProductButtonEle;

    @FindBy(css = "modal-container.slds-modal__container table.slds-table")
    private WebElement addProductsModalEle;


    @FindBy(css = "div.modal-header.slds-modal__header h1")
    private WebElement priceBookModalTitleEle;

    @FindBy(css = "div.title.forceMultiAddMultiEditHeader h2")
    private WebElement addProductsModalTitleEle;

    @FindBy(css = "input[class*='default input uiInput uiInputTextForAutocomplete']")
    private WebElement productSearchBoxEle;

    // Table element
    @FindBy(css = ".slds-grid.listDisplays.safari-workaround-anchor table.slds-table")
    private WebElement productTable;

    // Headers
    @FindBy(css = "thead tr th div a span:nth-of-type(2)")
    private List<WebElement> tableHeaders;

    // Rows
    @FindBy(css = "tbody tr")
    private List<WebElement> tableRows;

    @FindBy(css = "th.slds-cell-edit.cellContainer a")
    private List<WebElement> cells;


    // Scroll container (parent div of table)
    @FindBy(css = "div[class='uiScroller scroller-wrapper scroll-bidirectional native']")
    private WebElement scrollContainer;


    @FindBy(css=".modal-footer.slds-modal__footer button span")
    List<WebElement> priceBookCtasEle;


    @FindBy(css="ul.slds-path__nav li a span.title.slds-path__title")
    List<WebElement> stagesEle;


    public void clickAddProduct() {
        logger.info("Clicking 'Add Products' button...");
        waitForVisibility(addProductButtonEle);
        try {

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                    addProductButtonEle);

            // Wait until element is clickable (not just visible)
            waitForClickability(addProductButtonEle);


            try {
                // Normal click attempt
                addProductButtonEle.click();
                logger.info("'Add Products' button clicked successfully.");
            } catch (ElementClickInterceptedException e) {
                // Fallback to JS click
                logger.warn("Normal click intercepted, trying JS click.");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addProductButtonEle);
                logger.info("'Add Products' button clicked via JS fallback.");
            }

        } catch (Exception e) {
            logger.error("Failed to click 'Add Products' button.", e);
            throw new RuntimeException("Failed to click 'Add Products' button.", e);
        }
    }


    public void goToProductListingModal(String ctaText) {
        logger.info("Selecting product from Price Book modal, looking for CTA: {}", ctaText);
            //  Wait for modal to appear
        waitForVisibility(priceBookModalTitleEle);
        softAssert.assertEquals(priceBookModalTitleEle.getText(),"Choose Price Book");

        try {
            //  Wait for modal to appear
            logger.info("Price Book modal is visible with title: {}", priceBookModalTitleEle.getText());

            //  Find target CTA button
            Optional<WebElement> buttonOpt = priceBookCtasEle.stream()
                    .filter(btn -> btn.getText().trim().equalsIgnoreCase(ctaText))
                    .findFirst();

            if (buttonOpt.isEmpty()) {
                throw new NoSuchElementException("CTA button with text '" + ctaText + "' not found in Price Book modal");
            }

            WebElement button = buttonOpt.get();

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);

            // Try normal click
            try {
                wait.until(ExpectedConditions.elementToBeClickable(button)).click();
                logger.info("Clicked Price Book CTA: '{}'", ctaText);
            } catch (ElementClickInterceptedException e) {
                // JS Fallback
                logger.warn("Normal click failed for '{}', using JS click fallback", ctaText);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }

        } catch (Exception e) {
            logger.error("Failed to select product using CTA '{}'", ctaText, e);
            throw new RuntimeException("❌ Could not click Price Book CTA: " + ctaText, e);
        }
    }

    public void chooseProductFromStandardBook(String productName) {
            logger.info("Attempting to select product: {}", productName);

            try {
                // Wait for table to be visible
                wait.until(ExpectedConditions.visibilityOf(productTable));

                // Find "Product Name" column index
                int productNameIndex = -1;
                for (int i = 0; i < tableHeaders.size(); i++) {
                    String headerText = tableHeaders.get(i).getText().trim();
                    logger.info("Header[{}]: {}", i, headerText);
                    if (headerText.equalsIgnoreCase("Product Name")) {
                        productNameIndex = i;
                        break;
                    }
                }

                if (productNameIndex == -1) {
                    throw new RuntimeException("❌ 'Product Name' column not found in table headers.");
                }

                boolean productFound = false;
                int previousRowCount = 0;

                while (true) {
                    logger.info("Currently loaded rows: {}", tableRows.size());

                    for (WebElement row : tableRows) {
                        //List<WebElement> cells = row.findElements(By.cssSelector("th.slds-cell-edit.cellContainer a"));

                        if (cells.size() > productNameIndex) {
                            String cellText = cells.get(productNameIndex).getText().trim();
                            logger.info("Row product: {}", cellText);

                            if (cellText.equalsIgnoreCase(productName)) {
                                WebElement checkbox = cells.get(0).findElement(By.cssSelector("input[type='checkbox']"));

                                ((JavascriptExecutor) driver).executeScript(
                                        "arguments[0].scrollIntoView({block:'center'});", checkbox);

                                try {
                                    checkbox.click();
                                } catch (ElementClickInterceptedException e) {
                                    logger.warn("Normal click failed, retrying with JS click...");
                                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                                }

                                logger.info("✅ Product '{}' selected successfully.", productName);
                                productFound = true;
                                return;
                            }
                        }
                    }

                    // Stop if no new rows are loaded
                    if (tableRows.size() == previousRowCount) {
                        logger.warn("Reached end of table, product '{}' not found!", productName);
                        break;
                    }
                    previousRowCount = tableRows.size();

                    // Scroll table down
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", scrollContainer);
                    Thread.sleep(1000); // wait for lazy loading
                }

                if (!productFound) {
                    throw new NoSuchElementException("❌ Product '" + productName + "' not found in the table!");
                }

            } catch (Exception e) {
                logger.error("Failed to select product '{}'", productName, e);
                throw new RuntimeException("Failed to select product: " + productName, e);
            }
        }


    public void clickOnCta(String ctaText) {
        logger.info("Attempting to click on CTA: {}", ctaText);

        try {
            // Find CTA element by text (case-insensitive)
            WebElement ctaElement = priceBookCtasEle.stream()
                    .filter(cta -> cta.getText().trim().equalsIgnoreCase(ctaText))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("❌ CTA with text '" + ctaText + "' not found."));

            // Wait until clickable
            waitForClickability(ctaElement);

            try {
                ctaElement.click();
                logger.info("✅ Successfully clicked CTA: {}", ctaText);
            } catch (ElementClickInterceptedException | TimeoutException e) {
                logger.warn("⚠️ Normal click failed on '{}', retrying with JS click...", ctaText);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ctaElement);
                logger.info("✅ CTA '{}' clicked successfully using JS fallback.", ctaText);
            }

        } catch (Exception e) {
            logger.error("❌ Failed to click CTA '{}'", ctaText, e);
            throw new RuntimeException("Failed to click CTA: " + ctaText, e);
        }
    }

    /**
     * Clicks on a Lead Stage in the Opportunity Sales Path by stage name
     *
     * @param stageName The name of the stage to click (e.g., "Prospecting", "Future")
     */
    public void clickOpportunityStage(String stageName) {
        logger.info("Clicking Lead Stage: {}", stageName);

        // Wait for the path navigation to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        waitForVisibility(driver.findElement(By.cssSelector("ul.slds-path__nav")));

        // Find the stage matching the name and click
        stagesEle.stream()
                .filter(stage -> stage.getText().equalsIgnoreCase(stageName))
                .findFirst()
                .ifPresent(stage -> {
                    try {
                        // Scroll into view
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", stage);

                        // Normal click first
                        stage.click();
                        logger.info("✅ Clicked Lead Stage: {}", stageName);
                    } catch (ElementClickInterceptedException e) {
                        // JS fallback
                        logger.warn("Normal click failed, using JS click for '{}'", stageName);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stage);
                    }
                });
    }
}
