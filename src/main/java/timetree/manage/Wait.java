package timetree.manage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Wait {
  public static WebElement WaitForElementVisible(WebDriver driver, String xPath)
      throws InterruptedException {
    return WaitForElementVisible(driver, By.xpath(xPath));
  }

  public static WebElement WaitForElementVisible(
      WebDriver driver, String cssSelector, boolean byCssSelector) throws InterruptedException {
    return byCssSelector
        ? WaitForElementVisible(driver, By.cssSelector(cssSelector))
        : WaitForElementVisible(driver, By.xpath(cssSelector));
  }

  public static WebElement WaitForElementVisible(WebDriver driver, By by)
      throws InterruptedException {
    int attempts = 0;
    WebElement elementFound = null;

    while (attempts < 20 && elementFound == null) {
      try {
        elementFound = driver.findElement(by);
      } catch (NoSuchElementException e) {

      }
      Thread.sleep(1000);
      attempts++;
    }

    if (elementFound == null) {
      throw new RuntimeException("Cannot find element by: " + by);
    } else {
      System.out.println("FOUND element by: " + by);
    }

    return elementFound;
  }

  public static void WaitFor(long milliseconds) throws InterruptedException {
    Thread.sleep(milliseconds);
  }
}
