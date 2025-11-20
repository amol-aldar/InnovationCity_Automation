package org.rakdao;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.rakdao.pageObjects.portal.DocumentUploadPage;
import org.rakdao.pageObjects.portal.PortalApplicationPage;
import org.rakdao.pageObjects.portal.PortalHomePage;
import org.rakdao.pageObjects.portal.SignedDocumentPage;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;

public class CustomerPortalLogin {

    private WebDriver driver;
    private static final String PORTAL_URL = "https://innovationcity--staging.sandbox.my.site.com/s/login/";
    private static final String USERNAME = "a.aldar+1554@innovationcity.com.innovationcity";
    private static final String PASSWORD = "Rakdao@123";
    private static final Logger logger = LoggerFactory.getLogger(CustomerPortalLogin.class);


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


    }




    @Test
    public void testPortalApplicationFlow() throws IOException, InterruptedException, AWTException {
        try{
        // ✅ Check if already logged in (OTP skipped)
        if (driver.getCurrentUrl().contains("/s/login")) {
            System.out.println("🔐 Not logged in — performing login...");
            performLogin();
        } else {
            System.out.println("✅ Already logged in — skipping authentication.");
        }

        // ✅ Page Object actions
        PortalHomePage portalHomePage = new PortalHomePage(driver);
        PortalApplicationPage portalApplicationPage = portalHomePage.clickContinueWithApplication();
// 🔍 Zoom out while maximized
            ReusableUtil reusableUtil= new ReusableUtil(driver);
            reusableUtil.zoomOutPage(4);
        // ============================================================
        // 🧭 STEP 10: Fill Business Information in Portal
        // ============================================================
//        portalApplicationPage.clickPortalApplicationCTA("Save As Draft");
//        portalApplicationPage.clickPortalApplicationCTA("Let’s Get Started");
//        portalApplicationPage.enterBusinessNamePreferences("Ltd");
//        portalApplicationPage.selectActivityGroup("Artificial Intelligence");
//            portalApplicationPage.selectJurisdictionType("Common Law – DIFC");
//        portalApplicationPage.selectBusinessActivity("Blockchain Oracle");
//            portalApplicationPage.selectCompanyOwnedBy("Individual Shareholders");
//        portalApplicationPage.selectCompanyTypeForCustomer("Company Limited by Shares");
//        portalApplicationPage.selectCompany("Limited");
//        portalApplicationPage.selectCompanyOwnedBy("Individual Shareholders");
//        portalApplicationPage.selectJurisdictionType("Common Law – DIFC");
//        portalApplicationPage.clickPortalApplicationCTA("Save As Draft");
//        portalApplicationPage.clickPortalApplicationCTA("Continue");

        // ============================================================
        // 🕒 STEP 11: Handle Name Approval (Dynamic)
        // ============================================================
//        portalApplicationPage.shouldWaitNameApproval("No");

        // ============================================================
        // 💳 STEP 12: Select Payment Method and Process Payment
        // ============================================================
//        portalApplicationPage.selectPaymentMethod("Credit/Debit Card");
//        Thread.sleep(3000);
//            portalApplicationPage.selectPaymentMethod("Wire Transfer");

//        portalApplicationPage.clickPortalApplicationCTA("Proceed With Payment");
//        portalApplicationPage.enterPaymentDetails("41111111111111111", "12/30", "123", "Amol");

        // ============================================================
        // 👥 STEP 13: Enter Shareholder Details
        // ============================================================
//        portalApplicationPage.enterNumberOfShares();
//        portalApplicationPage.enterShareValue();
//        portalApplicationPage.clickAddShareholder("Individual");
//        portalApplicationPage.enterShareholderFname();
//        portalApplicationPage.enterShareholderLname();
//        portalApplicationPage.selectGender("Male");
//        portalApplicationPage.selectBirthCountry("India");
//        portalApplicationPage.enterShareholderPlaceOfBirth();
//        portalApplicationPage.selectNationality("India");
//        portalApplicationPage.enterShareholderPassportNum();
//        portalApplicationPage.enterDateOfBirth();
//        portalApplicationPage.enterPassportIssueDate();
//        portalApplicationPage.enterPassportExpiryDate();
//        portalApplicationPage.selectPassportIssueCountry("India");
//        portalApplicationPage.clickProceedButton();
//
//        portalApplicationPage.selectVisaType("No UAE Visa");
//        portalApplicationPage.clickProceedButton();
//        portalApplicationPage.enterShareholderPrimaryEmail("Primary Email");
//        portalApplicationPage.enterShareholderPrimaryMobileNum("Primary Phone");
//        portalApplicationPage.clickProceedButton();
//
//        // ============================================================
//        // 🏠 STEP 14: Enter Residential Information
//        // ============================================================
//        portalApplicationPage.selectResidentialCountry("India");
//        portalApplicationPage.selectResidentialProvince("Maharashtra");
//        portalApplicationPage.enterResidentialBuildingName();
//        portalApplicationPage.enterResidentialFlatNumber();
//        portalApplicationPage.enterResidentialStreetName();
//        portalApplicationPage.enterResidentialAreaName();
//        portalApplicationPage.enterResidentialPostalCode();
//        portalApplicationPage.enterResidentialCityVillage();
//        portalApplicationPage.selectYearsLiving();
//        portalApplicationPage.selectResidentialAddressCheckbox();
//        portalApplicationPage.clickProceedButton();
//
//        // ============================================================
//        // 🗳️ STEP 15: UBO, Roles, and Ownership Declaration
//        // ============================================================
//        portalApplicationPage.selectUBOCheckbox();
//        portalApplicationPage.selectVotingRightCheckbox();
//        portalApplicationPage.selectManagerCheckbox();
//        portalApplicationPage.selectDirectorCheckbox();
//        portalApplicationPage.selectAuthorizedSignatoryCheckbox();
//        portalApplicationPage.selectNatureOfOwnership("Control through other means e.g. holds decision or veto rights and /or controls the rights of others");
//        portalApplicationPage.enterOwnedShares();

        // ============================================================
        // ✅ STEP 16: Submit Shareholder Details
        // ============================================================
//        String shareholderSubSucMsg = portalApplicationPage.clickSubmitButton();
        logger.info("✅ Shareholder information submitted successfully.");

        // ============================================================
        // 📄 STEP 17: Document Upload
        // ============================================================
//        portalApplicationPage.clickPortalApplicationCTA("Continue");
//        portalApplicationPage.clickPortalApplicationCTA("Continue");
//        DocumentUploadPage documentUploadPage = new DocumentUploadPage(driver);
//        documentUploadPage.uploadDocumentsSequentially();

        // ============================================================
        // 🏁 STEP 18: Final Continuation
        // ============================================================
//        portalApplicationPage.clickPortalApplicationCTA("Continue");
//        Thread.sleep(3000);
            SignedDocumentPage signedDocumentPage= new SignedDocumentPage(driver);
            Thread.sleep(3000);
            signedDocumentPage.processAllSignedDocuments();

        logger.info("=== 🎉 Standard Incorporate Test Completed Successfully ===");

    } catch(
    Exception e)

    {
        logger.error("❌ Test failed due to unexpected error.", e);
        throw e;
    }
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
