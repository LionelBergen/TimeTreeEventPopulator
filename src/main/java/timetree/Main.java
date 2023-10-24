package timetree;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Arrays;
import java.util.List;
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
    String users = SecretValueManager.GetUsers();

    if (username == null
        || password == null
        || users == null
        || users.isBlank()
        || username.isBlank()
        || password.isBlank()) {
      throw new RuntimeException(
          "Timtree username, password, userlist must be set as environment variables! See readme.md");
    }

    List<String> userList = Arrays.asList(users.split(","));

    WebDriverManager.firefoxdriver().setup();
    FirefoxDriver driver = new FirefoxDriver();

    TimeTreeWebAppHandler timeTreeWebAppHandler = new TimeTreeWebAppHandler(driver);

    timeTreeWebAppHandler.signIn(username, password);

    logger.info("Current calender month: " + timeTreeWebAppHandler.getDateDisplayed());

    for (TimeTreeEvent event : TimeTreeEventManager.GetAllEvents()) {
      timeTreeWebAppHandler.addNewEvent(event, userList);
    }

    logger.info("Ending program.");
    // driver.close();
  }
}
