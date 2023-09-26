package timetree;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.LocalDate;
import org.openqa.selenium.firefox.FirefoxDriver;
import timetree.manage.LogManager;
import timetree.manage.Logger;
import timetree.manage.SecretValueManager;
import timetree.webapp.TimeTreeWebAppHandler;

public class Main {
  private static final Logger logger = LogManager.getLogger();

  public static void main(String[] args) throws Exception {
    String username = SecretValueManager.GetUsername();
    String password = SecretValueManager.GetPassword();

    if (username == null || password == null || username.isBlank() || password.isBlank()) {
      throw new RuntimeException(
          "Timtree username and password must be set as environment variables! See readme.md");
    }

    WebDriverManager.firefoxdriver().setup();
    FirefoxDriver driver = new FirefoxDriver();

    TimeTreeWebAppHandler timeTreeWebAppHandler = new TimeTreeWebAppHandler(driver);

    timeTreeWebAppHandler.signIn(username, password);

    timeTreeWebAppHandler.selectCalendar();

    logger.info(timeTreeWebAppHandler.getDateDisplayed());

    timeTreeWebAppHandler.addNewEvent(LocalDate.of(2023, 8, 29), "Testing123");
    timeTreeWebAppHandler.addNewEvent(LocalDate.of(2024, 2, 1), "Testing144");
    timeTreeWebAppHandler.addNewEvent(LocalDate.of(2024, 2, 5), "Testing155");

    logger.info("Ending program.");
    // driver.close();
  }
}
