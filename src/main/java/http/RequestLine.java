package http;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestLine {

  private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
  private final HttpMethod method;
  private final String path;
  private Map<String, String> params = new HashMap<>();

  public RequestLine(final String requestLine) {
    log.debug("request line : {}", requestLine);
    final String[] tokens = requestLine.split(" ");
    if (tokens.length != 3) {
      throw new IllegalArgumentException(requestLine + "이 형식에 맞지 않습니다.");
    }
    method = HttpMethod.valueOf(tokens[0]);
    log.debug("method : {}", method);

    if (method.isPost()) {
      path = tokens[1];
      return;
    }

    final int index = tokens[1].indexOf('?');
    if (index == -1) {
      path = tokens[1];
    } else {
      path = tokens[1].substring(0, index);
      params = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
    }
    log.debug("path : {}", path);
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public Map<String, String> getParams() {
    return params;
  }
}
