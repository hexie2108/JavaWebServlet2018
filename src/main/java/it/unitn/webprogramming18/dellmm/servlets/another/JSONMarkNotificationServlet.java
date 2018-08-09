package it.unitn.webprogramming18.dellmm.servlets.another;

import it.unitn.webprogramming18.dellmm.db.daos.NotificationDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Notification;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "JSONMarkNotificationServlet")
public class JSONMarkNotificationServlet extends HttpServlet {
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
            throw new ServletException("Impossible to get db factory for notification storage system", ex);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");


        try {
            Notification notification = notificationDAO.getByPrimaryKey(Integer.parseInt(id));

            if(notification == null) {
                ServletUtility.sendError(request, response, 400, "markNotification.errors.notFound");
                return;
            }

            notification.setStatus(true);

            notificationDAO.update(notification);
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "markNotification.errors.dbError");
            return;
        }

        ServletUtility.sendJSON(request, response, 200, new HashMap<>());
    }
}
