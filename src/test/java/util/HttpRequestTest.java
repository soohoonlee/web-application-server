package util;

import static org.junit.Assert.assertEquals;

import http.HttpRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class HttpRequestTest {

  private final String testDirectory = "./src/test/resources/";

  @Test
  public void request_GET() throws IOException {
    final InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
    final HttpRequest request = new HttpRequest(in);

    assertEquals("GET", request.getMethod());
    assertEquals("/user/create", request.getPath());
    assertEquals("keep-alive", request.getHeader("Connection"));
    assertEquals("jinimania", request.getParameter("userId"));
  }

  @Test
  public void request_POST() throws IOException {
    final InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
    final HttpRequest request = new HttpRequest(in);

    assertEquals("POST", request.getMethod());
    assertEquals("/user/create", request.getPath());
    assertEquals("keep-alive", request.getHeader("Connection"));
    assertEquals("jinimania", request.getParameter("userId"));
  }
}
