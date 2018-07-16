package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.PagePathsConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    public static final String PREV_URL_KEY = "prevUrl",
            NEXT_URL_KEY = "nextUrl",
            EMAIL_KEY = "email",
            PWD_KEY = "password",
            REMEMBER_KEY = "remember";

    public static final String ERR_NOUSER_PWD_KEY = "error_noUserOrPassword",
            ERR_NO_VER_KEY = "error_noVerified";

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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.getRequestDispatcher(PagePathsConstants.LOGIN_JSP).forward(request, response);
            return;
        }

        String newPrevUrl = request.getParameter(PREV_URL_KEY);
        if (newPrevUrl == null) {
            newPrevUrl = getServletContext().getContextPath();
            if (!newPrevUrl.endsWith("/")) {
                newPrevUrl += "/";
            }
        }

        request.setAttribute(NEXT_URL_KEY, URLEncoder.encode(newPrevUrl, "UTF-8"));
        request.setAttribute(PREV_URL_KEY, newPrevUrl);

        request.getRequestDispatcher(PagePathsConstants.ALREADY_LOGGED_IN_JSP).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        HttpSession session = request.getSession();

        String email = request.getParameter(EMAIL_KEY);
        String password = request.getParameter(PWD_KEY);
        String prevUrl = request.getParameter(PREV_URL_KEY);
        String nextUrl = request.getParameter(NEXT_URL_KEY);

        if (email == null) {
            email = "";
        }

        if (password == null) {
            password = "";
        }

        // Se prevUrl altrimenti non specificato usa pagina di default(index)
        if ((prevUrl == null) || (prevUrl.isEmpty())) {
            prevUrl = contextPath;
        }

        // Se nextUrl altrimenti non specificato usa pagina di default(index)
        if ((nextUrl == null) || (nextUrl.isEmpty())) {
            nextUrl = contextPath;
        }

        User user = null;

        if (!email.isEmpty() &&
                !password.isEmpty()) {
            try {
                user = userDAO.getByEmailAndPassword(email, password);
            } catch (DAOException e) {
                e.printStackTrace();
                throw new ServletException("Impossible to get the user requested");
            }
        }

        if (user == null) {
            request.getRequestDispatcher(PagePathsConstants.LOGIN_JSP + "?" +
                    PREV_URL_KEY + "=" + URLEncoder.encode(prevUrl, "utf-8") +
                    "&" + NEXT_URL_KEY + "=" + URLEncoder.encode(nextUrl, "utf-8") +
                    "&" + EMAIL_KEY + "=" + URLEncoder.encode(email, "utf-8") +
                    "&" + PWD_KEY + "=" + URLEncoder.encode(password, "utf-8") +
                    "&" + ERR_NOUSER_PWD_KEY + "=true"
            ).forward(request, response);
            return;
        }

        if (user.getVerifyEmailLink() != null) {
            request.getRequestDispatcher(PagePathsConstants.LOGIN_JSP + "?" +
                    PREV_URL_KEY + "=" + URLEncoder.encode(prevUrl, "utf-8") +
                    "&" + NEXT_URL_KEY + "=" + URLEncoder.encode(nextUrl, "utf-8") +
                    "&" + EMAIL_KEY + "=" + URLEncoder.encode(email, "utf-8") +
                    "&" + PWD_KEY + "=" + URLEncoder.encode(password, "utf-8") +
                    "&" + ERR_NO_VER_KEY + "=true"
            ).forward(request, response);
            return;
        }

        session.setAttribute("user", user);

        String remember = request.getParameter(REMEMBER_KEY);
        if (remember != null && remember.equals("on"))
            session.setMaxInactiveInterval(-1);

        response.sendRedirect(response.encodeRedirectURL(nextUrl));
    }
}