package org.rakdao.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.rakdao.utils.JsonLocatorReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<By> getAllLocators(String key) {
        List<By> locators = new ArrayList<>();

        // Get all locator data (main + fallback)
        Map<String, Object> locatorData = JsonLocatorReader.getLocatorData(pageName, key);

        // Primary locator
        Map<String, String> primary = new HashMap<>();
        primary.put("locatorType", (String) locatorData.get("locatorType"));
        primary.put("locatorValue", (String) locatorData.get("locatorValue"));
        locators.add(JsonLocatorReader.buildBy(primary));

        // Fallback locators
        Object fallbackObj = locatorData.get("fallback");
        if (fallbackObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> fallbacks = (List<Map<String, String>>) fallbackObj;
            for (Map<String, String> fb : fallbacks) {
                locators.add(JsonLocatorReader.buildBy(fb));
            }
        }

        return locators;
    }


}
