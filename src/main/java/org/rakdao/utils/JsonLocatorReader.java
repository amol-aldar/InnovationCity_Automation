package org.rakdao.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility to read locators from JSON file.
 * Supports primary + fallback locators.
 */
public class JsonLocatorReader {

    private static JsonNode rootNode;

    /** Load JSON file once before accessing elements */
    public static void load(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        rootNode = mapper.readTree(new File(filePath));
    }

    /** Retrieve WebElement directly using page + element name */
    public static WebElement getElement(WebDriver driver, String pageName, String elementName) {
        if (rootNode == null) throw new RuntimeException("JSON locators not loaded. Call load() first.");

        JsonNode pageNode = rootNode.get(pageName);
        if (pageNode == null || pageNode.get(elementName) == null)
            throw new RuntimeException("Element not found in JSON: " + pageName + " -> " + elementName);

        JsonNode elementNode = pageNode.get(elementName);

        // Try primary locator first
        try {
            By by = getByFromJson(elementNode.get("locatorType").asText(), elementNode.get("locatorValue").asText());
            return driver.findElement(by);
        } catch (Exception e) {
            // Try fallbacks if available
            JsonNode fallbacks = elementNode.get("fallback");
            if (fallbacks != null && fallbacks.isArray()) {
                Iterator<JsonNode> iter = fallbacks.elements();
                while (iter.hasNext()) {
                    JsonNode fallback = iter.next();
                    try {
                        By by = getByFromJson(fallback.get("locatorType").asText(), fallback.get("locatorValue").asText());
                        return driver.findElement(by);
                    } catch (Exception ignored) {}
                }
            }
            throw new RuntimeException("Unable to locate element: " + elementName);
        }
    }

    /** ✅ Converts locator type/value into By object */
    private static By getByFromJson(String type, String value) {
        switch (type.toLowerCase()) {
            case "id": return By.id(value);
            case "name": return By.name(value);
            case "css": return By.cssSelector(value);
            case "xpath": return By.xpath(value);
            case "classname": return By.className(value);
            case "linktext": return By.linkText(value);
            case "partiallinktext": return By.partialLinkText(value);
            case "tagname": return By.tagName(value);
            default: throw new IllegalArgumentException("Unsupported locator type: " + type);
        }
    }

    // ----------------------------------------------------------------------
    // ✅ Added Utility Methods (required by clickNavigationTab optimization)
    // ----------------------------------------------------------------------

    /**
     * Returns raw JSON locator data (including fallbacks) for given element.
     * Used by BasePage or page classes for building By lists.
     */
    public static Map<String, Object> getLocatorData(String pageName, String elementName) {
        if (rootNode == null)
            throw new RuntimeException("JSON locators not loaded. Call load() first.");

        JsonNode pageNode = rootNode.get(pageName);
        if (pageNode == null)
            throw new RuntimeException("Page not found in JSON: " + pageName);

        JsonNode elementNode = pageNode.get(elementName);
        if (elementNode == null)
            throw new RuntimeException("Element not found in page: " + elementName);

        Map<String, Object> data = new HashMap<>();
        data.put("locatorType", elementNode.get("locatorType").asText());
        data.put("locatorValue", elementNode.get("locatorValue").asText());

        if (elementNode.has("fallback")) {
            List<Map<String, String>> fallbackList = new ArrayList<>();
            for (JsonNode fb : elementNode.get("fallback")) {
                Map<String, String> fbMap = new HashMap<>();
                fbMap.put("locatorType", fb.get("locatorType").asText());
                fbMap.put("locatorValue", fb.get("locatorValue").asText());
                fallbackList.add(fbMap);
            }
            data.put("fallback", fallbackList);
        }
        return data;
    }

    /**
     * Builds a Selenium By object from a generic locator data map.
     * Example input: {"locatorType":"xpath", "locatorValue":"//div[@id='lead']"}
     */
    public static By buildBy(Map<String, String> locatorData) {
        if (locatorData == null || !locatorData.containsKey("locatorType") || !locatorData.containsKey("locatorValue"))
            throw new IllegalArgumentException("Invalid locator data: " + locatorData);

        String type = locatorData.get("locatorType");
        String value = locatorData.get("locatorValue");
        return getByFromJson(type, value);
    }
}
