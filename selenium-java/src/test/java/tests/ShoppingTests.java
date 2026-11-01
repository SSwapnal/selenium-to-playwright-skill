package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;

public class ShoppingTests extends BaseTest {

    private void signIn() {
        new LoginPage(driver, wait).login("standard_user", "secret_sauce");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
    }

    @Test(description = "TC03 - Add backpack and verify cart")
    public void addProductToCart() {
        signIn();
        InventoryPage inventory = new InventoryPage(driver, wait);

        Assert.assertEquals(inventory.getTitle(), "Products");
        inventory.addBackpack();
        Assert.assertEquals(inventory.getCartCount(), "1",
                "Cart badge should show one item");
        inventory.openCart();

        CartPage cart = new CartPage(driver, wait);
        Assert.assertEquals(cart.getItemName(), "Sauce Labs Backpack");
    }

    @Test(description = "TC04 - Remove backpack from cart")
    public void removeProductFromCart() {
        signIn();
        InventoryPage inventory = new InventoryPage(driver, wait);
        inventory.addBackpack();
        inventory.openCart();

        CartPage cart = new CartPage(driver, wait);
        Assert.assertEquals(cart.getItemName(), "Sauce Labs Backpack");
        cart.removeItem();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("cart_item")));
        Assert.assertEquals(driver.findElements(By.className("cart_item")).size(), 0,
                "Cart should be empty after removing the backpack");
    }

    @Test(description = "TC05 - Complete backpack checkout")
    public void checkoutBackpack() {
        signIn();
        InventoryPage inventory = new InventoryPage(driver, wait);
        inventory.addBackpack();
        inventory.openCart();

        CartPage cart = new CartPage(driver, wait);
        Assert.assertEquals(cart.getItemName(), "Sauce Labs Backpack");
        cart.checkout();

        // Checkout information step
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")))
                .sendKeys("Swapnal");
        driver.findElement(By.id("last-name")).sendKeys("Tester");
        driver.findElement(By.id("postal-code")).sendKeys("94086");
        driver.findElement(By.id("continue")).click();

        // Wait for the order overview and verify the product before finishing.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        WebElement overviewItem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name"))
        );
        Assert.assertEquals(overviewItem.getText(), "Sauce Labs Backpack",
                "Backpack should appear in the checkout overview");

        driver.findElement(By.id("finish")).click();

        // The confirmation heading exists only after Finish completes the order.
        WebElement confirmation = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header"))
        );
        Assert.assertEquals(confirmation.getText(), "Thank you for your order!",
                "Successful checkout confirmation should be displayed");
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete.html"),
                "User should land on the checkout completion page");
    }
}
