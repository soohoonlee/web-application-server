package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class LoginController implements Controller {

  @Override
  public void service(final HttpRequest httpRequest, final HttpResponse httpResponse) {
    User user = DataBase.findUserById(httpRequest.getParameter("userId"));
    if (user != null) {
      if (user.login(httpRequest.getParameter("password"))) {
        httpResponse.addHeader("Set-Cookie", "logined=true");
        httpResponse.sendRedirect("/index.html");
      } else {
        httpResponse.sendRedirect("/user/login_failed.html");
      }
    } else {
      httpResponse.sendRedirect("/user/login_failed.html");
    }
  }
}
