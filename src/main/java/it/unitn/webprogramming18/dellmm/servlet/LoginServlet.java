package it.unitn.webprogramming18.dellmm.servlet;

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
import java.net.URLEncoder;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
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
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            String newPrevUrl = request.getParameter("prevUrl");
            if (newPrevUrl == null) {
                String contextPath = getServletContext().getContextPath();
                if (!contextPath.endsWith("/")) {
                    contextPath += "/";
                }

                newPrevUrl = contextPath + "index.jsp";
            }

            request.setAttribute("nextUrl", URLEncoder.encode(newPrevUrl, "UTF-8"));
            request.setAttribute("prevUrl", newPrevUrl);

            request.getRequestDispatcher("/WEB-INF/jsp/alreadyLoggedIn.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        // Se non altrimenti specificata usa come url verso cui fare il redirect quella di default del sito
        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String prevUrl = request.getParameter("prevUrl");
        String nextUrl = request.getParameter("nextUrl");

        if (email == null) {
            email = "";
        }

        if (password == null) {
            password = "";
        }

        if ((prevUrl == null) || (prevUrl.isEmpty())) {
            prevUrl = contextPath + "index.jsp";
        }

        if ((nextUrl == null) || (nextUrl.isEmpty())) {
            nextUrl = contextPath + "index.jsp";
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
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp?" +
                    "prevUrl" + "=" + URLEncoder.encode(prevUrl, "utf-8") +
                    "&" + "nextUrl" + "=" + URLEncoder.encode(nextUrl, "utf-8") +
                    "&" + "email" + "=" + URLEncoder.encode(email, "utf-8") +
                    "&" + "password" + "=" + URLEncoder.encode(password, "utf-8") +
                    "&" + "error_noUserOrPassword=true"
            ).forward(request, response);
        } else if (user.getVerifyEmailLink() != null) {
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp?" +
                    "prevUrl" + "=" + URLEncoder.encode(prevUrl, "utf-8") +
                    "&" + "nextUrl" + "=" + URLEncoder.encode(nextUrl, "utf-8") +
                    "&" + "email" + "=" + URLEncoder.encode(email, "utf-8") +
                    "&" + "password" + "=" + URLEncoder.encode(password, "utf-8") +
                    "&" + "error_noVerified=true"
            ).forward(request, response);
        } else {
            session.setAttribute("user", user);

            String remember = request.getParameter("remember");
            if (remember != null && remember.equals("on"))
                session.setMaxInactiveInterval(-1);

            response.sendRedirect(response.encodeRedirectURL(nextUrl));
        }
    }
}
