package it.unitn.webprogramming18.dellmm.servlet.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.PagePathsConstants;
import it.unitn.webprogramming18.dellmm.util.RegistrationValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

@WebServlet(name = "ModifyUserServlet")
public class ModifyUserServlet extends HttpServlet
{

        private static final String MODIFY_USER_JSP = "/WEB-INF/jsp/modifyUser.jsp";

        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {
                DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
                if (daoFactory == null)
                {
                        throw new ServletException("Impossible to get db factory for user storage system");
                }

                try
                {
                        userDAO = daoFactory.getDAO(UserDAO.class);
                }
                catch (DAOFactoryException ex)
                {
                        throw new ServletException("Impossible to get db factory for user storage system", ex);
                }
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
        {
                req.getRequestDispatcher(MODIFY_USER_JSP).forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                // Ottieni tutti i parametri
                String firstName = request.getParameter(RegistrationValidator.FIRST_NAME_KEY);
                String lastName = request.getParameter(RegistrationValidator.LAST_NAME_KEY);
                String email = request.getParameter(RegistrationValidator.EMAIL_KEY);

                HttpSession session = request.getSession(false);
                User user = (User) session.getAttribute("user");

                HashMap<String, String> kv = new HashMap<>();

                if (firstName != null && !firstName.isEmpty())
                {
                        kv.put(RegistrationValidator.FIRST_NAME_KEY, firstName);
                }
                else
                {
                        firstName = "";
                }

                if (lastName != null && !lastName.isEmpty())
                {
                        kv.put(RegistrationValidator.LAST_NAME_KEY, lastName);
                }
                else
                {
                        lastName = "";
                }

                if (email != null && !email.isEmpty())
                {
                        kv.put(RegistrationValidator.EMAIL_KEY, email);
                }
                else
                {
                        email = "";
                }

                // Usa il validator per verifiacare la conformità
                HashMap<String, String> messages = RegistrationValidator.partialValidate(userDAO, kv);

                if (!messages.isEmpty())
                {
                        request.setAttribute("messages", messages);
                        request.getRequestDispatcher(
                                    MODIFY_USER_JSP + "?"
                                    + RegistrationValidator.FIRST_NAME_KEY + "=" + URLEncoder.encode(firstName, "UTF-8")
                                    + "&" + RegistrationValidator.LAST_NAME_KEY + "=" + URLEncoder.encode(lastName, "UTF-8")
                                    + "&" + RegistrationValidator.EMAIL_KEY + "=" + URLEncoder.encode(email, "UTF-8")
                        ).forward(request, response);
                        return;
                }

                if (!firstName.isEmpty())
                {
                        user.setName(firstName);
                }

                if (!lastName.isEmpty())
                {
                        user.setSurname(lastName);
                }

                if (!email.isEmpty())
                {
                        user.setEmail(email);
                }

                try
                {
                        userDAO.update(user);

                        String contextPath = getServletContext().getContextPath();
                        if (!contextPath.endsWith("/"))
                        {
                                contextPath += "/";
                        }

                        response.sendRedirect(contextPath + PagePathsConstants.MODIFY_USER);
                }
                catch (DAOException e)
                {
                        response.sendError(500, "Impossibile aggiornare l'utente");
                        return;
                }
        }
}
