package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@WebServlet(name = "ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    public static final String ID_KEY = "id";

    public static final String MSG_KEY = "message";

    private static final String RESET_PASSWORD_JSP = "/WEB-INF/jsp/userSystem/resetPassword.jsp";

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new JDBCUserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().endsWith(".json")) {
            ServletUtility.sendError(request, response, 400, "generic.errors.postOnly");
        } else {
            request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pw_rst_id = request.getParameter(ID_KEY);
        String password = request.getParameter(RegistrationValidator.FIRST_PWD_KEY);

        if (pw_rst_id == null) {
            ServletUtility.sendError(request, response, 400, "resetPassword.errors.missingId");
            return;
        }

        HashMap<String, Object> kv = new HashMap<>();
        kv.put(RegistrationValidator.FIRST_PWD_KEY, password);

        ResourceBundle bundle = it.unitn.webprogramming18.dellmm.util.i18n.getBundle(request);

        Map<String, String> messages
                = RegistrationValidator.partialValidate(userDAO, kv)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        (Map.Entry<String, RegistrationValidator.ErrorMessage> e) -> e.getKey(),
                        (Map.Entry<String, RegistrationValidator.ErrorMessage> e) -> RegistrationValidator.I18N_ERROR_STRING_PREFIX + e.getValue().toString()
                ));

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(request, response, 400, messages);
            return;
        }

        try {
            if (!userDAO.changePassword(pw_rst_id, password)) {
                ServletUtility.sendError(request, response, 400, "resetPassword.errors.notFoundId");
                return;
            }
        } catch (IllegalArgumentException ex) {
            ServletUtility.sendError(request, response, 400, "resetPassword.errors.invalidId");
            return;
        } catch (DAOException e) {
            ServletUtility.sendError(request, response, 500, "resetPassword.errors.cantChange");
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
