package it.unitn.webprogramming18.dellmm.servlets.front.service;

import it.unitn.webprogramming18.dellmm.db.daos.LogDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Log;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.FileUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servizio per aggiunge, elimina, segna come comprato un prodotto in una lista
 * per utente registrato
 *
 * @author mikuc
 */
public class UpdateItemInListService extends HttpServlet {

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
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            productInListDAO = daoFactory.getDAO(ProductInListDAO.class);
            logDAO = daoFactory.getDAO(LogDAO.class);
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ProductDAO or ProductInListDAO or LogDAO or PermissionDAO for user storage system", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle rb = i18n.getBundle(request);

        //string che memorizza il risultato dell'operazione
        String result = null;
        //get azione che vuoi fare
        String action = request.getParameter("action");
        //get id prodotto
        String productId = request.getParameter("productId");
        //get id lista
        String listId = request.getParameter("listId");

                //se azione è nullo
                if (action == null)
                {
                        ServletUtility.sendError(request, response, 400, "users.errors.missingAction"); //manca il parametro action
                        return;
                }
                CheckErrorUtils.isNull(listId, rb.getString("error.missingListId"));
                CheckErrorUtils.isNull(productId, rb.getString("error.missingProductId"));

        //get user corrente
        User user = (User) request.getSession().getAttribute("user");
        //get permesso dell'utente su tale lista
        Permission permission;
        try {
            permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        //se il permesso è  vuoto
        if (permission == null) {
            ServletUtility.sendError(request, response, 400, "servlet.errors.noPermissionOnList");
            return;
        }

        //in caso di inserimento
        if (action.equals("insert")) {
            if (permission.isAddObject()) {
                try {
                    //se non esiste la ripetizione
                    if (!productInListDAO.checkIsProductInListByIds(Integer.parseInt(productId), Integer.parseInt(listId))) {
                        //crea e inserisce la relazione tra il prodotto e la lista
                        ProductInList productInList = new ProductInList();
                        productInList.setProductId(Integer.parseInt(productId));
                        productInList.setListId(Integer.parseInt(listId));
                        productInList.setStatus(false);
                        productInListDAO.insert(productInList);
                        result = "InsertOk";
                    }
                    //se esiste la ripetizione, lancia un messaggio
                    else {
                        result = "InsertFail";
                    }
                } catch (DAOException ex) {
                    throw new ServletException(ex.getMessage(), ex);
                }
            } else {
                ServletUtility.sendError(request, response, 400, "permission.insertItemNotAllowed"); //Not allowed insert item in list
                return;
            }

        }

        //in caso elimina un prodotto dalla lista
        else if (action.equals("delete")) {

            Product product = null;

            if (permission.isDeleteObject()) {
                try {
                    //elimina la relazione tra prodotto e la lista
                    productInListDAO.deleteByProductIdAndListId(Integer.parseInt(productId), Integer.parseInt(listId));

                                        //get beans di prodotto
                                        product = productDAO.getByPrimaryKey(Integer.parseInt(productId));
                                        //se product è nullo
                                        CheckErrorUtils.isNull(product, rb.getString("error.ProductNotExist"));
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
                                ServletUtility.sendError(request, response, 400, "permission.deleteItemNotAllowed"); //Not allowed delete item in list
                                return;
                        }
                }

        //in caso comprato
        else if (action.equals("bought")) {

            ProductInList productInList = null;
            Log log = null;

            try {
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
                                        log.setEmailStatus(false);
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
                                        log.setEmailStatus(false);
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
