package timetree;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import timetree.manage.SecretValueManager;
import timetree.webapp.TimeTreeWebAppHandler;

public class Main {

  public static void main(String[] args) throws Exception {
    String username = SecretValueManager.GetUsername();
    String password = SecretValueManager.GetPassword();

    if (username == null || password == null || username.isBlank() || password.isBlank()) {
      throw new RuntimeException(
          "Timtree username and password must be set as environment variables! See readme.md");
    }

    WebDriverManager.firefoxdriver().setup();
    FirefoxDriver driver = new FirefoxDriver();

    TimeTreeWebAppHandler.SignIn(driver, username, password);

    // Thread.sleep(10000);
    TimeTreeWebAppHandler.SelectCalendar(driver);

    System.out.println("Ending program.");
    // driver.close();
  }
}
