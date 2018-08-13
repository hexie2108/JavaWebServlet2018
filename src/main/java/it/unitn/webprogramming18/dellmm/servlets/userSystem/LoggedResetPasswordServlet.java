package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class LoggedResetPasswordServlet extends HttpServlet {
    private static final String LOGGED_RESET_PASSWORD = "/WEB-INF/jsp/userSystem/loggedResetPassword.jsp";

    private UserDAO userDAO = null;

    @Override
    public void init() throws ServletException {
       
            userDAO = new JDBCUserDAO();
      
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getRequestURI().endsWith(".json")) {
            ServletUtility.sendError(request, response, 400, "generic.errors.postOnly");
        } else {
            request.getRequestDispatcher(LOGGED_RESET_PASSWORD).forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstPassword = request.getParameter(FormValidator.FIRST_PWD_KEY);
        String secondPassword = request.getParameter(FormValidator.SECOND_PWD_KEY);

        HashMap<String, Object> kv = new HashMap<>();
        kv.put(FormValidator.FIRST_PWD_KEY, firstPassword);
        kv.put(FormValidator.SECOND_PWD_KEY, secondPassword);
/*
        Map<String, String> messages =
                FormValidator.partialValidate(null, kv)
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                        (Map.Entry<String, FormValidator.ErrorMessage> e) -> e.getKey(),
                        (Map.Entry<String, FormValidator.ErrorMessage> e) -> FormValidator.I18N_ERROR_STRING_PREFIX + e.getValue().toString()
                    )
                );

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(request, response, 400, messages);
            return;
        }*/

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        user.setPassword(firstPassword);

        try {
            userDAO.update(user);
        } catch (DAOException e) {
            ServletUtility.sendError(request, response, 500, "generic.errors.unupdatableUser");
            return;
        }

        if(request.getRequestURI().endsWith(".json")) {
            ServletUtility.sendJSON(request, response, 200, new HashMap<>());
        } else {
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }

            response.sendRedirect(contextPath);
        }
    }
}
