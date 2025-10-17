package org.rakdao.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.*;
import org.rakdao.pageObjects.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class ReusableUtil {

    private WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(ReusableUtil.class);

    @FindBy(xpath="//lightning-spinner[@alternative-text='Loading']")
    protected WebElement spinner;

    public ReusableUtil(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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


}
