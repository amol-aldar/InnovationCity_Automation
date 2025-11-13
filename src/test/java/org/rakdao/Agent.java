package org.rakdao;

import org.rakdao.base.BaseClass;
import org.rakdao.pageObjects.portal.DocumentUploadPage;
import org.rakdao.pageObjects.portal.PortalApplicationPage;
import org.rakdao.pageObjects.portal.PortalHomePage;
import org.rakdao.pageObjects.salesforce.*;
import org.rakdao.utils.ConfigReader;
import org.rakdao.utils.LoggerUtil;
import org.rakdao.utils.User;
import org.rakdao.utils.UserGenerator;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Agent extends BaseClass {

    private static final Logger log = LoggerUtil.getLogger(Agent.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private LeadPage leadPage;
    private OpportunityPage opportunityPage;

    SoftAssert softAssert = new SoftAssert();



    // ==========================================
    // 🧪 Main Test: Agent Onboarding Flow
    // ==========================================
    @Test
    public void AgentOnboarding() throws IOException, InterruptedException, AWTException {
        try {
            log.info("=== 🚀 Starting Agent Onboarding Test ===");

            // -------------------------------------------------
            // 🧩 STEP 1: Generate Test Data
            // -------------------------------------------------
            log.info("🔧 Generating test data...");
            User user = UserGenerator.generateUser();
            log.info("✅ Test user generated: {} {}", user.getFirstName(), user.getLastName());

            // -------------------------------------------------
            // 🔐 STEP 2: Login to Salesforce
            // -------------------------------------------------
            log.info("🔐 Logging into Salesforce...");
            loginPage = new LoginPage(driver);
            loginPage.enterUsername(ConfigReader.get("adminUserName"));
            loginPage.enterPassword(ConfigReader.get("adminPassword"));
            loginPage.clickLogin();
            log.info("✅ Successfully logged into Salesforce.");

            // -------------------------------------------------
            // 🏠 STEP 3: Navigate to Leads and Create New Lead
            // -------------------------------------------------
            log.info("🧭 Navigating to Leads tab...");
            homePage = new HomePage(driver);
            homePage.clickNavigationTab("leads");
            homePage.clickNewLeadButton();
            leadPage = homePage.goToLeadPage();
            log.info("✅ Opened new Lead creation form.");

            // -------------------------------------------------
            // 🧾 STEP 4: Enter Lead Details
            // -------------------------------------------------
            log.info("✍️ Entering lead details...");
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
            leadPage.selectRoleDetailsCheckbox("CSP", true);
            leadPage.selectRoleDetailsCheckbox("Referral Partner", true);
            leadPage.selectRoleDetailsCheckbox("Referral Sponsor", false);
            leadPage.clickRibbonCta("Save");
            log.info("✅ Lead details saved successfully.");

            // -------------------------------------------------
            // 🔄 STEP 5: Convert Lead to Opportunity
            // -------------------------------------------------
            log.info("🔄 Converting lead to opportunity...");
            leadPage.convertLeadToOpportunity("Converted");
            leadPage.clickMarkStageComplete();
            leadPage.handleLeadConversionSection("Account");
            leadPage.handleLeadConversionSection("Opportunity");
            Thread.sleep(2000);
            leadPage.clickLeadModalCta("Convert");
            log.info("✅ Lead converted successfully.");

            // -------------------------------------------------
            // 💼 STEP 6: Open Opportunity and Progress Stage
            // -------------------------------------------------
            log.info("💼 Opening converted Opportunity...");
            Thread.sleep(2000);
            opportunityPage = leadPage.goToAccountContactOpportunity("Opportunity");
            opportunityPage.clickOpportunityStage("Developing");
            String successMsgText = opportunityPage.clickOpportunityCompleteButton();
            softAssert.assertEquals(successMsgText, "Stage changed successfully.");
            log.info("✅ Opportunity stage progressed successfully.");

            // -------------------------------------------------
            // 👤 STEP 7: Navigate to Contact → Portal Access
            // -------------------------------------------------
            log.info("👤 Navigating to Primary Contact portal access...");
            ContactPage contactPage = opportunityPage.goToContactOrAccount("Primary Contact");
            PortalHomePage portalHomePage = contactPage.goToPortal();
            PortalApplicationPage portalApplicationPage = portalHomePage.clickStartNowButton();
            log.info("✅ Portal launched successfully.");

            // -------------------------------------------------
            // 🏢 STEP 8: Fill Company & Bank Details
            // -------------------------------------------------
            log.info("🏢 Filling company and bank details...");
            portalApplicationPage.fillCompanyDetails("India", "Company Limited by Shares");
            portalApplicationPage.fillBankDetails();
            portalApplicationPage.clickPortalApplicationCTA("Save As Draft");
            portalApplicationPage.clickPortalApplicationCTA("Continue");
            log.info("✅ Company and bank details saved.");

            // -------------------------------------------------
            // 👥 STEP 9: Add Shareholder Details
            // -------------------------------------------------
            log.info("👥 Adding shareholder details...");
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
            log.info("✅ Shareholder personal details completed.");

            // -------------------------------------------------
            // 🛂 STEP 10: Visa and Contact Information
            // -------------------------------------------------
            log.info("🛂 Entering visa and contact information...");
            portalApplicationPage.selectVisaType("No UAE Visa");
            portalApplicationPage.clickProceedButton();
            portalApplicationPage.enterShareholderPrimaryEmail("Primary Email");
            portalApplicationPage.enterShareholderPrimaryMobileNum("Primary Phone");
            portalApplicationPage.clickProceedButton();
            log.info("✅ Contact information entered.");

            // -------------------------------------------------
            // 🏠 STEP 11: Residential Address
            // -------------------------------------------------
            log.info("🏠 Filling residential address...");
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
            log.info("✅ Residential details saved.");

            // -------------------------------------------------
            // 🧾 STEP 12: Ownership & Roles
            // -------------------------------------------------
            log.info("🧾 Selecting ownership and role details...");
            portalApplicationPage.selectUBOCheckbox();
            portalApplicationPage.selectVotingRightCheckbox();
            portalApplicationPage.selectManagerCheckbox();
            portalApplicationPage.selectDirectorCheckbox();
            portalApplicationPage.selectAuthorizedSignatoryCheckbox();
            portalApplicationPage.selectNatureOfOwnership("As a Nominee");
            portalApplicationPage.enterOwnedShares();
            Thread.sleep(2000);
            portalApplicationPage.clickSubmitButton();
            log.info("✅ Ownership and role section submitted.");

            // -------------------------------------------------
            // 📄 STEP 13: Document Upload & Final Submission
            // -------------------------------------------------
            log.info("📄 Uploading required documents...");
            DocumentUploadPage documentUploadPage = new DocumentUploadPage(driver);
            documentUploadPage.uploadDocumentsSequentially();
            portalApplicationPage.clickPortalApplicationCTA("Continue");
            documentUploadPage.clickAttentionDialogCTA("OKAY");
            log.info("✅ Document upload completed successfully.");

            // -------------------------------------------------
            // ✅ STEP 14: Assertions & Wrap-up
            // -------------------------------------------------
            softAssert.assertAll();
            log.info("🎯 Test completed successfully: Agent Onboarding flow passed.");

        } catch (Exception e) {
            log.error("❌ Test failed due to unexpected error.", e);
            throw e;
        }
    }
}
