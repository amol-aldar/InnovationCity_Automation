package org.rakdao;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.rakdao.pageObjects.DocumentUploadPage;
import org.rakdao.pageObjects.PortalApplicationPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;

public class PortalLogin {

    private WebDriver driver;
    private static final String PORTAL_URL = "https://innovationcity--staging.sandbox.my.site.com/AgentPortal/s/login/";
    private static final String USERNAME = "a.aldar+1344@innovationcity.com.innovationcity";
    private static final String PASSWORD = "Rakdao@123";

    @BeforeClass
    public void setup() throws Exception {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
//        options.addArguments("user-data-dir=C:\\PortalLoginProfile");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get(PORTAL_URL);

        // 🔍 Zoom out while maximized
        zoomOutPage(4);
    }

    public void zoomOutPage(int times) {
        try {
            Robot robot = new Robot();
            for (int i = 0; i < times; i++) {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(300);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testPortalApplicationFlow() throws IOException, InterruptedException {
        // ✅ Check if already logged in (OTP skipped)
        if (driver.getCurrentUrl().contains("/s/login")) {
            System.out.println("🔐 Not logged in — performing login...");
            performLogin();
        } else {
            System.out.println("✅ Already logged in — skipping authentication.");
        }

        // ✅ Page Object actions
        PortalApplicationPage portalApplicationPage = new PortalApplicationPage(driver);
        portalApplicationPage.clickStartNowButton();
        /*portalApplicationPage.fillCompanyDetails("India", "Company Limited by Shares");
        portalApplicationPage.fillBankDetails();
        portalApplicationPage.clickSaveInfoButton();
        portalApplicationPage.clickContinueButton();
        portalApplicationPage.enterNumberOfShares();
        portalApplicationPage.enterShareValue();
        portalApplicationPage.clickAddShareholder("Individual");
        portalApplicationPage.enterShareholderFname();
        portalApplicationPage.enterShareholderLname();
        portalApplicationPage.selectGender("Male");
        portalApplicationPage.selectBirthCountry("India");
        portalApplicationPage.enterShareholderPlaceOfBirth();
        portalApplicationPage.selectNationality("India");
        portalApplicationPage.enterShareholderPassportNum();
        portalApplicationPage.enterDateOfBirth();
        portalApplicationPage.enterPassportIssueDate();
        portalApplicationPage.enterPassportExpiryDate();
        portalApplicationPage.selectPassportIssueCountry("India");
        portalApplicationPage.clickProceedButton();
        portalApplicationPage.selectVisaType("No UAE Visa");
        portalApplicationPage.clickProceedButton();
        portalApplicationPage.enterShareholderPrimaryEmail("Primary Email");
        portalApplicationPage.enterShareholderPrimaryMobileNum("Primary Phone");
        portalApplicationPage.clickProceedButton();
        portalApplicationPage.selectResidentialCountry("India");
        portalApplicationPage.selectResidentialProvince("Maharashtra");
        portalApplicationPage.enterResidentialBuildingName();
        portalApplicationPage.enterResidentialFlatNumber();
        portalApplicationPage.enterResidentialStreetName();
        portalApplicationPage.enterResidentialAreaName();
        portalApplicationPage.enterResidentialPostalCode();
        portalApplicationPage.enterResidentialCityVillage();
        portalApplicationPage.selectYearsLiving();
        portalApplicationPage.selectResidentialAddressCheckbox();
        portalApplicationPage.clickProceedButton();
        portalApplicationPage.selectUBOCheckbox();
        portalApplicationPage.selectVotingRightCheckbox();
        portalApplicationPage.selectManagerCheckbox();
        portalApplicationPage.selectDirectorCheckbox();
        portalApplicationPage.selectAuthorizedSignatoryCheckbox();
        portalApplicationPage.selectNatureOfOwnership("As a Nominee");
        portalApplicationPage.clickSubmitButton();
        portalApplicationPage.clickContinueButton();*/
        DocumentUploadPage documentUploadPage= new DocumentUploadPage(driver);
        documentUploadPage.uploadDocumentsSequentially("C:\\Users\\Amol Aldar\\Desktop\\Upload Documents");


    }

    private void performLogin() throws InterruptedException {
        driver.findElement(By.xpath("//input[@placeholder='Username']")).clear();
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys(USERNAME);
        driver.findElement(By.xpath("//input[@placeholder='Password']")).clear();
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[@type='button']")).click();

        // Wait briefly for redirect
        Thread.sleep(4000);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
