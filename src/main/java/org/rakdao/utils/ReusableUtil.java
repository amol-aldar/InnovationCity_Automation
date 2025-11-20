package org.rakdao.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.*;
import org.rakdao.pageObjects.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReusableUtil {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(ReusableUtil.class);
    Robot robot;

    @FindBy(xpath="//lightning-spinner[@alternative-text='Loading']")
    protected WebElement spinner;



    public ReusableUtil(WebDriver driver) throws AWTException {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.robot = new Robot();
    }

    public void scrollDownByPixel(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + pixels + ");");
    }

    // ✅ Wait for visibility
    public WebElement waitForVisibility(WebElement element) {
        logger.info("Waiting for visibility of element: {}", element);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // ✅ Wait for clickability
    public WebElement waitForClickability(WebElement element) {
        logger.info("Waiting for element to be clickable: {}", element);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // ✅ Generic click
    public void click(WebElement element) {
        logger.info("Clicking on element: {}", element);
        waitForClickability(element).click();
    }

    // ✅ Type text
    public void type(WebElement element, String text) {
        logger.info("Typing '{}' into element: {}", text, element);
        WebElement visibleElement = waitForVisibility(element);
        visibleElement.clear();
        visibleElement.sendKeys(text);
    }

    // ✅ Get text
    public String getText(WebElement element) {
        String text = waitForVisibility(element).getText();
        logger.info("Retrieved text: '{}'", text);
        return text;
    }

    // ✅ Is element displayed
    public boolean isDisplayed(WebElement element) {
        try {
            boolean displayed = waitForVisibility(element).isDisplayed();
            logger.info("Element displayed: {}", displayed);
            return displayed;
        } catch (TimeoutException | NoSuchElementException e) {
            logger.warn("Element not displayed: {}", element);
            return false;
        }
    }

    // ✅ Page-wide scroll
    public void scrollToElement(WebElement element) {
        logger.info("Scrolling page to element: {}", element);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    // ✅ 🔹 Modal-specific scroll
    public void scrollInModal(WebElement modalContainer, WebElement targetElement) {
        logger.info("Scrolling inside modal to element: {}", targetElement);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollTop = arguments[1].offsetTop;", modalContainer, targetElement);
    }

    // ✅ Dropdown selection

    public void selectDropdownValue(String dropdownItemsXpath, String value) {
        logger.info("Selecting dropdown value '{}' from '{}'", value, dropdownItemsXpath);

        // Wait for all dropdown options to be visible
        List<WebElement> options = wait.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By.xpath(dropdownItemsXpath)));

        // Find the option that matches the value
        WebElement option = options.stream()
                .filter(el -> el.getText().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Dropdown value '" + value + "' not found using XPath: " + dropdownItemsXpath
                ));

        // Scroll into view
        scrollToElement(option);

        // Try normal click, fallback to JS click if intercepted
        try {
            option.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Normal click failed, using JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }

        logger.info("Dropdown value '{}' selected successfully.", value);
    }



    public WebElement scrollUntilElementInModal(By modalLocator, By elementLocator, int maxScrolls) {
        WebElement modal = driver.findElement(modalLocator);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int scrollCount = 0;
        WebElement element = null;

        while (scrollCount < maxScrolls) {
            try {
                element = modal.findElement(elementLocator);
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (NoSuchElementException e) {
                // not yet available, keep scrolling
            }

            // ✅ Scroll inside modal body, not the page
            js.executeScript("arguments[0].scrollTop = arguments[0].scrollTop + 400;", modal);
            scrollCount++;
        }

        throw new NoSuchElementException("Element not found in modal after scrolling: " + elementLocator);
    }

    // ✅ Wait for invisibility
    public boolean waitForInvisibility(By locator) {
        logger.info("Waiting for invisibility of element located by: {}", locator);
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.warn("Element did not become invisible: {}", locator);
            return false;
        }
    }

    // Overload for WebElement directly
    public boolean waitForInvisibility(WebElement element) {
        logger.info("Waiting for invisibility of element: {}", element);
        try {
            return wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            logger.warn("Element did not become invisible: {}", element);
            return false;
        }
    }

    // ✅ Find element inside Shadow DOM
    public WebElement findElementInShadowDom(By hostSelector, String shadowSelector) {
        logger.info("Finding element in Shadow DOM. Host selector: {}, Shadow selector: {}", hostSelector, shadowSelector);
        WebElement host = wait.until(ExpectedConditions.presenceOfElementLocated(hostSelector));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement shadowElement = (WebElement) js.executeScript(
            "return arguments[0].shadowRoot.querySelector(arguments[1])", host, shadowSelector
        );
        if (shadowElement == null) {
            throw new NoSuchElementException("Element not found in Shadow DOM: " + shadowSelector);
        }
        logger.info("Found element in Shadow DOM: {}", shadowElement);
        return shadowElement;
    }


    /**
     * Tries to click a WebElement directly, then via dropdown if provided,
     * and finally falls back to JavaScript click if necessary.
     *
     * @param page        BasePage containing the element JSON locators
     * @param elementKey  JSON key for the element to click
     * @param dropdownKey (Optional) JSON key for a dropdown that may contain the element
     * @param wait        WebDriverWait instance
     */

    protected void clickElementWithJSFallback(BasePage page, String elementKey, WebDriverWait wait) {
        try {
            WebElement element = page.getElement(elementKey);
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            System.out.println("✅ Clicked " + elementKey + " button");
        } catch (Exception e) {
            try {
                WebElement element = page.getElement(elementKey);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                System.out.println("✅ Clicked " + elementKey + " using JavaScript fallback");
            } catch (Exception ex) {
                throw new RuntimeException("❌ Failed to click " + elementKey, ex);
            }
        }
    }

    public void safeClickWithJSFallback(WebElement element, String elementName) {
        try {
            waitForClickability(element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            element.click();
            logger.info("✅ Clicked on '{}'", elementName);
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            logger.info("✅ JS click successful on '{}'", elementName);
        } catch (Exception e) {
            logger.error("❌ Failed to click '{}': {}", elementName, e.getMessage());
            throw e;
        }
    }


    // Generates random date strings (dd-MM-yyyy) depending on type.
    protected String getRandomDate(String type) {
        Random random = new Random();
        LocalDate randomDate;

        switch (type.toUpperCase()) {
            case "DOB":
                // Between 1970 and 2007
                int startYearDOB = 1970;
                int endYearDOB = 2007;
                randomDate = LocalDate.of(
                        startYearDOB + random.nextInt(endYearDOB - startYearDOB + 1),
                        1 + random.nextInt(12),
                        1 + random.nextInt(28)
                );
                break;

            case "ISSUE":
                // Within last 10 years
                randomDate = LocalDate.now().minusDays(random.nextInt(365 * 10));
                break;

            case "EXPIRY":
                // 5–10 years in the future
                randomDate = LocalDate.now()
                        .plusDays(365 * (5 + random.nextInt(5)))
                        .withDayOfMonth(1 + random.nextInt(28));
                break;

            default:
                randomDate = LocalDate.now();
                break;
        }

        // ✅ Format: day-month-year (e.g., 12-11-2025)
        return randomDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }


    public void selectDate(WebElement dateField, String dateValue, String fieldName) {

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

    // Generates a random uppercase string from A–Z of given length.
    protected String generateRandomName(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) ('A' + random.nextInt(26)); // A–Z
            sb.append(randomChar);
        }
        return sb.toString();
    }

    // ==========================================
    // 🔍 Utility Methods - Zoom Controls
    // ==========================================
    public void zoomOutPage(int times) {
        try {

            for (int i = 0; i < times; i++) {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(300);
            }
        } catch (Exception e) {
            logger.error("⚠️ Failed while zooming out.", e);
        }
    }

    public void zoomInPage(int times) {
        try {
            for (int i = 0; i < times; i++) {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_PLUS);
                robot.keyRelease(KeyEvent.VK_PLUS);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(300);
            }
        } catch (Exception e) {
            logger.error("⚠️ Failed while zooming in.", e);
        }
    }

    public void switchToWindowByIndex(int index) {
        List<String> windows = new ArrayList<>(driver.getWindowHandles());

        if (index < 0 || index >= windows.size()) {
            throw new RuntimeException("Invalid window index: " + index);
        }

        driver.switchTo().window(windows.get(index));
        logger.info("🔀 Switched to window index {} | Title: {}", index, driver.getTitle());
    }

    /** Robust dropdown selection for Salesforce LWC */
    protected void selectDropdownValue(By dropdownButton, String dropdownItemsXpath, String valueToSelect) {
        logger.info("[selectDropdownValue] Selecting '{}' from '{}'", valueToSelect, dropdownButton);

        // Step 1: Wait for and click the dropdown button

        WebElement button = waitForClickability(driver.findElement(dropdownButton));
        scrollToElement(button);
        try {
            button.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, using JS fallback...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }

        // Step 2: Wait for dropdown items to appear (presence + visibility)
        By itemLocator = By.xpath(String.format("%s[normalize-space(text())='%s']", dropdownItemsXpath, valueToSelect));
        WebElement item = null;

        int attempts = 0;
        while (attempts < 3) { // retry a few times in case of lazy-render
            try {
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(dropdownItemsXpath)));
                item = wait.until(ExpectedConditions.visibilityOfElementLocated(itemLocator));
                break; // found, exit loop
            } catch (TimeoutException e) {
                logger.warn("Dropdown item '{}' not yet visible, retrying... attempt {}", valueToSelect, attempts + 1);
                scrollToElement(button); // scroll dropdown into view
                sleep(500); // small wait before retry
            }
            attempts++;
        }

        if (item == null) {
            throw new java.util.NoSuchElementException("Dropdown option not found: " + valueToSelect);
        }

        // Step 3: Scroll to item and click
        scrollToElement(item);
        try {
            item.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
        }

        logger.info("[selectDropdownValue] ✅ Selected '{}'", valueToSelect);
    }

    /** Utility: simple sleep wrapper */
    private void sleep(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ignored) {}
    }

    public void selectFromDropdown(By locator, String value) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // Scroll
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", dropdown);

            wait.until(ExpectedConditions.elementToBeClickable(dropdown));

            Select select = new Select(dropdown);

            try {
                select.selectByVisibleText(value);
            } catch (NoSuchElementException e1) {
                // Fallback to select by value attribute
                select.selectByValue(value);
            }

            System.out.println("Selected dropdown value: " + value);

        } catch (Exception e) {
            throw new RuntimeException("Failed selecting '" + value + "' → " + e.getMessage(), e);
        }
    }

    protected void scrollToElementSmooth(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                element
        );
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }




}
