package com.swapnal.tests;

import com.swapnal.pages.LoginPage;
import com.swapnal.pages.NominationsPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginShowsNominations() {
        new LoginPage(driver, wait).login("scheduler", "Gas#2026");
        NominationsPage nominations = new NominationsPage(driver, wait);
        Assert.assertEquals(nominations.heading(), "Nominations");
        Assert.assertEquals(nominations.welcomeText(), "Welcome, scheduler");
    }

    @DataProvider(name = "badLogins")
    public Object[][] badLogins() {
        return new Object[][] {
            {"scheduler", "wrong", "Invalid username or password"},
            {"locked", "Gas#2026", "Your account is locked. Contact support."},
            {"", "Gas#2026", "Username is required"},
        };
    }

    @Test(dataProvider = "badLogins")
    public void badLoginShowsError(String user, String pass, String expected) {
        LoginPage login = new LoginPage(driver, wait);
        login.login(user, pass);
        Assert.assertEquals(login.errorText(), expected);
    }

    @Test
    public void logoutReturnsToSignIn() {
        LoginPage login = new LoginPage(driver, wait);
        login.login("scheduler", "Gas#2026");
        NominationsPage nominations = new NominationsPage(driver, wait);
        nominations.heading();
        nominations.logout();
        Assert.assertTrue(login.isShown());
    }
}
