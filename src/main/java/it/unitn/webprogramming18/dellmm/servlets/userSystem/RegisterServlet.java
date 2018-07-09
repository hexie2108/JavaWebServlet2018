package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.messageFactories.VerifyLinkMail;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;
    private EmailFactory emailFactory;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try{
            userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex){
            throw new ServletException("Impossible to get db factory for user storage system",ex);
        }

        emailFactory = (EmailFactory) super.getServletContext().getAttribute("emailFactory");
        if (emailFactory == null) {
            throw new ServletException("Impossible to get email factory for email system");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String first_name = request.getParameter("FirstName");
        String last_name = request.getParameter("LastName");
        String email = request.getParameter("Email");
        String password = request.getParameter("Password");
        String password2 = request.getParameter("Password2");
        String infPrivacy = request.getParameter("InfPrivacy");

        HashMap<String,String> messages = RegistrationValidator.createValidationMessages(
            userDAO,
            first_name,
            last_name,
            email,
            password,
            password2,
            infPrivacy);

        if (messages.isEmpty()) {
            try {
                User user = userDAO.generateUser(
                    first_name,
                    last_name,
                    email,
                    password);

                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                try {
                    emailFactory.sendMail(
                            "Registration",
                            "Registration",
                            VerifyLinkMail.createMessage(user),
                            "registrazioneprogettowebprog@gmail.com"
                    ); // Per ora le mandiamo a noi stessi per evitare casini
                }
                catch (MessagingException | UnsupportedEncodingException ex) {
                    ArrayList errorList = (ArrayList<String>)session.getAttribute("errors");
                    if(errorList == null){
                        session.setAttribute("errors",new ArrayList<String>());
                    }
                    errorList.add("Impossible to send the email. Please check the email in user's settings and click resend");
                }
            } catch (DAOException e) {
                if(e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                    messages.put("Email","Email già utilizzata");
                    request.setAttribute("messages",messages);

                    request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request,response);
                } else {
                    response.sendError(500, "Impossible register the user. The server returned: " + e.getMessage());
                }
            }
        } else {
            request.setAttribute("messages",messages);
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request,response);
        }
    }
}
