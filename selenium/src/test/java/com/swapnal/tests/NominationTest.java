package com.swapnal.tests;

import com.swapnal.pages.LoginPage;
import com.swapnal.pages.NominationsPage;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NominationTest extends BaseTest {
    private NominationsPage nominations;

    @BeforeMethod
    public void signIn() {
        new LoginPage(driver, wait).login("scheduler", "Gas#2026");
        nominations = new NominationsPage(driver, wait);
        nominations.heading();
    }

    @Test
    public void submitNominationAddsRow() {
        nominations.submit("Transco", "10000");
        Assert.assertEquals(nominations.toastText(), "Nomination N-1001 submitted");
        nominations.waitForRowCount(1);
        List<WebElement> cells = nominations.rowCells(0);
        Assert.assertEquals(cells.get(0).getText(), "N-1001");
        Assert.assertEquals(cells.get(1).getText(), "Transco");
        Assert.assertEquals(cells.get(2).getText(), "10,000");
        Assert.assertEquals(cells.get(3).getText(), "Submitted");
    }

    @Test
    public void zeroQuantityIsRejected() {
        nominations.submit("Texas Eastern", "0");
        Assert.assertEquals(nominations.errorText(), "Quantity must be greater than 0");
        Assert.assertEquals(nominations.rowCount(), 0);
    }

    @Test
    public void twoNominationsUpdateCount() {
        nominations.submit("Transco", "5000");
        nominations.waitForRowCount(1);
        nominations.submit("ANR Pipeline", "7500");
        nominations.waitForRowCount(2);
        Assert.assertEquals(nominations.countText(), "2 nominations");
    }
}
