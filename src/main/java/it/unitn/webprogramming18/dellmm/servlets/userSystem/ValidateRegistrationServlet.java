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

@WebServlet(name = "ValidateRegistrationServlet")
public class ValidateRegistrationServlet extends HttpServlet {
    private UserDAO userDAO;

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
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

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

        PrintWriter out=response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(messages));
    }
}
