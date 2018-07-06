package http;

import java.util.Map;
import util.HttpRequestUtils;

public class HttpCookie {

  private Map<String, String> cookies;

  public HttpCookie(final String cookieValue) {
    this.cookies = HttpRequestUtils.parseCookies(cookieValue);
  }

  public String getCookie(final String name) {
    return cookies.get(name);
  }

}
