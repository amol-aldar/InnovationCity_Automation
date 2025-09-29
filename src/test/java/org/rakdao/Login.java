package org.rakdao;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.pageObjects.HomePage;
import org.rakdao.pageObjects.BasePage;
import org.rakdao.base.BaseClass;
import org.rakdao.utils.ConfigReader;
import org.rakdao.utils.JsonLocatorReader;
import org.rakdao.utils.User;
import org.rakdao.utils.UserGenerator;
import org.testng.annotations.Test;

import java.time.Duration;

public class Login extends BaseClass {
    HomePage homePage;
    // Generate random user
    User user = UserGenerator.generateUser();

    @Test
    public void loginTest() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ===================== LOGIN =====================
        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/LoginPage.json");
        BasePage loginPage = new BasePage(driver, "LoginPage");

        loginPage.getElement("usernameField").sendKeys(ConfigReader.get("adminUserName"));
        loginPage.getElement("passwordField").sendKeys(ConfigReader.get("adminPassword"));
        loginPage.getElement("loginButton").click();

        try {
            if (loginPage.getElement("errorMessage").isDisplayed()) {
                System.out.println("❌ Login failed: " + loginPage.getElement("errorMessage").getText());
            }
        } catch (Exception e) {
            System.out.println("✅ Login successful (error message not found).");
        }

        // ===================== NAVIGATE TO LEADS =====================
        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/NavigationBar.json");
        BasePage navBar = new BasePage(driver, "NavigationBar");

        clickElementWithFallback(navBar, "leadsTab", "moreButton", wait);

        // ===================== CLICK NEW LEAD =====================
        JsonLocatorReader.load(System.getProperty("user.dir")
                + "/src/main/java/org/rakdao/pageObjects/locators/LeadsActions.json");
        BasePage leadBar = new BasePage(driver, "LeadsActions");

        clickElementWithJSFallback(leadBar, "newButton", wait);
        homePage = new HomePage(driver);
        homePage.selectRecordType("Customer");
        homePage.clickCta("Next");
        //Thread.sleep(3000);
        homePage.enterFirstName(user.getFirstName());
        homePage.enterLastName(user.getLastName());
        homePage.selectNationality("India");
        homePage.selectEntityType("Standard Company");
        homePage.selectActivityGroup("Blockchain Development, DLT services & Software");
        homePage.enterCompanyName(user.getCompany());
        homePage.enterMobileNumber(user.getMobile());
        homePage.enterEmail(user.getEmail());
        Thread.sleep(2000);
        homePage.clickRibbonCta("Save");

        // 🔎 Wait for Sales Path container to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul.slds-path__nav")));  // Salesforce Path container

        System.out.println("✅ Save successful, Sales Path is visible.");
        // ===================== SALES PATH =====================
//        JsonLocatorReader.load(System.getProperty("user.dir")
//                + "/src/main/java/org/rakdao/pageObjects/locators/LeadToOpprtunityStages.json");
//        BasePage salesPath = new BasePage(driver, "LeadSalesPath");
//
//        // Click the 'Converted' tab
//        clickElementWithJSFallback(salesPath, "tabs.Converted", wait);
//
//        // Click 'Mark Status as Complete' button
//        clickElementWithJSFallback(salesPath, "actions.markStatusAsComplete", wait);
//       // homePage.clickLeadModalCta("Convert");
        driver.findElement(By.cssSelector(""));
    }

    /**
     * Tries to click an element directly, then via a dropdown button if needed,
     * and falls back to JavaScript click if necessary.
     */
    private void clickElementWithFallback(BasePage page, String elementKey, String dropdownKey, WebDriverWait wait) {
        try {
            WebElement element = page.getElement(elementKey);
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            System.out.println("✅ Clicked " + elementKey + " directly");
        } catch (Exception e1) {
            try {
                WebElement dropdown = page.getElement(dropdownKey);
                wait.until(ExpectedConditions.elementToBeClickable(dropdown)).click();
                WebElement element = page.getElement(elementKey);
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                System.out.println("✅ Clicked " + elementKey + " from dropdown");
            } catch (Exception e2) {
                try {
                    WebElement element = page.getElement(elementKey);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    System.out.println("✅ Clicked " + elementKey + " using JavaScript");
                } catch (Exception e3) {
                    throw new RuntimeException("❌ Failed to click " + elementKey + " by all methods", e3);
                }
            }
        }
    }

    /**
     * Tries to click an element using WebDriverWait and falls back to JavaScript if needed.
     */
    private void clickElementWithJSFallback(BasePage page, String elementKey, WebDriverWait wait) {
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
