package org.rakdao.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // initialize elements
    }

    // Username field
    @FindBy(id = "username")
    private WebElement usernameField;

    // Password field
    @FindBy(id = "password")
    private WebElement passwordField;

    // Login/Submit button
    @FindBy(id = "Login")
    private WebElement loginButton;

    //Forgot password link
    @FindBy(id="forgot_password_link")
    WebElement forgotPassLink;


    // 🔹 Methods to interact with the elements
    public void enterUsername(String username) {
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        loginButton.click();
    }
}

