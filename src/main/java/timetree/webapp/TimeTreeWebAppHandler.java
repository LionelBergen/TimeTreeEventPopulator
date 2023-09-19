package timetree.webapp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import timetree.manage.Wait;

public class TimeTreeWebAppHandler {
  private static final String TIMETREE_SIGNIN_URL = "https://timetreeapp.com/signin";

  private static final String SIGN_IN_USERNAME_ELEMENT_CSS_SELECTOR =
      "input[data-test-id='signin-form-email']";
  private static final String SIGN_IN_PASSWORD_ELEMENT_CSS_SELECTOR =
      "input[data-test-id='signin-form-password']";
  private static final String SIGN_IN_SUBMIT_CSS_SELECTOR =
      "button[data-test-id='signin-form-submit']";

  private static final String CALENDAR_XPATH =
      "//p[contains(@class, 'e1rg0zpy2') and text() = 'Personal']";
  private static final String CALENDAR_XPATH_2 =
      "//div[contains(@class, 'e1rg0zpy1') and contains(@class, 'css-ok59kt')]";

  public static void SignIn(WebDriver driver, String username, String password) {
    driver.get(TIMETREE_SIGNIN_URL);

    sendKeysByCssSelector(driver, SIGN_IN_USERNAME_ELEMENT_CSS_SELECTOR, username);
    sendKeysByCssSelector(driver, SIGN_IN_PASSWORD_ELEMENT_CSS_SELECTOR, password);

    driver.findElement(By.cssSelector(SIGN_IN_SUBMIT_CSS_SELECTOR)).submit();
  }

  public static void SelectCalendar(RemoteWebDriver driver) throws InterruptedException {
    Wait.WaitForElementVisible(driver, CALENDAR_XPATH);
    WebElement element = driver.findElement(By.xpath(CALENDAR_XPATH));
    driver.executeScript("arguments[0].click();", element);

    element = driver.findElement(By.xpath(CALENDAR_XPATH_2));
    driver.executeScript("arguments[0].click();", element);
  }

  private static void sendKeysByCssSelector(WebDriver driver, String cssSelector, String keys) {
    driver.findElement(By.cssSelector(cssSelector)).sendKeys(keys);
  }
}
