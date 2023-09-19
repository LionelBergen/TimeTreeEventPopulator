package timetree.manage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Wait {
  public static void WaitForElementVisible(WebDriver driver, String xPath)
      throws InterruptedException {
    int attempts = 0;
    WebElement elementFound = null;

    while (attempts < 20 && elementFound == null) {
      try {
        elementFound = driver.findElement(By.xpath(xPath));
      } catch (NoSuchElementException e) {

      }
      Thread.sleep(1000);
      attempts++;
    }

    if (elementFound == null) {
      throw new RuntimeException("Cannot find element by xpath: " + xPath);
    } else {
      System.out.println("FOUND element by xpath: " + xPath);
    }
  }
}
