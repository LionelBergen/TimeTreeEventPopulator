package timetree.manage;

public class SecretValueManager {
  public static String GetUsername() {
    return System.getenv("TIMETREE_EMAIL_ID");
  }

  public static String GetPassword() {
    return System.getenv("TIMETREE_PASSWORD");
  }
}
