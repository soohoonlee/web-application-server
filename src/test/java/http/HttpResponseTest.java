package http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.Test;

public class HttpResponseTest {

  @Test
  public void responseCSS() throws IOException {
    final HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
    response.forward("/css/styles.css");
  }

  @Test
  public void responseJS() throws IOException {
    final HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
    response.forward("/js/scripts.js");
  }

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
    final String testDirectory = "./src/test/resources/";
    return new FileOutputStream(new File(testDirectory + fileName));
  }
}
