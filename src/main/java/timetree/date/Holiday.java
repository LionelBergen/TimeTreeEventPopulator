package timetree.date;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Holiday {
  private static final Date ROSH_HASHANAH = new Date(2024, 10, 02);

  public static List<Date> GetAllDates() {
    return Arrays.asList(ROSH_HASHANAH);
  }
}
