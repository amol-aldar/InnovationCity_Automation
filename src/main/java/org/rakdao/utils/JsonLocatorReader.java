package org.rakdao.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JsonLocatorReader {

    private static JsonNode rootNode;

    public static void load(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        rootNode = mapper.readTree(new File(filePath));
    }

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
            // Try fallbacks
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
}
