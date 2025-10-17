package org.rakdao;

import org.rakdao.base.BaseClass;
import org.rakdao.pageObjects.HomePage;
import org.rakdao.pageObjects.LeadPage;
import org.rakdao.pageObjects.LoginPage;
import org.rakdao.pageObjects.OpportunityPage;
import org.rakdao.utils.ConfigReader;
import org.rakdao.utils.LoggerUtil;
import org.rakdao.utils.User;
import org.rakdao.utils.UserGenerator;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;

public class Agent extends BaseClass {
    private static final Logger log = LoggerUtil.getLogger(Agent.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private LeadPage leadPage;
    private OpportunityPage opportunityPage;

    @Test
    public void newStandardIncorporate() throws IOException, InterruptedException {
        try {
            log.info("=== 🚀 Starting Standard Incorporate Test ===");

            // 🧩 Generate random test data
            User user = UserGenerator.generateUser();
            log.info("Generated test user: {} {}", user.getFirstName(), user.getLastName());

            // 🔐 Login
            loginPage = new LoginPage(driver);
            loginPage.enterUsername(ConfigReader.get("adminUserName"));
            loginPage.enterPassword(ConfigReader.get("adminPassword"));
            loginPage.clickLogin();
            log.info("✅ Login successful.");

            // 🏠 Navigate to Leads tab
            homePage = new HomePage(driver);
            homePage.clickNavigationTab("leads");  // from NavigationBar.json
            homePage.clickNewLeadButton();                // opens Lead modal
            leadPage = homePage.goToLeadPage();     // switch control to LeadPage

            // 🧾 Lead creation steps
            leadPage.selectRecordType("Channel Partner");
            leadPage.clickNext("Next");
            leadPage.enterLeadDetails(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCompany(),
                    user.getEmail(),
                    user.getMobile()
            );
            leadPage.selectEntityType("Standard Company");
            leadPage.selectActivityGroup("Blockchain Development, DLT services & Software");
            leadPage.selectNationality("India");

            leadPage.clickRibbonCta("Save");
            log.info("✅ Lead details entered and saved.");

            // 🔄 Convert Lead → Opportunity
            leadPage.convertLeadToOpportunity("Converted");
            leadPage.clickMarkStageComplete();
            leadPage.clickLeadModalCta("Convert");
            log.info("✅ Lead converted successfully.");

            // 💼 Open opportunity and continue
            opportunityPage = leadPage.goToAccountContactOpportunity("Opportunity");
            opportunityPage.clickAddProduct();
            opportunityPage.goToProductListingModal("Save");
            opportunityPage.chooseProductFromStandardBook("Standard Company / 1 visa / 1 year");
            opportunityPage.clickOnCta("Next");
            Thread.sleep(2000);
            opportunityPage.clickEditProductModalCta("Save");
            Thread.sleep(2000);
            opportunityPage.clickAddInventoryButton();

            // 🏢 Inventory selection logic
            opportunityPage.ensureEntityType("Standard Company");
            opportunityPage.ensureCustomerLookingFor("Co-Working Space");
            opportunityPage.ensureResourceType("Shared Desk");
            opportunityPage.clickGetInventoryButton();
            opportunityPage.selectSpecificInventory();
            opportunityPage.selectInventoryByRentalAmount("3000");

            // 🏁 Opportunity closure
            opportunityPage.clickOpportunityStage("Closing");
            opportunityPage.clickOpportunityCompleteButton();
            log.info("✅ Product selection & Opportunity completion done.");

            Thread.sleep(3000);
            log.info("=== 🎉 Standard Incorporate Test Completed Successfully ===");

        } catch (Exception e) {
            log.error("❌ Test failed due to unexpected error.", e);
            throw e;
        }
    }
}
