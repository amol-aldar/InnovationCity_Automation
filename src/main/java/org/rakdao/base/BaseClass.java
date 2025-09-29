package org.rakdao.base;

import org.openqa.selenium.WebDriver;
import org.rakdao.pageObjects.HomePage;
import org.rakdao.utils.ConfigReader;
import org.rakdao.utils.DriverFactory;
import org.rakdao.utils.LoggerUtil;

import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseClass {
    private static final Logger log = LoggerUtil.getLogger(BaseClass.class);
    protected WebDriver driver;
    String url;
    //protected HomePage homePage;
    @BeforeMethod
    public void setUp() {
        log.info("Initializing WebDriver...");
        DriverFactory.initDriver(ConfigReader.get("browser"));
        driver = DriverFactory.getDriver();
        log.info("WebDriver initialized successfully.");
        url=ConfigReader.get("baseUrl");
        driver.get(url);
        //homePage = new HomePage(DriverFactory.getDriver());
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        log.info("Quitting WebDriver...");
        DriverFactory.quitDriver();
        log.info("WebDriver quit completed.");
    }
}
