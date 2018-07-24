package it.unitn.webprogramming18.dellmm.servlets;

import com.google.gson.Gson;
import it.unitn.webprogramming18.dellmm.db.daos.NotificationDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Notification;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

@WebServlet(name = "JSONNotificationsServlet")
public class JSONNotificationsServlet extends HttpServlet {

    private NotificationDAO notificationDAO = null;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            notificationDAO = daoFactory.getDAO(NotificationDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get db factory for notifications storage system", ex);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        User user = (User) request.getSession().getAttribute("user");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Boolean filter = null;
        String strFilter = request.getParameter("status");

        if(strFilter != null) {
            if(strFilter.equalsIgnoreCase("true")){
                filter = true;
            } else if(strFilter.equalsIgnoreCase("false")){
                filter = false;
            }
        }

        try {
            List<HashMap<String, java.io.Serializable>> notificationJSON = new ArrayList<>();

            List<Notification> notificationList = notificationDAO.getNotificationsByUserId(user.getId(), filter);

            for (Notification notification: notificationList) {
                HashMap<String, java.io.Serializable> h = new HashMap<>();
                h.put("id", notification.getId());
                h.put("date", df.format(notification.getDate()));
                h.put("text", notification.getText());
                h.put("status", notification.isStatus());

                notificationJSON.add(h);
            }

            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            out.print(gson.toJson(notificationJSON));
        } catch (DAOException e) {
            e.printStackTrace();
            response.sendError(500, "Impossibile ottenere le notifiche");
            return;
        }

    }
}
