package it.unitn.webprogramming18.dellmm.servlet.userSystem;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCUserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

        private static final String LOGIN_JSP = "/WEB-INF/jsp/login.jsp";

        private UserDAO userDAO;
        private ProductDAO productDAO;
        private ListDAO listDAO;
        private PermissionDAO permissionDAO;

        @Override
        public void init() throws ServletException
        {
                userDAO = new JDBCUserDAO();
                listDAO = new JDBCListDAO();
                permissionDAO = new JDBCPermissionDAO();
                productDAO = new JDBCProductDAO();
        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                String contextPath = getServletContext().getContextPath();
                if (!contextPath.endsWith("/"))
                {
                        contextPath += "/";
                }

                HttpSession session = request.getSession();

                String email = request.getParameter(EMAIL_KEY);
                String password = request.getParameter(PWD_KEY);
                String prevUrl = request.getParameter(PREV_URL_KEY);
                String nextUrl = request.getParameter(NEXT_URL_KEY);

                if (email == null)
                {
                        email = "";
                }

                if (password == null)
                {
                        password = "";
                }

                // Se prevUrl altrimenti non specificato usa pagina di default(index)
                if (prevUrl == null || prevUrl.isEmpty())
                {
                        prevUrl = contextPath;
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
                                throw new ServletException("Impossible to get the user requested");
                        }
                }

                if (user == null)
                {
                        request.getRequestDispatcher(LOGIN_JSP + "?"
                                    + PREV_URL_KEY + "=" + URLEncoder.encode(prevUrl, "utf-8")
                                    + "&" + NEXT_URL_KEY + "=" + URLEncoder.encode(nextUrl, "utf-8")
                                    + "&" + EMAIL_KEY + "=" + URLEncoder.encode(email, "utf-8")
                                    + "&" + ERR_NOUSER_PWD_KEY + "=true"
                        ).forward(request, response);
                        return;
                }

                if (user.getVerifyEmailLink() != null)
                {
                        request.getRequestDispatcher(LOGIN_JSP + "?"
                                    + PREV_URL_KEY + "=" + URLEncoder.encode(prevUrl, "utf-8")
                                    + "&" + NEXT_URL_KEY + "=" + URLEncoder.encode(nextUrl, "utf-8")
                                    + "&" + EMAIL_KEY + "=" + URLEncoder.encode(email, "utf-8")
                                    + "&" + ERR_NO_VER_KEY + "=true"
                        ).forward(request, response);
                        return;
                }

                session.setAttribute("user", user);

                String remember = request.getParameter(REMEMBER_KEY);
                if (remember != null && remember.equals("on"))
                {
                        session.setMaxInactiveInterval(-1);
                }

                /*--------------------------------------------------------------------------------------------------------------------*/
                //non ha ottenuto ancora selzionato una lista
                if (session.getAttribute("myListId") == null)
                {
                        List<ShoppingList> allMyList = new ArrayList();
                        List<Permission> allMyListPermission = new ArrayList();
                        List<Product> productsOfMyList = null;

                        try
                        {
                                //ottiene tutte liste dell'utente
                                allMyList = listDAO.getAllListByUserId(user.getId());
                                //se possiede qualche liste
                                if (allMyList != null || allMyList.size() > 0)
                                {
                                        
                                        Permission permission = null;
                                        for (int i = 0; i < allMyList.size(); i++)
                                        {
                                                //get relativi permessi di ogni lista
                                                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), allMyList.get(i).getId());
                                                allMyListPermission.add(permission);
                                        }
                                        
                                        productsOfMyList = productDAO.getProductsInListByListId(allMyList.get(0).getId());
                                        
                                        //se la lista corrente non è vuota, memorizza la lista di prodotto presente
                                        if(productsOfMyList!=null && productsOfMyList.size()>0){
                                                session.setAttribute("productsOfMyList", productsOfMyList);
                                        }
                                        //set la prima come la liste corrente
                                        session.setAttribute("myListId", allMyList.get(0).getId());
                                        //memoriazza tutte le liste che manipolabile
                                        session.setAttribute("allMyList", allMyList);
                                         //memorizza relativi permessi su tutte le liste
                                        session.setAttribute("allMyListPermission", allMyListPermission);
                                        
                                }
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }

                }
                /*---------------------------------------------------------------------------------------------------------------------*/

                response.sendRedirect(response.encodeRedirectURL(nextUrl));
        }
}
