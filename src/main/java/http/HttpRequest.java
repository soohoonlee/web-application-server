package http;

import static http.HttpMethod.*;
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

  private final Map<String, String> headers = new HashMap<>();
  private Map<String, String> params;
  private RequestLine requestLine;

  public HttpRequest(final InputStream in) throws IOException {
    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, UTF_8));
    String line = bufferedReader.readLine();
    if (line == null) {
      throw new IllegalArgumentException("값이 없습니다.");
    }
    requestLine = new RequestLine(line);

    line = bufferedReader.readLine();
    while (!line.equals("")) {
      log.debug("header : {}", line);
      final String[] tokens = line.split(":");
      headers.put(tokens[0].trim(), tokens[1].trim());
      line = bufferedReader.readLine();
    }
    if (getMethod().isPost()) {
      final String body = IOUtils
          .readData(bufferedReader, Integer.parseInt(headers.get("Content-Length")));
      params = HttpRequestUtils.parseQueryString(body);
      log.debug("body : {}", params);
    } else {
      params = requestLine.getParams();
    }
  }

  public HttpMethod getMethod() {
    return requestLine.getMethod();
  }

  public String getPath() {
    return requestLine.getPath();
  }

  public String getHeader(final String name) {
    return headers.get(name);
  }

  public String getParameter(final String name) {
    return params.get(name);
  }
}
