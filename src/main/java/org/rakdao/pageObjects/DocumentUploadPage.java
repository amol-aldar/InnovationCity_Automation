package org.rakdao.pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class DocumentUploadPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(DocumentUploadPage.class);

    @FindBy(xpath = "//c-dao-document")
    private List<WebElement> documentContainers;

    public DocumentUploadPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
        logger.info("📄 DocumentUploadPage initialized");
    }

    /**
     * Sequentially upload documents from a folder.
     * If only one file exists, reuse it for all documents.
     */
    public void uploadDocumentsSequentially(String folderPath) throws InterruptedException {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            logger.error("Folder path is invalid: {}", folderPath);
            return;
        }

        File[] files = folder.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            logger.error("No files found in folder: {}", folderPath);
            return;
        }

        boolean singleFileMode = files.length == 1;
        File singleFile = singleFileMode ? files[0] : null;

        logger.info("Found {} files. Single file mode: {}", files.length, singleFileMode);

        Thread.sleep(2000);

        logger.info("Number of document containers found: {}", documentContainers.size());

        for (WebElement doc : documentContainers) {
            try {
                // Scroll to document container
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", doc);

                // Locate document name element
                WebElement nameElement = doc.findElement(By.xpath(".//*[starts-with(name(), 'lightning-layout-item') and contains(@class,'dao-doc-name')]"));
                wait.until(ExpectedConditions.visibilityOf(nameElement));

                // Try standard click first, fallback to JS click
                try {
                    nameElement.click();
                    logger.info("Clicked on document (standard click): {}", nameElement.getText().trim());
                } catch (WebDriverException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nameElement);
                    logger.info("Clicked on document (JS click fallback): {}", nameElement.getText().trim());
                }

                // Wait for upload button to be visible
                WebElement uploadButton = wait.until(ExpectedConditions.visibilityOf(
                        doc.findElement(By.xpath(".//button[contains(@class,'dao-button') and span[text()='upload']]"))
                ));
                uploadButton.click();
                // Determine which file to upload
                File fileToUpload;
                String docName = nameElement.getText().trim();
                if (singleFileMode) {
                    fileToUpload = singleFile;
                } else {
                    fileToUpload = null;
                    for (File f : files) {
                        if (f.getName().contains(docName.replace("*", ""))) {
                            fileToUpload = f;
                            break;
                        }
                    }
                    if (fileToUpload == null) {
                        fileToUpload = files[0];
                        logger.warn("No exact match for '{}', using '{}' instead", docName, fileToUpload.getName());
                    }
                }

                // Send file to input
                WebElement fileInput = doc.findElement(By.cssSelector("input[type='file']"));
                fileInput.sendKeys(fileToUpload.getAbsolutePath());
                logger.info("⬆️ File selected for document: {}", docName);


                logger.info("Uploading document: {}", docName);

                // Wait for upload to complete
                waitUntilUploadComplete(doc);
                logger.info("✅ Upload completed for document: {}", docName);

            } catch (Exception e) {
                logger.error("Error uploading document '{}': {}", doc.getText(), e.getMessage());
            }
        }
    }

    private void waitUntilUploadComplete(WebElement docContainer) {
        try {
            wait.until(ExpectedConditions.visibilityOf(
                    docContainer.findElement(By.xpath(".//p[text()='Successfully Uploaded']"))
            ));
        } catch (TimeoutException e) {
            logger.warn("Upload may not have completed in expected time for {}", docContainer.getText());
        }
    }
}
