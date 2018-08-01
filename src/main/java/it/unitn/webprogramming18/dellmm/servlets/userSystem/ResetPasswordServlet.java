package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

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
        if (request.getRequestURI().endsWith(".json")) {
            response.sendError(400, "POST only");
        } else {
            request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pw_rst_id = request.getParameter(ID_KEY);
        String password = request.getParameter(PWD_KEY);

        if (pw_rst_id == null) {
            ServletUtility.sendError(request,response,400,"Paramentro id mancante"); // TODO: To i18n
            return;
        }

        RegistrationValidator.ErrorMessage error =  RegistrationValidator.validatePassword(password);

        if (error != null) {
            ServletUtility.sendError(request, response, 400, RegistrationValidator.I18N_ERROR_STRING_PREFIX + error.toString());
            return;
        }

        try {
            userDAO.changePassword(pw_rst_id, password);
        } catch (IllegalArgumentException ex) {
            ServletUtility.sendError(request, response, 400,"ID non valido"); // TODO: To i18n
            return;
        } catch (DAOException e) {
            ServletUtility.sendError(request, response, 500,"Impossibile ricercare l'id richiesto"); // TODO: To i18n
            return;
        }

        if (request.getRequestURI().endsWith(".json")) {
            ServletUtility.sendJSON(request, response, 200, new HashMap());
        } else {
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            response.sendRedirect(contextPath);
        }
    }
}
