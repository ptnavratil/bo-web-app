package cz.upce.webapp.selenium.testframework.pages;

import cz.upce.webapp.selenium.testframework.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

@PageObject
public class LoginPage
{
    public static final String INVALID_LOGIN_ERROR_MESSAGE = "Invalid login credentials!";
    String url = "http://localhost:9000";

    @Autowired
    public WebDriver driver;

    @Autowired
    private DashboardPage dashboardPage;

    @FindBy(id = "inputPassword")
    WebElement inputPassword;

    public String getLoginFormName()
    {
        return driver.findElement(By.className("card-header")).getText();
    }

    public String getSubmitName()
    {
        return driver.findElement(By.className("btn-primary")).getText();
    }

    public String getFormSubmitLink()
    {
        return driver.findElement(By.id("loginForm")).getAttribute("action");
    }

    public DashboardPage submitLoginForm(String email, String password)
    {
        fillEmail(email);
        fillPassword(password);
        driver.findElement(By.id("login-submit")).click();

        return dashboardPage;


    }

    private LoginPage fillPassword(String password) {
        inputPassword.sendKeys(password);
        return this;
    }

    private LoginPage fillEmail(String email) {
        driver.findElement(By.id("inputEmail"))
                .sendKeys(email);
        return this;
    }

    public String getErrorMessage() {
        return driver.findElement(By.id("error-message")).getText();
    }

    public String getExpectedTitle()
    {
        return "Nuts E-Shop Login";
    }

    public String getExpectedFormName()
    {
        return "Login";
    }

    public String getExpectedSubmitName()
    {
        return "Login";
    }

    public String getFormAction()
    {
        return "http://localhost:9000" +
                "/loginafter";
    }

    public LoginPage visit() {
        driver.get(url);
        return this;
    }
}