package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTests extends BaseTest {

    private final By productsHeading = By.cssSelector(".title");
    private final By loginButton = By.id("login-button");

    @Test(description = "TC01 - Verify login with valid credentials")
    public void validLogin() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("standard_user", "secret_sauce");

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productsHeading)
        );

        Assert.assertEquals(heading.getText(), "Products",
                "Products heading should appear after successful login");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                "User should be redirected to the inventory page");
    }

    @Test(description = "TC02 - Verify invalid credentials show an error")
    public void invalidLogin() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("standard_user", "wrong_password");

        String actualError = login.getErrorMessage();
        Assert.assertTrue(actualError.contains("Username and password do not match"),
                "A useful error message should be displayed for invalid credentials");

        // Check that the login form is still displayed. Avoid an overly strict URL
        // comparison because browsers may normalize the trailing slash.
        Assert.assertTrue(wait.until(
                        ExpectedConditions.visibilityOfElementLocated(loginButton)
                ).isDisplayed(),
                "User should remain on the login form after an unsuccessful login");
    }
}
