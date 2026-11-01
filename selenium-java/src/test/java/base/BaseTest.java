package base;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final String BASE_URL = "https://www.saucedemo.com/";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Set HEADLESS=true to run without opening a browser window.
        if ("true".equalsIgnoreCase(System.getenv("HEADLESS"))) options.addArguments("--headless=new");
        options.addArguments("--window-size=1440,1000");
        driver = new ChromeDriver(options); // Selenium Manager resolves the driver when needed.
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
