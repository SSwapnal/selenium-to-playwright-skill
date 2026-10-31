package com.swapnal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By signInButton = By.xpath("//button[text()='Sign in']");
    private final By error = By.id("login-error");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void login(String user, String pass) {
        driver.findElement(username).clear();
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(signInButton).click();
    }

    public String errorText() {
        // The error is filled in after a short delay, so wait until it has text.
        wait.until(d -> !d.findElement(error).getText().isEmpty());
        return driver.findElement(error).getText();
    }

    public boolean isShown() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(signInButton)).isDisplayed();
    }
}
