package timetree;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import timetree.webapp.TimeTreeWebAppHandler;

public class Main {

  public static void main(String[] args) {
    WebDriverManager.firefoxdriver().setup();
    WebDriver driver = new FirefoxDriver();

    TimeTreeWebAppHandler.SignIn(driver);

    System.out.println("Ending program.");
    // driver.close();
  }
}
