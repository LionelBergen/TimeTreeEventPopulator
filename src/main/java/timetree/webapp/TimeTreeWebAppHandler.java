package timetree.webapp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TimeTreeWebAppHandler {
  private static final String TIMETREE_SIGNIN_URL = "https://timetreeapp.com/signin";

  private static final String SIGN_IN_USERNAME_ELEMENT_CSS_XPATH =
      "input[data-test-id='signin-form-email']";
  private static final String SIGN_IN_PASSWORD_ELEMENT_CSS_XPATH =
      "input[data-test-id='signin-form-password']";

  public static void SignIn(WebDriver driver) {
    driver.get(TIMETREE_SIGNIN_URL);

    try {
      sendKeysByCssSelector(driver, SIGN_IN_USERNAME_ELEMENT_CSS_XPATH, "h");
      sendKeysByCssSelector(driver, SIGN_IN_PASSWORD_ELEMENT_CSS_XPATH, "h");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void sendKeysByCssSelector(WebDriver driver, String cssSelector, String keys)
      throws InterruptedException {
    driver.findElement(By.cssSelector(cssSelector)).sendKeys(keys);

    driver.wait(100);
  }
}
