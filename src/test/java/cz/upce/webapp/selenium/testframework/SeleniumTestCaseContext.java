package cz.upce.webapp.selenium.testframework;

import cz.upce.webapp.dao.testutil.Creator;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = SeleniumTestCaseContext.class)
public class SeleniumTestCaseContext {

 @Bean
 public WebDriver webDriver() {
  return WebDriverHelper.getWebDriver();
 }

}