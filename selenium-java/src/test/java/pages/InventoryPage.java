package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InventoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By title = By.cssSelector(".title");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By backpackAdd = By.id("add-to-cart-sauce-labs-backpack");
    private final By backpackRemove = By.id("remove-sauce-labs-backpack");

    public InventoryPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText();
    }

    public void addBackpack() {
        wait.until(ExpectedConditions.elementToBeClickable(backpackAdd)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
    }

    public void removeBackpack() {
        wait.until(ExpectedConditions.elementToBeClickable(backpackRemove)).click();
    }

    public String getCartCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
    }

    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkout")));
    }
}
