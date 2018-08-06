package it.unitn.webprogramming18.dellmm.servlets.adminSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UsersServlet")
public class UsersServlet extends HttpServlet {
    private static final String JSP_PAGE = "/WEB-INF/jsp/admin/users.jsp";

    private UserDAO userDAO = null;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get UserDAO for user storage system", ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String admin = request.getParameter("admin");

        Integer iId;
        try{
            iId = id == null || id.trim().isEmpty()? null: Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 400, "users.errors.idNotInt");
            return;
        }

        if(email != null && email.trim().isEmpty()) {
            email = null;
        }

        if(name != null && name.trim().isEmpty()) {
            name = null;
        }

        if(surname != null && surname.trim().isEmpty()) {
            surname = null;
        }

        Boolean bAdmin = admin == null || admin.trim().isEmpty()? null: Boolean.parseBoolean(admin);
        if (request.getRequestURI().endsWith(".json")) {
            List<User> users;

            try {
                users = userDAO.filter(iId, email, name, surname, bAdmin);
            } catch (DAOException e) {
                e.printStackTrace();
                ServletUtility.sendError(request, response, 500, "users.errors.impossibleDbFilter");
                return;
            }

            ServletUtility.sendJSON(request, response, 200, users);
        } else {
            request.getRequestDispatcher(JSP_PAGE).forward(request, response);
        }
    }
}
