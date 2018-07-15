package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.messageFactories.ResetPasswordMail;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

@WebServlet(name = "ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    private UserDAO userDAO;
    private EmailFactory emailFactory;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if(daoFactory == null){
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/forgotPassword.jsp").forward(request,response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if(!contextPath.endsWith("/")) {
            contextPath+="/";
        }

        String email = request.getParameter("email");

        if (email == null ||
            email.isEmpty()) {
            response.sendRedirect(response.encodeRedirectURL(contextPath+"index.jsp"));
        }
        else
        {
            String prevUrl = request.getParameter("prevUrl");

            if ((prevUrl == null) || (prevUrl.isEmpty())) {
                prevUrl = "index.jsp";
            }

            User user;

            try {
                user = userDAO.getByEmail(email);
            } catch (DAOException e) {
                e.printStackTrace();
                throw new ServletException("Impossible trovare l'utente");
            }

            if (user == null) {
                request.getRequestDispatcher("/WEB-INF/jsp/forgotPassword.jsp?"+
                        "error_noEmail=true"+
                        "&"+"prevUrl"+"="+prevUrl+
                        "&"+"email"+"="+email
                ).forward(request,response);
            } else if(user.getVerifyEmailLink() != null) {
                request.getRequestDispatcher("/WEB-INF/jsp/forgotPassword.jsp?"+
                        "error_noVerified=true"+
                        "&"+"prevUrl"+"="+prevUrl+
                        "&"+"email"+"="+email
                ).forward(request,response);
            } else {
                boolean successo=false;
                for(int tentativi=0;(tentativi<5)&&(!successo);tentativi++) {
                    successo = true;
                    try
                    {
                        user.setResetPwdEmailLink(UUID.randomUUID().toString());
                        userDAO.update(user);

                        try {
                            emailFactory.sendMail(
                                    "Reset Password",
                                    "Reset Password",
                                    ResetPasswordMail.createMessage(user),
                                    "registrazioneprogettowebprog@gmail.com"
                            );
                            // Per permetterci nel mentre di non creare account mail false per verificare gli account
                            // TODO: Da cambiare
                        } catch(MessagingException | UnsupportedEncodingException ex) {
                            // TODO: Rimettere uuid_pw_rst a null?
                            response.sendError(500,"Impossible to send the email");
                            return;
                        }
                    } catch (DAOException  ex) {
                        if(ex.getCause() instanceof SQLIntegrityConstraintViolationException){
                            if (ex.getCause().getMessage().matches("Duplicate entry '.*?' for key 'UUID_[A-Z]*?_UNIQUE'")) {
                                successo = false;
                            }
                        }
                    }
                }

                // Avendo fallito per 5 volte a generare uuid unici mandiamo un errore
                // in quanto in condizioni normali è estremamente improbabile
                if(!successo) {
                    response.sendError(500,"Impossible to create reset password link");
                }
            }
        }
    }
}
