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
import java.io.PrintWriter;

@WebServlet(name = "ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException
    {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if(daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex)
        {
            throw new ServletException("Impossible to get db factory for user storage system",ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/resetPassword.jsp").forward(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String pw_rst_id = request.getParameter("id");
        String password = request.getParameter("password");

        if(pw_rst_id == null) {
            out.println("Paramentro id mancante");
        } else {
            String message = RegistrationValidator.validatePassword(password);
            if(message != null) {
                request.setAttribute("message",message);
                request.getRequestDispatcher(response.encodeRedirectURL("/WEB-INF/jsp/resetPassword.jsp"+"?message="+message)).forward(request,response);
            } else {
                try
                {
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
