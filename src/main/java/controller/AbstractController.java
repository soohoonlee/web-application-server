package controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

public abstract class AbstractController implements Controller {

  @Override
  public void service(final HttpRequest httpRequest, final HttpResponse httpResponse) {
    final HttpMethod method = httpRequest.getMethod();

    if (method.isPost()) {
      doPost(httpRequest, httpResponse);
    } else {
      doGet(httpRequest, httpResponse);
    }
  }

  protected void doPost(final HttpRequest request, final HttpResponse response) {
  }

  protected void doGet(final HttpRequest request, final HttpResponse response) {
  }

}
