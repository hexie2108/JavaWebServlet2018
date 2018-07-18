package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import com.google.gson.Gson;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
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
import java.util.HashMap;

@WebServlet(name = "ValidateUserServlet")
public class ValidateUserServlet extends HttpServlet {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // Ottieni tutti i parametri
        String firstName = request.getParameter(RegistrationValidator.FIRST_NAME_KEY);
        String lastName = request.getParameter(RegistrationValidator.LAST_NAME_KEY);
        String email = request.getParameter(RegistrationValidator.EMAIL_KEY);
        String firstPassword = request.getParameter(RegistrationValidator.FIRST_PWD_KEY);
        String secondPassword = request.getParameter(RegistrationValidator.SECOND_PWD_KEY);
        String infPrivacy = request.getParameter(RegistrationValidator.INF_PRIVACY_KEY);

        HashMap<String, String> messages;

        // Usa il validator per verificare la conformità(in caso di strict controllo anche campi non inviati altrimenti no
        if(request.getParameter("strict") != null) {
            messages = RegistrationValidator.createValidationMessages(
                    userDAO,
                    firstName,
                    lastName,
                    email,
                    firstPassword,
                    secondPassword,
                    infPrivacy
            );
        } else {
            HashMap<String, String> kv = new HashMap<>();

            if (firstName != null && !firstName.isEmpty()) {
                kv.put(RegistrationValidator.FIRST_NAME_KEY, firstName);
            }

            if (lastName != null && !lastName.isEmpty()) {
                kv.put(RegistrationValidator.LAST_NAME_KEY, lastName);
            }

            if (email != null && !email.isEmpty()) {
                kv.put(RegistrationValidator.EMAIL_KEY, email);
            }

            if (firstPassword != null && !firstPassword.isEmpty()) {
                kv.put(RegistrationValidator.FIRST_PWD_KEY, firstPassword);
            }

            if (secondPassword != null && !secondPassword.isEmpty()) {
                kv.put(RegistrationValidator.SECOND_PWD_KEY, secondPassword);
            }

            messages = RegistrationValidator.partialValidate(userDAO, kv);
        }

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(messages));
    }
}