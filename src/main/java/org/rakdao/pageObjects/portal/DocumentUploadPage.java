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

public class DocumentUploadPage extends ReusableUtil {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(DocumentUploadPage.class);

    @FindBy(xpath = "//c-dao-document")
    private List<WebElement> documentContainers;

    public DocumentUploadPage(WebDriver driver) throws AWTException {
        super(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("📄 DocumentUploadPage initialized");
    }

    /*public void uploadDocumentsSequentially() throws InterruptedException {
        logger.info("📄 Number of document containers found: {}", documentContainers.size());

        for (WebElement doc : documentContainers) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", doc);

                WebElement nameElement = doc.findElement(By.xpath(
                        ".//*[starts-with(name(), 'lightning-layout-item') and contains(@class,'dao-doc-name')]"));
                wait.until(ExpectedConditions.visibilityOf(nameElement));

                String docName = nameElement.getText().trim();
                logger.info("🧾 Processing document: {}", docName);

                // Expand if collapsed
                try {
                    nameElement.click();
                } catch (WebDriverException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nameElement);
                }

                // Upload buttons inside this specific document
                List<WebElement> uploadLabels = doc.findElements(By.xpath(
                        ".//label[contains(@for,'fileInput') and .//span[normalize-space()='upload']]"
                ));
                logger.info("📎 Found {} upload button(s) for {}", uploadLabels.size(), docName);

                // --- Special handling for Emirates ID ---
                if (docName.toLowerCase().contains("emirates id") && uploadLabels.size() >= 2) {
                    logger.info("🪪 Detected Emirates ID — uploading Front and Back sequentially.");

                    // Upload FRONT
                    uploadFileWithWait(uploadLabels.get(0), doc, docName + " - Front");

                    // Upload BACK (wait until first upload completes)
                    uploadFileWithWait(uploadLabels.get(1), doc, docName + " - Back");
                } else {
                    // Normal document flow (one upload button)
                    for (int i = 0; i < uploadLabels.size(); i++) {
                        uploadFileWithWait(uploadLabels.get(i), doc, docName + " [#" + (i + 1) + "]");
                    }
                }

            } catch (Exception e) {
                logger.error("❌ Error uploading document: {}", e.getMessage());
            }
        }
    }

    private void uploadFileWithWait(WebElement uploadLabel, WebElement doc, String fileDescription)
            throws InterruptedException {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(uploadLabel));

            // Click normally or via JS
            try {
                uploadLabel.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", uploadLabel);
            }

            logger.info("🖱️ Clicked upload button for {}", fileDescription);

            // Run AutoIT upload
            Runtime.getRuntime().exec(new String[]{
                    "C:\\Users\\Amol Aldar\\Desktop\\FilesUpload\\FileUploadScript.exe"
            });

            // Wait for upload confirmation
            WebElement successMsg = new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath(".//p[text()='Successfully Uploaded']")
                    ));

            if (successMsg.isDisplayed()) {
                logger.info("✅ File uploaded successfully for {}", fileDescription);
            }

            Thread.sleep(1000); // Small gap before next upload (UI sync)

        } catch (Exception e) {
            logger.error("⚠️ Failed to upload {}: {}", fileDescription, e.getMessage());
        }
    }*/

    public void uploadDocumentsSequentially() {
        logger.info("📄 Number of document containers: {}", documentContainers.size());

        for (WebElement doc : documentContainers) {
            try {
                scrollToElementSmooth(doc);
                waitForDomToSettle();

                WebElement nameElement = doc.findElement(By.xpath(
                        ".//*[starts-with(name(), 'lightning-layout-item') and contains(@class,'dao-doc-name')]"
                ));

                wait.until(ExpectedConditions.visibilityOf(nameElement));

                String docName = nameElement.getText().trim();
                logger.info("🧾 Processing document: {}", docName);

                expandDocumentSection(nameElement, docName);
                waitForDomToSettle();

                List<WebElement> uploadLabels = doc.findElements(By.xpath(
                        ".//label[contains(@for,'fileInput') and .//span[normalize-space()='upload']]"
                ));

                logger.info("📎 Found {} upload button(s) for {}", uploadLabels.size(), docName);

                if (docName.toLowerCase().contains("emirates id") && uploadLabels.size() >= 2) {
                    uploadFileWithWait(uploadLabels.get(0), doc, docName + " - Front");
                    uploadFileWithWait(uploadLabels.get(1), doc, docName + " - Back");
                } else {
                    int count = 1;
                    for (WebElement uploadLabel : uploadLabels) {
                        uploadFileWithWait(uploadLabel, doc, docName + " #" + (count++));
                    }
                }

                Thread.sleep(2500); // GAP before next document

            } catch (Exception e) {
                logger.error("❌ Error in processing document: {}", e.getMessage());
            }
        }
    }

    private void expandDocumentSection(WebElement nameElement, String docName) {
        try {
            logger.info("📂 Expanding document '{}'", docName);
            nameElement.click();
        } catch (Exception ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nameElement);
        }

        try {
            Thread.sleep(600); // Wait for accordion animation
        } catch (InterruptedException ignored) {}
    }

    private void uploadFileWithWait(WebElement uploadLabel, WebElement doc, String fileDescription)
            throws InterruptedException {

        try {
            wait.until(ExpectedConditions.elementToBeClickable(uploadLabel));

            try {
                uploadLabel.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", uploadLabel);
            }

            logger.info("🖱️ Clicked upload for {}", fileDescription);

            // AutoIT upload
            Process autoIt = Runtime.getRuntime().exec(
                    "C:\\Users\\Amol Aldar\\Desktop\\FilesUpload\\FileUploadScript.exe"
            );
            autoIt.waitFor();   // BLOCK until file is selected

            // Wait for success inside the same document container
            WebElement successMsg = new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(driver1 ->
                            doc.findElement(By.xpath(".//p[text()='Successfully Uploaded']"))
                    );

            if (successMsg.isDisplayed()) {
                logger.info("✅ Upload successful: {}", fileDescription);
            }

            Thread.sleep(1800); // allow UI to settle

        } catch (Exception e) {
            logger.error("⚠️ Upload failed for {}: {}", fileDescription, e.getMessage());
        }
    }

    private void waitForDomToSettle() throws InterruptedException {
        Thread.sleep(600);
    }



    public SignedDocumentPage clickAttentionDialogCTA(String buttonLabel) throws AWTException {
        By buttonLocator = By.xpath(String.format("//button[.//span[normalize-space()='%s']]", buttonLabel));

        try {
            // 1️⃣ Try normal click
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));
            button.click();
            logger.info("✅ Normal click successful for button: {}", buttonLabel);
        } catch (Exception e1) {
            logger.warn("⚠️ Normal click failed for button: {} — trying JS click...", buttonLabel, e1);

            try {
                // 2️⃣ Fallback: JS click
                WebElement button = driver.findElement(buttonLocator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                logger.info("✅ JS click successful for button: {}", buttonLabel);
            } catch (Exception e2) {
                logger.warn("⚠️ JS click failed for button: {} — checking for alert...", buttonLabel, e2);

                try {
                    // 3️⃣ If both clicks fail, accept alert
                    Alert alert = driver.switchTo().alert();
                    logger.info("⚠️ Alert found: '{}' — accepting it.", alert.getText());
                    alert.accept();
                } catch (NoAlertPresentException nae) {
                    logger.error("❌ No alert present, unable to click button: {}", buttonLabel);
                } catch (Exception e3) {
                    logger.error("❌ Unexpected error while handling alert for button {}: {}", buttonLabel, e3.getMessage(), e3);
                }
            }
        }
        return new SignedDocumentPage(driver);
    }


}
