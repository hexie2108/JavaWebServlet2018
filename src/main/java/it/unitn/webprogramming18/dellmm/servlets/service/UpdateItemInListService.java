package it.unitn.webprogramming18.dellmm.servlets.service;

import it.unitn.webprogramming18.dellmm.db.daos.LogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCLogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Log;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servizio per aggiunge, elimina, segna come comprato un prodotto in una lista
 * per utente registrato
 *
 *
 * @author mikuc
 */
public class UpdateItemInListService extends HttpServlet
{

        //il percorso base per tutte le immagini
        private static final String IMAGE_BASE_PATH = "image";
        // la cartella per immagine di prodotto
        private static final String IMAGE_PRODUCT = "product";
        // la cartella per immagine di logo di prodotto
        private static final String IMAGE_LOGO_PRODUCT = "productLogo";
        
        private ProductDAO productDAO;
        private ProductInListDAO productInListDAO;
        private LogDAO logDAO;
        private PermissionDAO permissionDAO;
        
        @Override
        public void init() throws ServletException
        {
                productDAO = new JDBCProductDAO();
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
                //string che memorizza il risultato dell'operazione
                String result = null;
                //get azione che vuoi fare
                String action = request.getParameter("action");
                //get id prodotto
                String productId = request.getParameter("productId");
                //get id lista
                String listId = request.getParameter("listId");

                //se manca il parametro
                if (action == null || productId == null || listId == null)
                {
                        throw new ServletException("manca il parametro action o  id del prodotto o id della lista da aggiungere");
                }

                //get user corrente
                User user = (User) request.getSession().getAttribute("user");
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
                CheckErrorUtils.isNull(permission, "non hai nessun permesso su tale lista");

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
                                                //crea e inserisce la relazione tra il prodotto e la lista
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
                                throw new ServletException("non hai il permesso di inserire il prodotto a questa lista");
                        }
                        
                }

                //in caso elimina un prodotto dalla lista
                else if (action.equals("delete"))
                {
                        
                        Product product = null;
                        
                        if (permission.isDeleteObject())
                        {
                                try
                                {
                                        //elimina la relazione tra prodotto e la lista
                                        productInListDAO.deleteByProductIdAndListId(Integer.parseInt(productId), Integer.parseInt(listId));

                                        //get beans di prodotto
                                        product = productDAO.getByPrimaryKey(Integer.parseInt(productId));
                                        //se product è nullo
                                        CheckErrorUtils.isNull(product, "non eiste il prodotto con tale id");
                                        //se il prodotto è privato
                                        if (product.getPrivateListId() != 0)
                                        {
                                                //bisogna prima eliminare immagine
                                                String uploadPath = request.getServletContext().getRealPath("/") + IMAGE_BASE_PATH;
                                                //elimina file img e file logo del prodotto
                                                FileUtils.deleteFile(uploadPath + File.separator + IMAGE_PRODUCT + File.separator + product.getImg());
                                                FileUtils.deleteFile(uploadPath + File.separator + IMAGE_LOGO_PRODUCT + File.separator + product.getLogo());
                                                //poi elimina tale prodotto
                                                productDAO.deleteProductById(Integer.parseInt(productId));
                                        }
                                }
                                catch (DAOException ex)
                                {
                                        throw new ServletException(ex.getMessage(), ex);
                                }
                                result = "DeleteOk";
                        }
                        else
                        {
                                throw new ServletException("non hai il permesso di eliminare il prodotto da questa lista");
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

                                //get log di tale utente con tale prodotto
                                log = logDAO.getUserProductLogByIds(user.getId(), Integer.parseInt(productId));

                                //se non esiste un log vecchio
                                if (log == null)
                                {
                                        //inserisce un nuovo
                                        log = new Log();
                                        log.setProductId(Integer.parseInt(productId));
                                        log.setUserId(user.getId());
                                        log.setLast1(new Timestamp(System.currentTimeMillis()));
                                        logDAO.insert(log);
                                }
                                //se esiste già un log, bisogna aggiornare i vari tempi di acquisto
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
