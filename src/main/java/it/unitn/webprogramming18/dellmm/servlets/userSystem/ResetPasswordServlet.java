package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

@WebServlet(name = "ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
    public static final String ID_KEY = "id",
            PWD_KEY = "password";

    public static final String MSG_KEY = "message";

    private static final String RESET_PASSWORD_JSP = "/WEB-INF/jsp/resetPassword.jsp";

    private UserDAO userDAO;

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
        request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pw_rst_id = request.getParameter(ID_KEY);
        String password = request.getParameter(PWD_KEY);

        if (pw_rst_id == null) {
            response.sendError(400,"Paramentro id mancante");
            return;
        }


        ResourceBundle bundle = it.unitn.webprogramming18.dellmm.util.i18n.getBundle(request);

        RegistrationValidator.ErrorMessage error =  RegistrationValidator.validatePassword(password);

        String message =
            error == null?
                null:
                bundle.getString(RegistrationValidator.I18N_ERROR_STRING_PREFIX+error.toString());

        if (message != null) {
            request.getRequestDispatcher(
                    response.encodeRedirectURL(
                            RESET_PASSWORD_JSP + "?" + MSG_KEY + "=" + message
                    )
            ).forward(request, response);
            return;
        }

        try {
            userDAO.changePassword(pw_rst_id, password);

            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            response.sendRedirect(contextPath);
        } catch (IllegalArgumentException ex) {
            response.sendError(400,"ID non valido");
            return;
        } catch (DAOException e) {
            response.sendError(500, "Impossibile ricercare l'id richiesto");
            return;
        }
    }
}
