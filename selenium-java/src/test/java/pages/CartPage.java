package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By itemName = By.className("inventory_item_name");
    private final By removeButton = By.id("remove-sauce-labs-backpack");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public String getItemName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(itemName)).getText();
    }

    public void removeItem() {
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
    }

    public void checkout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
    }
}
