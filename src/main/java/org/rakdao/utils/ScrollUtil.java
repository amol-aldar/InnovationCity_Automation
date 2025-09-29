package org.rakdao.utils;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScrollUtil {

    private final WebDriver driver;

    public ScrollUtil(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Scrolls the page until the element is visible in viewport.
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
}

