package org.rakdao.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);

    public static String captureFailure(WebDriver driver, String testName) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = "test-output/screenshots/" + fileName;

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(filePath);
            dest.getParentFile().mkdirs();
            FileUtils.copyFile(src, dest);

            log.info("Screenshot captured: {}", filePath);
            return filePath;

        } catch (Exception e) {
            log.error("Failed to capture screenshot for test: {}", testName, e);
            return null;
        }
    }
}
