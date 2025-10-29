package org.rakdao;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.rakdao.base.BaseClass;
import org.rakdao.pageObjects.*;
import org.rakdao.utils.ConfigReader;
import org.rakdao.utils.LoggerUtil;
import org.rakdao.utils.User;
import org.rakdao.utils.UserGenerator;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

public class Agent extends BaseClass {
    private static final Logger log = LoggerUtil.getLogger(Agent.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private LeadPage leadPage;
    private OpportunityPage opportunityPage;
    SoftAssert softAssert= new SoftAssert();

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

            leadPage.selectNationality("India");
            leadPage.selectRoleDetailsCheckbox("CSP", true);               // ✅ checks CSP
            leadPage.selectRoleDetailsCheckbox("Referral Partner", true);  // ✅ checks it
            leadPage.selectRoleDetailsCheckbox("Referral Sponsor", false); // ✅ unchecks it

            leadPage.clickRibbonCta("Save");
            log.info("✅ Lead details entered and saved.");

            leadPage.convertLeadToOpportunity("Converted");
            leadPage.clickMarkStageComplete();
            // 🔄 Convert Lead → Opportunity
            leadPage.handleLeadConversionSection("Account");// Selects “Create New Account” + “Channel Partner”
            leadPage.handleLeadConversionSection("Opportunity");  // Selects “Create New Opportunity” + “Agent Onboarding”
            Thread.sleep(2000);
            leadPage.clickLeadModalCta("Convert");
            log.info("✅ Lead converted successfully.");


            // 💼 Open opportunity and continue
            Thread.sleep(2000);
            opportunityPage = leadPage.goToAccountContactOpportunity("Opportunity");


            // 🏁 Opportunity closure
            opportunityPage.clickOpportunityStage("Developing");
            String successMsgText=opportunityPage.clickOpportunityCompleteButton();
            softAssert.assertEquals(successMsgText,"Stage changed successfully.");
            log.info("✅ Opportunity closing successfully ");
            ContactPage contactPage=opportunityPage.goToContactOrAccount("Primary Contact");
            PortalApplicationPage portalApplicationPage=contactPage.goToPortal();
            portalApplicationPage.clickStartNowButton();
            portalApplicationPage.fillCompanyDetails("Afghanistan","Company Limited by Shares");
            portalApplicationPage.fillBankDetails();
            portalApplicationPage.clickSaveInfoButton();
            portalApplicationPage.clickContinueButton();
            portalApplicationPage.enterNumberOfShares();
            portalApplicationPage.enterShareValue();
            portalApplicationPage.clickAddShareholder("Individual Shareholder");
            softAssert.assertAll();


        } catch (Exception e) {
            log.error("❌ Test failed due to unexpected error.", e);
            throw e;
        }
    }


}
