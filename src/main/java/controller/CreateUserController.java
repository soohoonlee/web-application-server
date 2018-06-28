package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUserController implements Controller {

  private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

  @Override
  public void service(final HttpRequest httpRequest, final HttpResponse httpResponse) {
    final User user = new User(httpRequest.getParameter("userId"),
        httpRequest.getParameter("password"), httpRequest.getParameter("name"),
        httpRequest.getParameter("email"));
    log.debug("user : {}", user);
    DataBase.addUser(user);
    httpResponse.sendRedirect("/index.html");
  }
}
