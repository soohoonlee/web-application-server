package webserver;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            final HttpRequest httpRequest = new HttpRequest(in);
            final HttpResponse httpResponse = new HttpResponse(out);
            final String path = getDefaultPath(httpRequest.getPath());

            if ("/user/create".equals(path)) {
                createUser(httpRequest, httpResponse);
            } else if ("/user/login".equals(path)) {
                login(httpRequest, httpResponse);
            } else if ("/user/list".equals(path)) {
                listUser(httpRequest, httpResponse);
            } else {
                httpResponse.forward(path);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void listUser(final HttpRequest httpRequest, final HttpResponse httpResponse)
        throws IOException {
        if (!isLogin(httpRequest.getHeader("Cookie"))) {
            httpResponse.sendRedirect("/user/login.html");
            return;
        }
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        httpResponse.forwardBody(sb.toString());
    }

    private void login(final HttpRequest httpRequest, final HttpResponse httpResponse)
        throws IOException {
        User user = DataBase.findUserById(httpRequest.getParameter("userId"));
        if (user != null) {
            if (user.login(httpRequest.getParameter("password"))) {
                httpResponse.addHeader("Set-Cookie", "logined=true");
                httpResponse.sendRedirect("/index.html");
            } else {
                httpResponse.sendRedirect("/user/login_failed.html");
            }
        } else {
            httpResponse.sendRedirect("/user/login_failed.html");
        }
    }

    private void createUser(final HttpRequest httpRequest, final HttpResponse httpResponse)
        throws IOException {
        final User user = new User(httpRequest.getParameter("userId"),
            httpRequest.getParameter("password"), httpRequest.getParameter("name"),
            httpRequest.getParameter("email"));
        log.debug("user : {}", user);
        DataBase.addUser(user);
        httpResponse.sendRedirect("/index.html");
    }

    private String getDefaultPath(String path) {
        if (path.equals("/")) {
            path = "/index.html";
        }
        return path;
    }

    private boolean isLogin(String line) {
        String[] headerTokens = line.split(":");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

}
