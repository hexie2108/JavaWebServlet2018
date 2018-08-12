package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.email.messageFactories.VerifyLinkMail;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.i18n;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

@WebServlet(name = "ResendEmailServlet")
public class ResendEmailServlet extends HttpServlet {
    private EmailFactory emailFactory;

    @Override
    public void init() throws ServletException {
        emailFactory = (EmailFactory) super.getServletContext().getAttribute("emailFactory");
        if (emailFactory == null) {
            throw new ServletException("Impossible to get email factory for email system");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        //I18nEmail-----------------------------------------
        ResourceBundle languageBundle = i18n.getBundle(request);
        //--------------------------------------------------
        
        try {
            
            //I18nEmail-----------------------------------------
            String emailSubject = languageBundle.getString("emailText.label.registerEmailSubject");
            //--------------------------------------------------
            
            emailFactory.sendMail(
                    "Registration",
                    emailSubject,
                    VerifyLinkMail.createMessage(user, languageBundle),
                    "registrazioneprogettowebprog@gmail.com"
            ); // Per ora le mandiamo a noi stessi per evitare casini
        } catch (MessagingException e) {
            // TODO: Cambiare a notification ?
            ArrayList<String> errorList = (ArrayList<String>) session.getAttribute("errors");
            if (errorList == null) {
                errorList = new ArrayList<>();
            }
            
            //I18nEmail-----------------------------------------
            errorList.add(languageBundle.getString("forgotPassword.errors.cantSendEmail"));
            //--------------------------------------------------
            
            //errorList.add("Impossible to send the email. Please check the email in user's settings and click resend");
            session.setAttribute("errors", errorList);
        }
    }
}
