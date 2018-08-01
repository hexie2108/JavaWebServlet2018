package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import com.google.gson.Gson;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ResourceBundle;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    public static final String PREV_URL_KEY = "prevUrl",
            NEXT_URL_KEY = "nextUrl",
            EMAIL_KEY = "email",
            PWD_KEY = "password",
            REMEMBER_KEY = "remember";

    public static final String ERR_NOUSER_PWD_KEY = "error_noUserOrPassword",
            ERR_NO_VER_KEY = "error_noVerified";

    private static final String LOGIN_JSP = "/WEB-INF/jsp/login.jsp";

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
        request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        String email = request.getParameter(EMAIL_KEY);
        String password = request.getParameter(PWD_KEY);
        String nextUrl = request.getParameter(NEXT_URL_KEY);

        String requestedWith = request.getHeader("x-requested-with");

        if (email == null) {
            email = "";
        }

        if (password == null) {
            password = "";
        }

        // Se nextUrl altrimenti non specificato usa pagina di default(index)
        if (nextUrl == null || nextUrl.isEmpty()) {
            nextUrl = contextPath;
        }

        User user = null;

        if (!email.isEmpty() && !password.isEmpty()) {
            try {
                user = userDAO.getByEmailAndPassword(email, password);
            } catch (DAOException e) {
                e.printStackTrace();
                throw new ServletException("Impossible to get the user requested");
            }
        }

        HashMap<String, String> res = new HashMap<>();
        int resCode;

        if (user == null) {
            ResourceBundle bundle = it.unitn.webprogramming18.dellmm.util.i18n.getBundle(request);

            if(requestedWith == null) {
                response.sendError(400,"login.errors.wrongUsernameOrPassword");
                return;
            }

            res.put("message", bundle.getString("login.errors.wrongUsernameOrPassword"));
            resCode = 400;
        } else if (user.getVerifyEmailLink() != null) {
            ResourceBundle bundle = it.unitn.webprogramming18.dellmm.util.i18n.getBundle(request);

            if(requestedWith == null) {
                response.sendError(400, "login.errors.noValidatedEmail");
                return;
            }

            res.put("message", bundle.getString("login.errors.noValidatedEmail"));
            resCode = 400;
        } else {
            // Create session, set user
            HttpSession session = request.getSession();

            session.setAttribute("user", user);

            String remember = request.getParameter(REMEMBER_KEY);
            if (remember != null && remember.equals("on"))
                session.setMaxInactiveInterval(-1);

            if(requestedWith == null){
                response.sendRedirect(response.encodeRedirectURL(nextUrl));
                return;
            }

            // Create json structure
            res.put("nextUrl", response.encodeRedirectURL(nextUrl));
            resCode = 200;
        }

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // Send json
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        out.print(gson.toJson(res));
        response.setStatus(resCode);
    }
}
