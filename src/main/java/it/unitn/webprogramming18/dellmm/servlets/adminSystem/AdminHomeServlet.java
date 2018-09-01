package it.unitn.webprogramming18.dellmm.servlets.adminSystem;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * page per visuallizare il home di admin
 *
 * @author mikuc
 */
public class AdminHomeServlet extends HttpServlet
{

        private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/admin/home.jsp";

        private ProductDAO productDAO;
        private CategoryListDAO categoryListDAO;
        private CategoryProductDAO categoryProductDAO;
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
                        productDAO = daoFactory.getDAO(ProductDAO.class);
                        categoryListDAO = daoFactory.getDAO(CategoryListDAO.class);
                        categoryProductDAO = daoFactory.getDAO(CategoryProductDAO.class);
                        userDAO = daoFactory.getDAO(UserDAO.class);
                }
                catch (DAOFactoryException ex)
                {
                        throw new ServletException("Impossible to get PermissionDAO for user storage system", ex);
                }

        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                
                try
                {
                        //set i dati statistici
                        request.setAttribute("numberCategoryList", categoryListDAO.getCount());
                        request.setAttribute("numberCategoryProduct", categoryProductDAO.getCount());
                        request.setAttribute("numberProduct", productDAO.getCount());
                        request.setAttribute("numberUser", userDAO.getCount());
                        
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                
                //set il titolo della pagina
                request.setAttribute(ConstantsUtils.HEAD_TITLE, "admin home");
                request.getRequestDispatcher(JSP_PAGE_PATH).forward(request, response);
        }

}
