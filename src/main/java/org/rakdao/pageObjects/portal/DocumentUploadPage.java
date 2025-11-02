package org.rakdao.pageObjects.portal;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("📄 DocumentUploadPage initialized");
    }

    /**
     * Upload a fixed document using AutoIT
     */
    public void uploadDocumentsSequentially() throws InterruptedException {
        logger.info("Number of document containers found: {}", documentContainers.size());

        for (WebElement doc : documentContainers) {
            try {
                // Scroll to document container
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", doc);

                // Locate document name
                WebElement nameElement = doc.findElement(By.xpath(
                        ".//*[starts-with(name(), 'lightning-layout-item') and contains(@class,'dao-doc-name')]"));
                wait.until(ExpectedConditions.visibilityOf(nameElement));

                logger.info("🧾 Processing document: {}", nameElement.getText());

                // Expand or focus document if needed
                try {
                    nameElement.click();
                } catch (WebDriverException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nameElement);
                }

                // ✅ Locate upload buttons inside this document only
                List<WebElement> uploadLabels = doc.findElements(By.xpath(
                        ".//label[contains(@for,'fileInput') and .//span[normalize-space()='upload']]"
                ));

                logger.info("Found {} upload button(s) for {}", uploadLabels.size(), nameElement.getText());

                for (WebElement uploadLabel : uploadLabels) {
                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(uploadLabel));

                        try {
                            uploadLabel.click();
                        } catch (Exception e) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", uploadLabel);
                        }

                        logger.info("✅ Clicked upload button for {}", nameElement.getText());

                        // Execute AutoIT for file upload
                        Runtime.getRuntime().exec(new String[]{
                                "C:\\Users\\Amol Aldar\\Desktop\\FilesUpload\\FileUploadScript.exe"
                        });

                        // Optional wait for upload completion
                        wait.until(ExpectedConditions.visibilityOf(
                                doc.findElement(By.xpath(".//p[text()='Successfully Uploaded']"))
                        ));
                        logger.info("📁 File successfully uploaded for {}", nameElement.getText());

                    } catch (Exception e) {
                        logger.error("⚠️ Failed to click upload button inside {}: {}", nameElement.getText(), e.getMessage());
                    }
                }

            } catch (Exception e) {
                logger.error("❌ Error uploading document: {}", e.getMessage());
            }
        }
    }

    public void clickAttentionDialogCTA(String buttonLabel) {
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
    }


}
