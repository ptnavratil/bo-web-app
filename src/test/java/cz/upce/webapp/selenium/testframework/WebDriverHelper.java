package cz.upce.webapp.selenium.testframework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.InetAddress;

public class WebDriverHelper {


    public static WebDriver getWebDriver() {
        WebDriver driver;
        // driver = setupRemoteWebDriver(OperaDriver.class);
        driver = setupRemoteWebDriver(ChromeDriver.class);
        //driver = setupFirefox();

        return driver;

    }

    protected static WebDriver setupFirefox() {
        WebDriver driver;
        System.setProperty("webdriver.firefox.bin", "/usr/bin/firefox54");
        WebDriverManager instance = WebDriverManager.getInstance(OperaDriver.class);
        instance.setup();
        driver = new FirefoxDriver();
        return driver;
    }

    private static RemoteWebDriver setupRemoteWebDriver(Class driverClass) {
        RemoteWebDriver driver;
        WebDriverManager instance = WebDriverManager.getInstance(driverClass);
        instance.setup();
        try {
            if (driverClass.equals(ChromeDriver.class)) {
                ChromeOptions options = new ChromeOptions();
                if (true || !"jety-17".equals(InetAddress.getLocalHost().getHostName())) {
                    options.setHeadless(true);
                    options.addArguments("window-size=1200x600");
                    System.out.println("Running chrome in headless mode");
                }

                driver = new ChromeDriver(options);
            } else {
                driver = (RemoteWebDriver) driverClass.newInstance();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return driver;
    }
}
