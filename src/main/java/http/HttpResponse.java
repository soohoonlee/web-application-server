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

  public void forward(final String url) throws IOException {
    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
    headers.put("Content-Type", "text/html;charset=utf-8");
    headers.put("Content-Length", body.length + "");

    dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");
    final Set<String> keys = headers.keySet();
    for (final String key : keys) {
      dataOutputStream.writeBytes(key + ": " + headers.get(key) + " \r\n");
    }
    dataOutputStream.writeBytes("\r\n");
  }

  public void sendRedirect(final String redirectUrl) throws IOException {
    dataOutputStream.writeBytes("HTTP/1.1 302 Redirect \r\n");
    final Set<String> keys = headers.keySet();
    for (final String key : keys) {
      dataOutputStream.writeBytes(key + ": " + headers.get(key) + " \r\n");
    }
    dataOutputStream.writeBytes("Location: " + redirectUrl + " \r\n");
    dataOutputStream.writeBytes("\r\n");
  }

  public void addHeader(final String key, final String value) {
    headers.put(key, value);
  }
}
