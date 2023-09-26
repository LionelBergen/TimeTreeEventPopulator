package timetree;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import timetree.date.TimeTreeEvent;
import timetree.date.TimeTreeEventManager;
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

    logger.info("Current calender month: " + timeTreeWebAppHandler.getDateDisplayed());

    for (TimeTreeEvent event : TimeTreeEventManager.GetAllEvents()) {
      timeTreeWebAppHandler.addNewEvent(event);
    }

    logger.info("Ending program.");
    // driver.close();
  }
}
