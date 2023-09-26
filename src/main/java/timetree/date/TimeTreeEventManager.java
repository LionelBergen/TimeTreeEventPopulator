package timetree.date;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TimeTreeEventManager {
  private static final TimeTreeEvent ROSH_HASHANAH_1 =
      GetEvent(LocalDate.of(2023, 8, 11), "Testing11");
  private static final TimeTreeEvent ROSH_HASHANAH_2 =
      GetEvent(LocalDate.of(2023, 8, 10), "Testing10");
  private static final TimeTreeEvent ROSH_HASHANAH_3 =
      GetEvent(LocalDate.of(2023, 8, 3), "Testing3");
  private static final TimeTreeEvent ROSH_HASHANAH_4 =
      GetEvent(LocalDate.of(2023, 8, 1), "Testing1");

  private static List<TimeTreeEvent> events =
      Arrays.asList(ROSH_HASHANAH_1, ROSH_HASHANAH_2, ROSH_HASHANAH_3, ROSH_HASHANAH_4);

  public static List<TimeTreeEvent> GetAllEvents() {
    return events;
  }

  public static TimeTreeEvent GetEvent(LocalDate eventDate, String titleOfEvent) {
    return new TimeTreeEvent(eventDate, titleOfEvent);
  }
}
