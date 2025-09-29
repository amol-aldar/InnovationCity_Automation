package org.rakdao.utils;

import org.openqa.selenium.WebDriver;
import org.testng.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        log.info("Suite started: {}", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("Suite finished: {}", suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Test started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", result.getMethod().getMethodName());

        try {
            Object testClass = result.getInstance();
            WebDriver driver = (WebDriver) testClass.getClass().getDeclaredField("driver").get(testClass);
            String path = ScreenshotUtil.captureFailure(driver, result.getMethod().getMethodName());
            if (path != null) {
                log.info("Screenshot stored at: {}", path);
            }
        } catch (Exception e) {
            log.error("Could not capture screenshot for test: {}", result.getMethod().getMethodName(), e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("Test failed but within success percentage: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        log.error("Test failed due to timeout: {}", result.getMethod().getMethodName());
        onTestFailure(result); // capture screenshot too
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("Test context started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Test context finished: {}", context.getName());
    }
}
