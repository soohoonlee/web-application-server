package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

  private Map<String, Object> values = new HashMap<>();

  private String id;

  public HttpSession(final String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setAttribute(final String name, final Object value) {
    values.put(name, value);
  }

  public Object getAttribute(final String name) {
    return values.get(name);
  }

  public void removeAttribute(final String name) {
    values.remove(name);
  }

  public void invalidate() {
    HttpSessions.remove(id);
  }
}
