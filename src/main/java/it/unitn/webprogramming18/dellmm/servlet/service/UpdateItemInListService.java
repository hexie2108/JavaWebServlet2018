package it.unitn.webprogramming18.dellmm.servlet.service;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.LogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCCategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCLogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import it.unitn.webprogramming18.dellmm.javaBeans.Log;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.IOException;
import java.rmi.ServerException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servizio per aggiunge, elimina, comprato un prodotto in una list per utente
 * registrato
 *
 * @author mikuc
 */
public class UpdateItemInListService extends HttpServlet
{

        private ProductInListDAO productInListDAO;
        private LogDAO logDAO;
        private PermissionDAO permissionDAO;

        @Override
        public void init() throws ServletException
        {
                productInListDAO = new JDBCProductInListDAO();
                logDAO = new JDBCLogDAO();
                permissionDAO = new JDBCPermissionDAO();
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                doGet(request, response);

        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                //get azione che vuoi fare
                String action = null;
                action = request.getParameter("action");
                //get id prodotto
                String productId = null;
                productId = request.getParameter("productId");
                //get id lista
                String listId = null;
                listId = request.getParameter("listId");

                if (action == null || productId == null || listId == null)
                {
                        throw new ServletException("manca il parametro action o  id del prodotto o id della lista da aggiungere");
                }
                //memorizza il risultato dell'operazione
                String result = "";

                //get user corrente
                User user = (User) request.getSession().getAttribute("user");
                if (user == null)
                {
                        throw new ServletException("non sei loggato");
                }
                //get permesso dell'utente su tale lista
                Permission permission;
                try
                {
                        permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));

                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }
                //se il permesso è  vuoto
                if (permission == null)
                {
                        throw new ServletException("non hai nessun permesso su tale lista");
                }

                //in caso di inserimento
                if (action.equals("insert"))
                {
                        if (permission.isAddObject())
                        {
                                try
                                {

                                        //se non esiste la ripetizione
                                        if (!productInListDAO.checkIsProductInListByIds(Integer.parseInt(productId), Integer.parseInt(listId)))
                                        {
                                                ProductInList productInList = new ProductInList();
                                                productInList.setProductId(Integer.parseInt(productId));
                                                productInList.setListId(Integer.parseInt(listId));
                                                productInList.setStatus(false);
                                                productInListDAO.insert(productInList);
                                                result = "InsertOk";
                                        }
                                        //se esiste la ripetizione, lancia un messaggio 
                                        else
                                        {
                                                result = "InsertFail";
                                        }
                                }
                                catch (DAOException ex)
                                {
                                        throw new ServletException(ex.getMessage(), ex);
                                }
                        }
                        else
                        {
                                throw new ServletException("non hai il permesso di aggiungere il prodotto in questa lista");
                        }

                }

                //in caso elimina un prodotto dalla lista
                else if (action.equals("delete"))
                {
                        if (permission.isDeleteObject())
                        {
                                try
                                {
                                        productInListDAO.deleteByProductIdAndListId(Integer.parseInt(productId), Integer.parseInt(listId));
                                }
                                catch (DAOException ex)
                                {
                                        throw new ServletException(ex.getMessage(), ex);
                                }
                                result = "DeleteOk";
                        }
                        else
                        {
                                throw new ServletException("non hai il permesso di eliminare il prodotto in questa lista");
                        }
                }

                //in caso comprato
                else if (action.equals("bought"))
                {
                        ProductInList productInList = null;
                        Log log = null;

                        try
                        {
                                //aggiornare lo stato del prodotto in lista
                                productInList = productInListDAO.getByProductIdAndListId(Integer.parseInt(productId), Integer.parseInt(listId));
                                productInList.setStatus(true);
                                productInListDAO.update(productInList);

                                //inserire o aggiornare il log di acquisto
                                log = logDAO.getUserProductLogByIds(user.getId(), Integer.parseInt(productId));

                                //se non esiste un log su tale prodotto e utente
                                if (log == null)
                                {
                                        log = new Log();
                                        log.setProductId(Integer.parseInt(productId));
                                        log.setUserId(user.getId());
                                        log.setLast1(new Timestamp(System.currentTimeMillis()));
                                        logDAO.insert(log);
                                }
                                //esiste già un log, bisogna aggiornare i vari tempi
                                else
                                {

                                        //caso del secondo aquisto
                                        if (log.getLast2() == null)
                                        {
                                                log.setLast2(log.getLast1());
                                        }
                                        //caso del terzo aquisto
                                        else if (log.getLast3() == null)
                                        {
                                                log.setLast3(log.getLast2());
                                                log.setLast2(log.getLast1());

                                        }
                                        //caso del quarto aquisto e in poi
                                        else
                                        {
                                                log.setLast4(log.getLast3());
                                                log.setLast3(log.getLast2());
                                                log.setLast2(log.getLast1());
                                        }

                                        log.setLast1(new Timestamp(System.currentTimeMillis()));
                                        logDAO.update(log);
                                }

                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }
                        result = "BoughtOk";

                }

                //ritorna alla pagina di provenienza
                String prevUrl = request.getHeader("Referer");
                if (prevUrl == null)
                {
                        prevUrl = getServletContext().getContextPath();
                }
                //passare lo risultato  di inserimento
                request.getSession().setAttribute("result", result);
                response.sendRedirect(response.encodeRedirectURL(prevUrl));
        }

}
