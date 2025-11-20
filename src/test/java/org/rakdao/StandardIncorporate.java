package org.rakdao;

import org.rakdao.base.BaseClass;
import org.rakdao.pageObjects.portal.DocumentUploadPage;
import org.rakdao.pageObjects.portal.PortalApplicationPage;
import org.rakdao.pageObjects.portal.PortalHomePage;
import org.rakdao.pageObjects.portal.SignedDocumentPage;
import org.rakdao.pageObjects.salesforce.*;
import org.rakdao.utils.*;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.*;
import java.io.IOException;

public class StandardIncorporate extends BaseClass {
    private static final Logger log = LoggerUtil.getLogger(StandardIncorporate.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private LeadPage leadPage;
    private OpportunityPage opportunityPage;
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void newStandardIncorporate() throws IOException, InterruptedException, AWTException {
        try {
            log.info("=== 🚀 Starting Standard Incorporate Test ===");

            // ============================================================
            // 🧩 STEP 1: Generate random test data
            // ============================================================
            User user = UserGenerator.generateUser();
            log.info("Generated test user: {} {}", user.getFirstName(), user.getLastName());

            // ============================================================
            // 🔐 STEP 2: Login to Salesforce
            // ============================================================
            loginPage = new LoginPage(driver);
            loginPage.enterUsername(ConfigReader.get("adminUserName"));
            loginPage.enterPassword(ConfigReader.get("adminPassword"));
            loginPage.clickLogin();
            log.info("✅ Login successful.");

            // ============================================================
            // 🏠 STEP 3: Navigate to Leads tab and create a new Lead
            // ============================================================
            homePage = new HomePage(driver);
            homePage.clickNavigationTab("leads");  // fetched from NavigationBar.json
            homePage.clickNewLeadButton();         // opens Lead modal
            leadPage = homePage.goToLeadPage();    // switch control to LeadPage

            // ============================================================
            // 🧾 STEP 4: Fill in Lead details
            // ============================================================
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

            // ============================================================
            // 🔄 STEP 5: Convert Lead to Opportunity
            // ============================================================
            Thread.sleep(2000);
            leadPage.convertLeadToOpportunity("Converted");
            leadPage.clickMarkStageComplete();
            leadPage.clickLeadModalCta("Convert");
            log.info("✅ Lead converted successfully.");

            // ============================================================
            // 💼 STEP 6: Open Opportunity and add product
            // ============================================================
            opportunityPage = leadPage.goToAccountContactOpportunity("Opportunity");
            opportunityPage.clickAddProduct();
            opportunityPage.goToProductListingModal("Save");
            Thread.sleep(2000);
            opportunityPage.chooseProductFromStandardBook("Standard Company / 1 visa / 1 year");
            opportunityPage.clickOnCta("Next");
            Thread.sleep(2000);
            opportunityPage.clickEditProductModalCta("Save");
            opportunityPage.clickAddInventoryButton();

            // ============================================================
            // 🏢 STEP 7: Select Inventory (based on entity and customer type)
            // ============================================================
            opportunityPage.ensureEntityType("Standard Company");
            opportunityPage.ensureCustomerLookingFor("Co-Working Space");
            opportunityPage.ensureResourceType("Shared Desk");
            opportunityPage.clickGetInventoryButton();
            opportunityPage.selectSpecificInventory();
            opportunityPage.selectInventoryByRentalAmount("3000");
            opportunityPage.clickAddSelectedInveButton();

            // ============================================================
            // 🏁 STEP 8: Close Opportunity and move to Portal
            // ============================================================
            opportunityPage.clickOpportunityStage("Closing");
            opportunityPage.clickOpportunityCompleteButton();
            log.info("✅ Product selection & Opportunity completion done.");
            ServiceRequestPage serviceRequestPage=opportunityPage.goToServiceRequest("Service Requests");

            // ============================================================
            // 🌐 STEP 9: Open Portal and continue application
            // ============================================================
//          ContactPage contactPage = opportunityPage.goToContactOrAccount("Primary Contact");
            ContactPage contactPage = serviceRequestPage.goToContactOrAccount("Contact");
            PortalHomePage portalHomePage = contactPage.goToPortal();
            PortalApplicationPage portalApplicationPage = portalHomePage.clickContinueWithApplication();

            // ============================================================
            // 🧭 STEP 10: Fill Business Information in Portal
            // ============================================================
            portalApplicationPage.clickPortalApplicationCTA("Save As Draft");
            portalApplicationPage.clickPortalApplicationCTA("Let’s Get Started");
            portalApplicationPage.enterBusinessNamePreferences("Ltd");
//          portalApplicationPage.selectActivityGroup("Artificial Intelligence");
            portalApplicationPage.selectBusinessActivity("Blockchain Oracle");
            portalApplicationPage.selectCompanyTypeForCustomer("Company Limited by Shares");
            portalApplicationPage.selectCompany("Limited");
            portalApplicationPage.selectCompanyOwnedBy("Both");
            portalApplicationPage.selectJurisdictionType("Civil Law");
            portalApplicationPage.clickPortalApplicationCTA("Save As Draft");
            portalApplicationPage.clickPortalApplicationCTA("Continue");

            // ============================================================
            // 🕒 STEP 11: Handle Name Approval (Dynamic)
            // ============================================================
            portalApplicationPage.shouldWaitNameApproval("No");
            serviceRequestPage.expandBackOfficeAccordion("Standard Company Incorporation Assessment");
            serviceRequestPage.clickBackOfficeInternalCompleteStep("Review Company Names");
            serviceRequestPage.clickCtaButton("Next");
            serviceRequestPage.selectFirstCompanyName();
            serviceRequestPage.selectApproverDecision("Approve");
            serviceRequestPage.clickCtaButton("Next");
            serviceRequestPage.clickCtaButton("Submit");
            serviceRequestPage.clickCtaButton("Finish");
            serviceRequestPage.switchToWindowByIndex(1);

            // ============================================================
            // 💳 STEP 12: Select Payment Method and Process Payment
            // ============================================================
            portalApplicationPage.selectPaymentMethod("Credit/Debit Card");
            portalApplicationPage.clickPortalApplicationCTA("Proceed With Payment");
            portalApplicationPage.enterPaymentDetails("41111111111111111", "12/30", "123", "Amol");

            // ============================================================
            // 👥 STEP 13: Enter Shareholder Details
            // ============================================================
            portalApplicationPage.enterNumberOfShares();
            portalApplicationPage.enterShareValue();
            portalApplicationPage.addOnlyIndividualShareholder("Individual","Male","India","India","India");
//            portalApplicationPage.addOnlyCorporateShareholder("Corporate","Male","India","India","India");


            portalApplicationPage.selectVisaType("No UAE Visa");
            portalApplicationPage.clickProceedButton();
            portalApplicationPage.enterShareholderPrimaryEmail("Primary Email");
            portalApplicationPage.enterShareholderPrimaryMobileNum("Primary Phone");
            portalApplicationPage.clickProceedButton();

            // ============================================================
            // 🏠 STEP 14: Enter Residential Information
            // ============================================================
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

            // ============================================================
            // 🗳️ STEP 15: UBO, Roles, and Ownership Declaration
            // ============================================================
            portalApplicationPage.selectUBOCheckbox();
            portalApplicationPage.selectVotingRightCheckbox();
            portalApplicationPage.selectManagerCheckbox();
            portalApplicationPage.selectDirectorCheckbox();
            portalApplicationPage.selectAuthorizedSignatoryCheckbox();
            portalApplicationPage.selectNatureOfOwnership("Control through other means e.g. holds decision or veto rights and /or controls the rights of others");
            portalApplicationPage.enterOwnedShares();

            // ============================================================
            // ✅ STEP 16: Submit Shareholder Details
            // ============================================================
//            Thread.sleep(5000);
            String shareholderSubSucMsg = portalApplicationPage.clickSubmitButton();
            softAssert.assertEquals(shareholderSubSucMsg, "Shareholder’s information saved");
            log.info("✅ Shareholder information submitted successfully.");

            // ============================================================
            // 📄 STEP 17: Document Upload
            // ============================================================
            portalApplicationPage.clickPortalApplicationCTA("Continue");
            DocumentUploadPage documentUploadPage =portalApplicationPage.clickPortalApplicationCTA("Continue");
            documentUploadPage.uploadDocumentsSequentially();
            driver.navigate().refresh();

            // ============================================================
            // 🏁 STEP 18: Final Continuation
            // ============================================================
            portalApplicationPage.clickPortalApplicationCTA("Continue");
            SignedDocumentPage signedDocumentPage=documentUploadPage.clickAttentionDialogCTA("OKAY");
            Thread.sleep(3000);
            signedDocumentPage.processAllSignedDocuments();

            log.info("=== 🎉 Standard Incorporate Test Completed Successfully ===");

        } catch (Exception e) {
            log.error("❌ Test failed due to unexpected error.", e);
            throw e;
        }
    }
}
