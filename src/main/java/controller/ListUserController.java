package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import java.util.Collection;
import java.util.Map;
import model.User;
import util.HttpRequestUtils;

public class ListUserController implements Controller {

  @Override
  public void service(final HttpRequest httpRequest, final HttpResponse httpResponse) {
    if (!isLogin(httpRequest.getHeader("Cookie"))) {
      httpResponse.sendRedirect("/user/login.html");
      return;
    }
    Collection<User> users = DataBase.findAll();
    StringBuilder sb = new StringBuilder();
    sb.append("<table border='1'>");
    for (User user : users) {
      sb.append("<tr>");
      sb.append("<td>" + user.getUserId() + "</td>");
      sb.append("<td>" + user.getName() + "</td>");
      sb.append("<td>" + user.getEmail() + "</td>");
      sb.append("</tr>");
    }
    sb.append("</table>");
    httpResponse.forwardBody(sb.toString());
  }

  private boolean isLogin(String line) {
    String[] headerTokens = line.split(":");
    Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());
    String value = cookies.get("logined");
    if (value == null) {
      return false;
    }
    return Boolean.parseBoolean(value);
  }
}
