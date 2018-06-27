package http;

import static http.HttpMethod.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import org.junit.Test;

public class RequestLineTest {

  @Test(expected = IllegalArgumentException.class)
  public void request_line_wrong_parameter() {
    RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1 111");
    assertNotNull(requestLine);
    requestLine = new RequestLine("GET /index.html");
    assertNotNull(requestLine);
  }

  @Test
  public void create_get_method() {
    final RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1");
    assertEquals(GET, requestLine.getMethod());
    assertEquals("/index.html", requestLine.getPath());
  }

  @Test
  public void create_post_method() {
    final RequestLine requestLine = new RequestLine("POST /index.html HTTP/1.1");
    assertEquals(POST, requestLine.getMethod());
    assertEquals("/index.html", requestLine.getPath());
  }

  @Test
  public void create_path_and_params() {
    final RequestLine requestLine = new RequestLine(
        "GET /user/create?userId=jinimania&password=321321 HTTP/1.1");
    assertEquals(GET, requestLine.getMethod());
    assertEquals("/user/create", requestLine.getPath());
    final Map<String, String> params = requestLine.getParams();
    assertEquals(2, params.size());
  }
}
