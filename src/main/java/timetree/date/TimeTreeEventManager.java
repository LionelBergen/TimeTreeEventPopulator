package timetree.date;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TimeTreeEventManager {
  private static final TimeTreeEvent ROSH_HASHANAH_1 =
      GetEvent(LocalDate.of(2023, 9, 27), LocalDate.of(2023, 9, 30), "Multi");

  private static List<TimeTreeEvent> events = Arrays.asList(ROSH_HASHANAH_1);

  public static List<TimeTreeEvent> GetAllEvents() {
    return events;
  }

  public static TimeTreeEvent GetEvent(LocalDate eventDate, String titleOfEvent) {
    return new TimeTreeEvent(eventDate, titleOfEvent);
  }

  public static TimeTreeEvent GetEvent(LocalDate start, LocalDate end, String titleOfEvent) {
    return new TimeTreeEvent(start, end, titleOfEvent);
  }
}
