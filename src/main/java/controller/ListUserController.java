package controller;

import http.HttpRequest;
import http.HttpResponse;

import http.HttpSession;
import java.util.Collection;
import java.util.Map;

import model.User;
import util.HttpRequestUtils;
import db.DataBase;

public class ListUserController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if (!isLogined(request.getSession())) {
            response.sendRedirect("/user/login.html");
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
        response.forwardBody(sb.toString());
    }

    private static boolean isLogined(final HttpSession httpSession) {
        final Object user = httpSession.getAttribute("user");
        if (user == null) {
            return false;
        }
        return true;
    }
}
