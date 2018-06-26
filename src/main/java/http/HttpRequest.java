package http;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {

  private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

  private String method;
  private String path;
  private final Map<String, String> headers = new HashMap<>();
  private Map<String, String> params = new HashMap<>();

  public HttpRequest(final InputStream in) throws IOException {
    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, UTF_8));
    String line = bufferedReader.readLine();
    if (line == null) {
      return;
    }
    processRequestLine(line);

    line = bufferedReader.readLine();
    while (!line.equals("")) {
      log.debug("header : {}", line);
      final String[] tokens = line.split(":");
      headers.put(tokens[0].trim(), tokens[1].trim());
      line = bufferedReader.readLine();
    }
    if ("POST".equals(method)) {
      final String body = IOUtils
          .readData(bufferedReader, Integer.parseInt(headers.get("Content-Length")));
      params = HttpRequestUtils.parseQueryString(body);
      log.debug("body : {}", params);
    }
  }

  private void processRequestLine(final String requestLine) {
    log.debug("request line : {}", requestLine);
    final String[] tokens = requestLine.split(" ");
    method = tokens[0];

    if ("POST".equals(method)) {
      path = tokens[1];
      return;
    }

    final int index = tokens[1].indexOf('?');
    log.debug("index : {}", index);
    if (index == -1) {
      path = tokens[1];
    } else {
      path = tokens[1].substring(0, index);
      params = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
    }
    log.debug("path : {}", path);
  }

  public String getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public String getHeader(final String name) {
    return headers.get(name);
  }

  public String getParameter(final String name) {
    return params.get(name);
  }
}
