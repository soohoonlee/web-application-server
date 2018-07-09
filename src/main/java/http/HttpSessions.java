package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {

  private static Map<String, HttpSession> sessions = new HashMap<>();

  public static HttpSession getSession(final String id) {
    HttpSession httpSession = sessions.get(id);
    if (httpSession == null) {
      httpSession = new HttpSession(id);
      sessions.put(id, httpSession);
      return httpSession;
    }
    return httpSession;
  }

  public static void remove(final String id) {
    sessions.remove(id);
  }
}
