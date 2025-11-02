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

import java.io.IOException;

public class StandardIncorporate extends BaseClass {
    private static final Logger log = LoggerUtil.getLogger(StandardIncorporate.class);

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
            leadPage.selectRecordType("Customer");
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
            Thread.sleep(1000);
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
            opportunityPage.clickAddSelectedInveButton();

            // 🏁 Opportunity closure
            opportunityPage.clickOpportunityStage("Closing");
            opportunityPage.clickOpportunityCompleteButton();
            log.info("✅ Product selection & Opportunity completion done.");
            ContactPage contactPage=opportunityPage.goToContactOrAccount("Primary Contact");
            PortalHomePage portalHomePage=contactPage.goToPortal();
            PortalApplicationPage portalApplicationPage=portalHomePage.clickContinueWithApplication();
            portalApplicationPage.clickPortalApplicationCTA("Save As Draft");
            portalApplicationPage.clickPortalApplicationCTA("Let’s Get Started");
            portalApplicationPage.enterBusinessNamePreferences("Ltd");
            portalApplicationPage.selectActivityGroup("Artificial Intelligence");
            portalApplicationPage.selectBusinessActivity("Digital Analytics Services");
            portalApplicationPage.selectCompanyType("Company Limited by Shares");
            portalApplicationPage.selectCompany("Limited");
            portalApplicationPage.selectCompanyOwnedBy("Individual Shareholders");
            portalApplicationPage.selectJurisdictionType("Common Law – DIFC");
            portalApplicationPage.clickPortalApplicationCTA("Save As Draft");
            portalApplicationPage.clickPortalApplicationCTA("Continue");
            portalApplicationPage.shouldWaitNameApproval("No");
            portalApplicationPage.selectPaymentMethod("Credit/Debit Card");
            portalApplicationPage.acceptTermsAndConditions();
            portalApplicationPage.clickPortalApplicationCTA("Proceed With Payment");
            portalApplicationPage.enterPaymentDetails("41111111111111111","12/30","123","Amol");

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
            portalApplicationPage.selectNatureOfOwnership("Control through other means e.g. holds decision or veto rights and /or controls the rights of others");
            portalApplicationPage.enterOwnedShares();
            Thread.sleep(5000);
            String shareholderSubSucMsg=portalApplicationPage.clickSubmitButton();
            softAssert.assertEquals(shareholderSubSucMsg,"Shareholder’s information saved");

            portalApplicationPage.clickPortalApplicationCTA("Continue");

            portalApplicationPage.clickPortalApplicationCTA("Continue");

            DocumentUploadPage documentUploadPage= new DocumentUploadPage(driver);
            documentUploadPage.uploadDocumentsSequentially();

            portalApplicationPage.clickPortalApplicationCTA("Continue");













            Thread.sleep(3000);
            log.info("=== 🎉 Standard Incorporate Test Completed Successfully ===");

        } catch (Exception e) {
            log.error("❌ Test failed due to unexpected error.", e);
            throw e;
        }
    }
}
