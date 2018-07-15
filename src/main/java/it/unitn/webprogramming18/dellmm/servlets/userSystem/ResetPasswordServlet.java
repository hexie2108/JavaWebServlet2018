package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.PagePathsConstants;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
    public static final String ID_KEY = "id",
            PWD_KEY = "password";

    public static final String MSG_KEY = "message";

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
        request.getRequestDispatcher(PagePathsConstants.RESET_PASSWORD_JSP).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String pw_rst_id = request.getParameter(ID_KEY);
        String password = request.getParameter(PWD_KEY);

        if (pw_rst_id == null) {
            out.println("Paramentro id mancante");
        } else {
            String message = RegistrationValidator.validatePassword(password);
            if (message != null) {
                request.getRequestDispatcher(response.encodeRedirectURL(PagePathsConstants.RESET_PASSWORD_JSP + "?" + MSG_KEY + "=" + message)).forward(request, response);
            } else {
                try {
                    userDAO.changePassword(pw_rst_id, password);

                    String contextPath = getServletContext().getContextPath();
                    if (!contextPath.endsWith("/")) {
                        contextPath += "/";
                    }

                    response.sendRedirect(contextPath);
                } catch (IllegalArgumentException ex) {
                    out.println("ID non valido");
                } catch (DAOException e) {
                    out.println("ERRORE: Verifica se l'ID è corretto!");
                }
            }
        }
    }
}
