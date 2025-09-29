package org.rakdao.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;

import java.time.Duration;

public class DriverFactory {

    private static final Logger log = LoggerUtil.getLogger(DriverFactory.class);
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    /**
     * Get current thread's driver
     */
    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    /**
     * Initialize driver based on browser type
     * @param browser chrome / edge / firefox
     */
    public static void initDriver(String browser) {
        WebDriver driver = null;
        switch (browser.toLowerCase()) {

            case "chrome":
                log.info("Setting up ChromeDriver using WebDriverManager...");
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications"); // block notifications
                driver = new ChromeDriver(chromeOptions);
                log.info("Chrome browser launched successfully.");
                break;

            case "edge":
                log.info("Setting up EdgeDriver using WebDriverManager...");
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-notifications"); // block notifications
                driver = new EdgeDriver(edgeOptions);
                log.info("Edge browser launched successfully.");
                break;

            case "firefox":
                log.info("Setting up FirefoxDriver using WebDriverManager...");
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("dom.webnotifications.enabled", false); // block notifications
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setProfile(profile);
                driver = new FirefoxDriver(firefoxOptions);
                log.info("Firefox browser launched successfully.");
                break;

            default:
                log.error("Unsupported browser: {}. Please use 'chrome', 'edge' or 'firefox'.", browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        tlDriver.set(driver);
        log.info("{} driver initialized and configured.", browser);
    }


     //Quit driver and remove from ThreadLocal

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            log.info("Quitting WebDriver...");
            tlDriver.get().quit();
            tlDriver.remove();
            log.info("WebDriver quit and removed from ThreadLocal.");
        } else {
            log.warn("Attempted to quit WebDriver, but it was null.");
        }
    }
}
