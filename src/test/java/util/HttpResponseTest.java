package util;

import http.HttpResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.Test;

public class HttpResponseTest {

  private final String testDirectory = "./src/test/resources/";

  @Test
  public void responseForward() throws IOException {
    final HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
    response.forward("/index.html");
  }

  @Test
  public void responseRedirect() throws IOException {
    final HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
    response.sendRedirect("/index.html");
  }

  @Test
  public void responseCookies() throws IOException {
    final HttpResponse response = new HttpResponse(createOutputStream("Http_Cookie.txt"));
    response.addHeader("Set-Cookie", "logined=true");
    response.sendRedirect("/index.html");
  }

  private OutputStream createOutputStream(final String fileName) throws FileNotFoundException {
    return new FileOutputStream(new File(testDirectory + fileName));
  }
}
