package org.rakdao.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;

import java.io.File;
import java.time.Duration;

public class DriverFactory {

    private static final Logger log = LoggerUtil.getLogger(DriverFactory.class);
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void initDriver(String browser) {
        WebDriver driver = null;

        switch (browser.toLowerCase()) {

            case "chrome":
                log.info("🟢 Setting up ChromeDriver with persistent profile...");
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--start-maximized");

                // ✅ Use persistent user data directory to keep session/cookies
                String profilePath = "C:\\SeleniumChromeProfile";
                File profileDir = new File(profilePath);
                if (!profileDir.exists()) {
                    profileDir.mkdirs();
                    log.info("Created Chrome profile directory: {}", profileDir.getAbsolutePath());
                }
                chromeOptions.addArguments("user-data-dir=" + profilePath);

                // (Optional) Use a specific Chrome profile within user data dir
                // chromeOptions.addArguments("--profile-directory=Default");

                driver = new ChromeDriver(chromeOptions);
                driver.manage().window().setPosition(new Point(0, 0));
                driver.manage().window().setSize(new Dimension(1366, 768));
                log.info("✅ Chrome launched with persistent session at {}", profilePath);
                break;

            case "edge":
                log.info("🟢 Setting up EdgeDriver...");
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                log.info("✅ Edge browser launched successfully.");
                break;

            case "firefox":
                log.info("🟢 Setting up FirefoxDriver...");
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("dom.webnotifications.enabled", false);
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setProfile(profile);
                driver = new FirefoxDriver(firefoxOptions);
                log.info("✅ Firefox launched successfully.");
                break;

            default:
                log.error("❌ Unsupported browser: {}", browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        // Common setup
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        tlDriver.set(driver);
        log.info("{} driver initialized and ready.", browser);
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            log.info("🧹 Quitting WebDriver and cleaning up...");
            tlDriver.get().quit();
            tlDriver.remove();
            log.info("✅ WebDriver closed and removed from ThreadLocal.");
        } else {
            log.warn("⚠️ Attempted to quit WebDriver, but it was null.");
        }
    }
}
