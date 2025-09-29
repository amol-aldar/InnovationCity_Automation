package org.rakdao.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.rakdao.utils.JsonLocatorReader;

public class BasePage {

    protected WebDriver driver;
    private String pageName;

    public BasePage(WebDriver driver, String pageName) {
        this.driver = driver;
        this.pageName = pageName;
    }

    public WebElement getElement(String key) {
        return JsonLocatorReader.getElement(driver, pageName, key);
    }
}
