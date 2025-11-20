package org.rakdao.pageObjects.portal;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.rakdao.utils.ReusableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class SignedDocumentPage extends ReusableUtil {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(SignedDocumentPage.class);

    // ------------------------------
    // 🔍 LOCATORS
    // ------------------------------

    @FindBy(xpath = "//c-dao-document")
    private List<WebElement> documentContainers;

    private By checkbox = By.xpath(".//input[@type='checkbox']");
    private By documentTitle = By.xpath(".//lightning-layout-item[contains(@class,'dao-doc-name')]//slot");
    private By downloadButton = By.xpath(".//button[contains(@title,'Download')]");
    private By uploadSignedDocumentButton = By.xpath(".//button[contains(.,'Upload Signed Document')]");
    private By uploadSuccessMsg = By.xpath(".//p[text()='Successfully Uploaded']");
    By uploadButton = By.xpath("//label[contains(@for,'fileInput')]//button[@title='upload']");


    // ------------------------------
    // 🔧 CONSTRUCTOR
    // ------------------------------

    public SignedDocumentPage(WebDriver driver) throws AWTException {
        super(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
        logger.info("📄 SignedDocumentPage initialized");
    }

    // ------------------------------
    // 🚀 MAIN FUNCTION
    // ------------------------------

    /*public void processAllSignedDocuments() {
        logger.info("📑 Total documents found: {}", documentContainers.size());

        for (WebElement doc : documentContainers) {
            try {
                scrollIntoView(doc);

                String docName = getDocumentTitle(doc);
                logger.info("📝 Processing Document: {}", docName);

                expandDocument(doc);
                selectDownloadSignCheckbox(doc);
                downloadDocument(doc, docName);
                uploadSignedDocument(doc, docName);

                logger.info("✔ Completed: {}", docName);

            } catch (Exception e) {
                logger.error("❌ Error processing document: {}", e.getMessage());
            }
        }
    }*/

    public void processAllSignedDocuments() {
        List<WebElement> docs = driver.findElements(By.xpath("//c-dao-document"));

        logger.info("📑 Total documents found: {}", docs.size());

        for (int i = 0; i < docs.size(); i++) {
            WebElement doc = docs.get(i);

            try {
                scrollIntoView(doc);
                Thread.sleep(500);

                String docName = getDocumentTitle(doc);
                logger.info("📝 Processing Document: {}", docName);

                expandDocument(doc);
                selectDownloadSignCheckbox(doc);
                downloadDocument(doc, docName);
                uploadSignedDocument(doc, docName);

                logger.info("✔ Completed: {}", docName);

            } catch (Exception e) {
                logger.error("❌ Error processing {}: {}", i + 1, e.getMessage());
            }

            // Refresh live list for next iteration
            docs = driver.findElements(By.xpath("//c-dao-document"));
        }
    }


    // ------------------------------
    // 🟦 UTILITY METHODS
    // ------------------------------

    private String getDocumentTitle(WebElement doc) {
        WebElement title = doc.findElement(documentTitle);
        return title.getText().trim();
    }

    private void expandDocument(WebElement doc) {
        try {
            WebElement header = doc.findElement(By.xpath(".//lightning-layout-item[contains(@class,'dao-doc-name')]"));
            header.click();
            Thread.sleep(600);
        } catch (Exception ignored) {}
    }

    private void selectDownloadSignCheckbox(WebElement doc) {
        try {
            WebElement cb = doc.findElement(checkbox);
            if (!cb.isSelected()) {
                cb.click();
                logger.info("☑ Checkbox selected.");
            } else {
                logger.info("☑ Checkbox already selected.");
            }
        } catch (Exception e) {
            logger.error("⚠ Failed clicking checkbox: {}", e.getMessage());
        }
    }

    private void downloadDocument(WebElement doc, String docName) throws InterruptedException {
        try {
            WebElement dlBtn = doc.findElement(downloadButton);
            wait.until(ExpectedConditions.elementToBeClickable(dlBtn));
            dlBtn.click();
            logger.info("⬇ Download clicked for {}", docName);

            Thread.sleep(2500); // file download wait

        } catch (Exception e) {
            logger.error("❌ Download failed for {}: {}", docName, e.getMessage());
        }
    }

    private void uploadSignedDocument(WebElement doc, String docName) throws Exception {
        try {
            // 1️⃣ Click Upload Signed Document (always JS click for LWC)
            WebElement uploadSignedDocBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(doc.findElement(uploadSignedDocumentButton))
            );
            jsClick(uploadSignedDocBtn);
            logger.info("📤 'Upload Signed Document' clicked for {}", docName);

            // 2️⃣ Correct global upload button
            By uploadBtnXpath = By.xpath("//label[contains(@for,'fileInput')]//button[.//span[translate(., " +
                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='upload']]");

            WebElement uploadBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(uploadBtnXpath)
            );

            // 3️⃣ JS click (normal click fails in LWC)
            jsClick(uploadBtn);
            logger.info("⬆ Secondary upload button clicked");

            // 4️⃣ AutoIT
            Runtime.getRuntime().exec("C:\\Users\\Amol Aldar\\Desktop\\FilesUpload\\FileUploadScript.exe");

            // 5️⃣ Wait for success inside SAME container
            wait.until(ExpectedConditions.visibilityOf(doc.findElement(uploadSuccessMsg)));
            logger.info("✅ Upload successful for {}", docName);

        } catch (Exception e) {
            logger.error("⚠ Upload failed for {}: {}", docName, e.getMessage());
        }
    }


    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }



}

