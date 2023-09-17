package timetree;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import timetree.manage.SecretValueManager;
import timetree.webapp.TimeTreeWebAppHandler;

public class Main {

  public static void main(String[] args) {
    String username = SecretValueManager.GetUsername();
    String password = SecretValueManager.GetPassword();

    if (username == null || password == null || username.isBlank() || password.isBlank()) {
      throw new RuntimeException(
          "Timtree username and password must be set as environment variables! See readme.md");
    }

    WebDriverManager.firefoxdriver().setup();
    WebDriver driver = new FirefoxDriver();

    TimeTreeWebAppHandler.SignIn(driver, username, password);

    System.out.println("Ending program.");
    // driver.close();
  }
}
