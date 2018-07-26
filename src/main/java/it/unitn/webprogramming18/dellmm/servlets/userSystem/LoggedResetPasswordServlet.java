package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "LoggedResetPasswordServlet")
public class LoggedResetPasswordServlet extends HttpServlet {
    private static final String LOGGED_RESET_PASSWORD = "/WEB-INF/jsp/loggedResetPassword.jsp";

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
            throw new ServletException("Impossible to get db factory for user storage system", ex);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOGGED_RESET_PASSWORD).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstPassword = request.getParameter(RegistrationValidator.FIRST_PWD_KEY);
        String secondPassword = request.getParameter(RegistrationValidator.SECOND_PWD_KEY);

        HashMap<String, Object> kv = new HashMap<>();
        kv.put(RegistrationValidator.FIRST_PWD_KEY, firstPassword);
        kv.put(RegistrationValidator.SECOND_PWD_KEY, secondPassword);

        HashMap<String, String> messages = RegistrationValidator.partialValidate(null, kv);

        if (!messages.isEmpty()) {
            request.setAttribute("messages", messages);
            request.getRequestDispatcher(LOGGED_RESET_PASSWORD).forward(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        user.setPassword(firstPassword);

        try {
            userDAO.update(user);
        } catch (DAOException e) {
            response.sendError(500, "Impossible fare l'update");
            return;
        }
    }
}
