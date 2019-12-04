package cz.upce.webapp.selenium.testframework.pages;

import cz.upce.webapp.selenium.testframework.PageObject;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Martin Volenec / st46661
 */

@PageObject
public class DashboardPage
{
    private String loginURL = "http://localhost:9000";
    LoginPage loginPage;

    @Autowired WebDriver driver;

    public String getTitle()
    {
        return "Dashboard";
    }
}