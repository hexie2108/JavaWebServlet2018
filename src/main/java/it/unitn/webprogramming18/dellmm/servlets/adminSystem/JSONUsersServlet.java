package it.unitn.webprogramming18.dellmm.servlets.adminSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "JSONUsersServlet")
public class JSONUsersServlet extends HttpServlet {
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

    @Override
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


        try {
            List<User> users = userDAO.filter(iId, email, name, surname, bAdmin);

            ServletUtility.sendJSON(request, response, 200, users);
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "users.errors.impossibleDbFilter");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        String id = request.getParameter("id");

        if (id == null) {
            ServletUtility.sendError(request, response, 400, "users.errors.idNotPresent");
            return;
        }

        int iId;
        try{
            iId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 400, "users.errors.idNotInt");
            return;
        }

        if (action == null) {
            ServletUtility.sendError(request, response, 400, "users.errors.missingAction");
        } else if (action.equalsIgnoreCase("modify")) {

        } else if (action.equalsIgnoreCase("delete")) {
            try {
                userDAO.delete(iId);
            } catch (DAOException e) {
                e.printStackTrace();
                ServletUtility.sendError(request, response, 400, "users.errors.impossibleDeleteUser");
                return;
            }

            ServletUtility.sendJSON(request, response, 200, new HashMap<>());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
