package com.swapnal.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NominationsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heading = By.cssSelector("#nominations-view h1");
    private final By welcome = By.id("welcome");
    private final By logoutButton = By.id("logout-btn");
    private final By pipeline = By.id("pipeline");
    private final By quantity = By.id("quantity");
    private final By submitButton = By.id("submit-btn");
    private final By error = By.id("nom-error");
    private final By toast = By.id("toast");
    private final By rows = By.cssSelector("#nom-table tbody tr");
    private final By count = By.id("nom-count");

    public NominationsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public String heading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(heading)).getText();
    }

    public String welcomeText() {
        return driver.findElement(welcome).getText();
    }

    public void submit(String pipelineName, String qty) {
        new Select(driver.findElement(pipeline)).selectByVisibleText(pipelineName);
        driver.findElement(quantity).sendKeys(qty);
        driver.findElement(submitButton).click();
    }

    public String toastText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(toast)).getText();
    }

    public String errorText() {
        wait.until(d -> !d.findElement(error).getText().isEmpty());
        return driver.findElement(error).getText();
    }

    public void waitForRowCount(int expected) {
        wait.until(ExpectedConditions.numberOfElementsToBe(rows, expected));
    }

    public List<WebElement> rowCells(int rowIndex) {
        return driver.findElements(rows).get(rowIndex).findElements(By.tagName("td"));
    }

    public int rowCount() {
        return driver.findElements(rows).size();
    }

    public String countText() {
        return driver.findElement(count).getText();
    }

    public void logout() {
        driver.findElement(logoutButton).click();
    }
}
