package http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

  private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

  private final DataOutputStream dataOutputStream;
  private Map<String, String> headers = new HashMap<>();

  public HttpResponse(final OutputStream outputStream) {
    dataOutputStream = new DataOutputStream(outputStream);
  }

  public void addHeader(final String key, final String value) {
    headers.put(key, value);
  }

  public void forward(final String url) {
    try {
      byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
      if (url.endsWith(".css")) {
        headers.put("Content-Type", "text/css");
      } else if (url.endsWith(".js")) {
        headers.put("Content-Type", "application/javascript");
      } else {
        headers.put("Content-Type", "text/html;charset=utf-8");
      }
      headers.put("Content-Length", body.length + "");
      response200Header();
      responseBody(body);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void forwardBody(final String body) {
      final byte[] contents = body.getBytes();
      headers.put("Content-Type", "text/html;charset=utf-8");
      headers.put("Content-Length", contents.length + "");
      response200Header();
      responseBody(contents);
  }

  public void sendRedirect(final String redirectUrl) {
    try {
      dataOutputStream.writeBytes("HTTP/1.1 302 Redirect \r\n");
      processHeaders();
      dataOutputStream.writeBytes("Location: " + redirectUrl + " \r\n");
      dataOutputStream.writeBytes("\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void processHeaders() {
    try {
      final Set<String> keys = headers.keySet();
      for (final String key: keys) {
        dataOutputStream.writeBytes(key + ": " + headers.get(key) + " \r\n");
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void response200Header() {
    try {
      dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");
      processHeaders();
      dataOutputStream.writeBytes("\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void responseBody(final byte[] body) {
    try {
      dataOutputStream.write(body, 0, body.length);
      dataOutputStream.writeBytes("\r\n");
      dataOutputStream.flush();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
