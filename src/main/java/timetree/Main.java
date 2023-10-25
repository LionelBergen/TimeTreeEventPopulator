package timetree;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.openqa.selenium.firefox.FirefoxDriver;
import timetree.date.TimeTreeEvent;
import timetree.manage.LogManager;
import timetree.manage.Logger;
import timetree.manage.SecretValueManager;
import timetree.webapp.TimeTreeWebAppHandler;

public class Main {
  private static final Logger logger = LogManager.getLogger();
  private static final String eventFilePath = "notes_jewish_days.txt";

  public static void main(String[] args) throws Exception {
    String username = SecretValueManager.GetUsername();
    String password = SecretValueManager.GetPassword();
    String users = SecretValueManager.GetUsers();

    if (isAnyEmptyOrNull(username, password, users)) {
      throw new RuntimeException(
          "Timtree username, password, userlist must be set as environment variables! See readme.md");
    }
    List<String> userList = Arrays.asList(users.split(","));

    List<String> fileContents = Files.lines(Paths.get(eventFilePath)).collect(Collectors.toList());
    List<TimeTreeEvent> events = new ArrayList<>();

    for (String line : fileContents) {
      String[] columns = line.split(",");

      String eventName = columns[0];
      LocalDate eventStart = getDateFromString(columns[1]);
      LocalDate eventEnd = getDateFromString(columns[2]);

      events.add(new TimeTreeEvent(eventStart, eventEnd, eventName));
    }

    // sort the events to make them 'load' faster
    Collections.sort(
        events,
        new Comparator<TimeTreeEvent>() {
          @Override
          public int compare(TimeTreeEvent o1, TimeTreeEvent o2) {
            return o1.getStart().compareTo(o2.getStart());
          }
        });

    WebDriverManager.firefoxdriver().setup();
    FirefoxDriver driver = new FirefoxDriver();

    TimeTreeWebAppHandler timeTreeWebAppHandler = new TimeTreeWebAppHandler(driver);

    timeTreeWebAppHandler.signIn(username, password);

    logger.info("Current calender month: " + timeTreeWebAppHandler.getDateDisplayed());

    for (TimeTreeEvent event : events) {
      timeTreeWebAppHandler.addNewEvent(event, userList);
    }

    logger.info("Ending program.");
    // driver.close();
  }

  private static LocalDate getDateFromString(String string) {
    String[] split = string.split("-");

    int year = Integer.parseInt(split[0]);
    int month = Integer.parseInt(split[1]);
    int day = Integer.parseInt(split[2]);

    return LocalDate.of(year, month, day);
  }

  private static boolean isAnyEmptyOrNull(String... strings) {
    for (String string : strings) {
      if (string == null || string.isBlank()) {
        return true;
      }
    }

    return false;
  }
}
