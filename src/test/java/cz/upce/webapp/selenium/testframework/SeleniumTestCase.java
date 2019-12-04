package cz.upce.webapp.selenium.testframework;

import org.junit.After;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SeleniumTestCaseContext.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public abstract class SeleniumTestCase {

    @Autowired
    WebDriver webDriver;

    @After
    public void afterTestMethod() throws Exception {
        // webDriver.quit();
    }
}