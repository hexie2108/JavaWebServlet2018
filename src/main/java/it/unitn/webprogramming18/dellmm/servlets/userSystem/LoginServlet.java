package it.unitn.webprogramming18.dellmm.servlets.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet
{

        public static final String PREV_URL_KEY = "prevUrl",
                    NEXT_URL_KEY = "nextUrl",
                    EMAIL_KEY = "email",
                    PWD_KEY = "password",
                    REMEMBER_KEY = "remember";

        public static final String ERR_NOUSER_PWD_KEY = "error_noUserOrPassword",
                    ERR_NO_VER_KEY = "error_noVerified";

        private static final String LOGIN_JSP = "/WEB-INF/jsp/userSystem/login.jsp";

        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {

                userDAO = new JDBCUserDAO();

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                if (request.getRequestURI().endsWith(".json"))
                {
                        ServletUtility.sendError(request, response, 400, "generic.errors.postOnly");
                }
                else
                {
                        request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
                }
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                String contextPath = getServletContext().getContextPath();
                if (!contextPath.endsWith("/"))
                {
                        contextPath += "/";
                }

                String email = request.getParameter(EMAIL_KEY);
                String password = request.getParameter(PWD_KEY);
                String nextUrl = request.getParameter(NEXT_URL_KEY);

                if (email == null)
                {
                        email = "";
                }

                if (password == null)
                {
                        password = "";
                }

                // Se nextUrl altrimenti non specificato usa pagina di default(index)
                if (nextUrl == null || nextUrl.isEmpty())
                {
                        nextUrl = contextPath;
                }

                User user = null;

                if (!email.isEmpty() && !password.isEmpty())
                {
                        try
                        {
                                user = userDAO.getByEmailAndPassword(email, password);
                        }
                        catch (DAOException e)
                        {
                                e.printStackTrace();
                                ServletUtility.sendError(request, response, 500, "login.errors.unsearchableUser");
                                return;
                        }
                }

                if (user == null)
                {
                        ServletUtility.sendError(request, response, 400, "login.errors.wrongUsernameOrPassword");
                        return;
                }

                if (user.getVerifyEmailLink() != null)
                {
                        ServletUtility.sendError(request, response, 400, "login.errors.noValidatedEmail");
                        return;
                }

                // Create session, set user
                HttpSession session = request.getSession();

                session.setAttribute("user", user);

                String remember = request.getParameter(REMEMBER_KEY);
                if (remember != null && remember.equals("on"))
                {
                        session.setMaxInactiveInterval(-1);
                }

                if (request.getRequestURI().endsWith(".json"))
                {
                        HashMap<String, String> res = new HashMap<>();
                        res.put("nextUrl", response.encodeRedirectURL(nextUrl));
                        ServletUtility.sendJSON(request, response, 200, res);

                        return;
                }
                else
                {
                        response.sendRedirect(response.encodeRedirectURL(nextUrl));
                        return;
                }
        }
}
