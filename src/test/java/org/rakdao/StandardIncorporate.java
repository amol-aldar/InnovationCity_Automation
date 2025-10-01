package org.rakdao;

import org.openqa.selenium.WebDriver;
import org.rakdao.base.BaseClass;
import org.rakdao.pageObjects.HomePage;
import org.rakdao.pageObjects.LoginPage;
import org.rakdao.pageObjects.OpportunityPage;
import org.rakdao.utils.ConfigReader;
import org.rakdao.utils.LoggerUtil;
import org.rakdao.utils.User;
import org.rakdao.utils.UserGenerator;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;

public class StandardIncorporate extends BaseClass {
    private static final Logger log = LoggerUtil.getLogger(StandardIncorporate.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private OpportunityPage opportunityPage;

    @Test
    public void newStandardIncorporate() throws IOException, InterruptedException {
        try {
            log.info("=== Starting Standard Incorporate Test ===");

            // Generate random user data
            User user = UserGenerator.generateUser();
            log.info("Generated test user: {} {}", user.getFirstName(), user.getLastName());

            // Login
            loginPage = new LoginPage(driver);
            loginPage.enterUsername(ConfigReader.get("adminUserName"));
            loginPage.enterPassword(ConfigReader.get("adminPassword"));
            loginPage.clickLogin();
            log.info("Login successful.");

            // Navigate to Leads and create new Lead
            homePage = new HomePage(driver);
            homePage.clickOnNavigationBar("leadsTab");
            homePage.clickNewLeadButton("newButton");
            homePage.selectRecordType("Customer");
            homePage.clickCta("Next");

            // Fill lead details
            homePage.enterFirstName(user.getFirstName());
            homePage.enterLastName(user.getLastName());
            homePage.selectNationality("India");
            homePage.selectEntityType("Standard Company");
            homePage.selectActivityGroup("Blockchain Development, DLT services & Software");
            homePage.enterCompanyName(user.getCompany());
            homePage.enterMobileNumber(user.getMobile());
            homePage.enterEmail(user.getEmail());
            homePage.clickRibbonCta("Save");

            // Convert lead to opportunity
            homePage.convertLeadToOpportunity("Converted");
            homePage.clickMarkStageComplete();
            homePage.clickLeadModalCta("Convert");
            log.info("Lead converted successfully.");

            // Go to opportunity and add product
            opportunityPage = homePage.goToAccountContactOpportunity("Opportunity");
            opportunityPage.clickAddProduct();
            opportunityPage.goToProductListingModal("Save");
            opportunityPage.chooseProductFromStandardBook("Standard Company / 1 visa / 1 year");
            opportunityPage.clickOnCta("Next");
            log.info("Product selection completed.");
            Thread.sleep(3000);

            log.info("=== Standard Incorporate Test Completed Successfully ===");

        } catch (Exception e) {
            log.error("❌ Test failed due to unexpected error.", e);
            throw e;
        }
    }
}
