/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.service;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * servizio per creare e modificare la lista
 *
 * @author luca_morgese
 */
public class UpdateListService extends HttpServlet
{

        private ListDAO listDAO;
        private PermissionDAO permissionDAO;
        private ProductDAO productDAO;

        private User user = null;
        private Permission permission = null;
        private ShoppingList shoppingList = null;
        private String action = null;
        private String listId = null;
        private String result = null;
        private String prevUrl = null;
        private String uploadPath = null;

        //il percorso base per tutte le immagini
        private static final String IMAGE_BASE_PATH = "image";
        // la cartella per immagine di lista
        private static final String IMAGE_LIST = "list";
        // la cartella per immagine di prodotto
        private static final String IMAGE_PRODUCT = "product";
        // la cartella per immagine di logo di prodotto
        private static final String IMAGE_LOGO_PRODUCT = "productLogo";

        //le dimensioni dell'immagini
        private static final int IMAGE_WIDTH = 800;
        private static final int IMAGE_HEIGHT = 800;

        @Override
        public void init() throws ServletException
        {
                productDAO = new JDBCProductDAO();
                listDAO = new JDBCListDAO();
                permissionDAO = new JDBCPermissionDAO();
        }

        /**
         * get occupa elimina della lista
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException
        {

                //get l'azione che vuoi fare
                action = request.getParameter("action");
                //se azione è nullo
                CheckErrorUtils.isNull(action, "manca il parametro action");

                //in caso di delete
                if (action.equals("delete"))
                {

                        //get id lista
                        listId = request.getParameter("listId");
                        CheckErrorUtils.isNull(listId, "manca il parametro id lista");

                        //get user corrente
                        user = (User) request.getSession().getAttribute("user");

                        try
                        {
                                //get beans di lista da DB
                                shoppingList = listDAO.getByPrimaryKey(Integer.parseInt(listId));
                                //se lista è nullo
                                CheckErrorUtils.isNull(shoppingList, "non eiste la lista con tale id lista");

                                //get permesso dell'utente su tale lista
                                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));

                                //se il permesso è  nullo
                                CheckErrorUtils.isNull(permission, "non hai nessun permesso su tale lista");

                                //se utente non ha il permesso di eliminare la lista
                                CheckErrorUtils.isFalse(permission.isDeleteList(), "non hai il permesso di eliminare tale lista");

                                //prima deve eliminare img di lista
                                //set il percorso complete per salvare immagine
                                uploadPath = request.getServletContext().getRealPath("/") + IMAGE_BASE_PATH;

                                //prima deve eliminare tutti immagini e log dei prodotti privati presente in questa lista
                                List<Product> productList = productDAO.getPrivateProductByListId(Integer.parseInt(listId));
                                for (Product product : productList)
                                {
                                        //elimina file img del prodtto
                                        FileUtils.deleteFile(uploadPath + File.separator + IMAGE_PRODUCT + File.separator + product.getImg());
                                        //elimina file img del logo del prodtto
                                        FileUtils.deleteFile(uploadPath + File.separator + IMAGE_LOGO_PRODUCT + File.separator + product.getLogo());
                                }

                                //elimina file img della lista
                                FileUtils.deleteFile(uploadPath + File.separator + IMAGE_LIST + File.separator + shoppingList.getImg());

                                //elimina la lista
                                listDAO.deleteListByListId(Integer.parseInt(listId));

                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }

                        //set il risultato   dell'operazione
                        result = "ListDeleteOk";
                        //ritorna alla pagina di mylists
                        prevUrl = getServletContext().getContextPath() + "/mylists";

                        //passare lo risultato  dell'operazione
                        request.getSession().setAttribute("result", result);
                        response.sendRedirect(response.encodeRedirectURL(prevUrl));
                }

                //se valore di action è sconosciuto
                else
                {

                        throw new ServletException("valore di action non riconosciuto");

                }

        }

        /**
         * post occupa insert e update della lista
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                // usa un metodo statico per controllare se la richiesta è codificato in formato multipart/form-data
                CheckErrorUtils.isFalse(ServletFileUpload.isMultipartContent(request), "la richiesta non è stata codificata in formato multipart/form-data");

                List<FileItem> items = null;
                try
                {
                        //in caso di richiesta codificato in formato multipart, deve usare questo metodo per ottenre i parametri in formato di lista
                        items = FileUtils.initial().parseRequest(request);
                }
                catch (FileUploadException ex)
                {
                        throw new ServletException("l'errore durante analisi della richiesta");
                }

                String listName = null;
                String listCategory = null;
                String listDescription = null;
                String listImg = null;
                FileItem listImgFileItem = null;

                if (items != null && items.size() > 0)
                {
                        for (FileItem item : items)
                        {
                                //se oggetto è un campo di form
                                if (item.isFormField())
                                {
                                        switch (item.getFieldName())
                                        {
                                                //get il valore di vari parametri
                                                case "action":
                                                        action = item.getString();
                                                        break;
                                                case "listName":
                                                        listName = item.getString();
                                                        break;
                                                case "listCategory":
                                                        listCategory = item.getString();
                                                        break;
                                                case "listDescription":
                                                        listDescription = item.getString();
                                                        break;
                                                case "listId":
                                                        listId = item.getString();
                                                        break;
                                        }
                                }
                                //se item name uguale "listImg", allora è file img
                                else if (item.getFieldName().equals("listImg") && !item.getString().equals(""))
                                {
                                        listImgFileItem = item;
                                }
                        }
                }

                //se variabile sono nullo
                CheckErrorUtils.isNull(action, "manca il parametro action");
                CheckErrorUtils.isNull(listName, "manca il parametro listName");
                CheckErrorUtils.isNull(listCategory, "manca il parametro listCategory");
                CheckErrorUtils.isNull(listDescription, "manca il parametro listDescription");

                //get user corrente
                user = (User) request.getSession().getAttribute("user");

                //in caso di inserimento 
                if (action.equals("insert"))
                {
                        CheckErrorUtils.isNull(listImgFileItem, "manca il prametro file listImg");

                        //set il percorso complete per salvare immagine
                        uploadPath = request.getServletContext().getRealPath("/") + IMAGE_BASE_PATH + File.separator + IMAGE_LIST;
                        //salva l'immagine e get il nome salvato
                        listImg = FileUtils.upload(listImgFileItem, uploadPath, IMAGE_WIDTH, IMAGE_HEIGHT);
                        //crea beans di lista
                        shoppingList = new ShoppingList();
                        shoppingList.setName(listName);
                        shoppingList.setCategoryList(Integer.parseInt(listCategory));
                        shoppingList.setDescription(listDescription);
                        shoppingList.setImg(listImg);
                        shoppingList.setOwnerId(user.getId());

                        int newlistId;
                        try
                        {
                                //inserire la lista e  get id della nuova lista
                                newlistId = listDAO.insert(shoppingList);
                                //crea il beans di permesso su tale lista e user
                                permission = new Permission();
                                permission.setListId(newlistId);
                                permission.setUserId(user.getId());
                                permission.setModifyList(true);
                                permission.setDeleteList(true);
                                permission.setAddObject(true);
                                permission.setDeleteObject(true);

                                //inserire il permesso
                                permissionDAO.insert(permission);
                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }

                        result = "ListInsertOk";
                        //ritorna alla pagina di lista appena creata

                        prevUrl = getServletContext().getContextPath() + "/mylist?listId=" + newlistId;

                }

                //in caso di update
                else if (action.equals("update"))
                {
                        //se manca id lista da aggiornare
                        CheckErrorUtils.isNull(listId, "manca il parametro id lista");

                        //get permesso dell'utente su tale lista
                        try
                        {
                                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
                                //se il permesso è  nullo
                                CheckErrorUtils.isNull(permission, "non hai nessun permesso su tale lista");
                                //se utente non ha il permesso per modificare la lista
                                CheckErrorUtils.isFalse(permission.isModifyList(), "non hai il permesso di modificare tale lista");

                                //get beans di lista da DB
                                shoppingList = listDAO.getByPrimaryKey(Integer.parseInt(listId));
                                //se la lista è nulla
                                CheckErrorUtils.isNull(shoppingList, "non eiste la lista con tale id lista");

                                shoppingList.setName(listName);
                                shoppingList.setCategoryList(Integer.parseInt(listCategory));
                                shoppingList.setDescription(listDescription);

                                //se è stato selezionato un file durante questa richiesta 
                                if (listImgFileItem != null)
                                {

                                        //set il percorso complete per salvare immagine
                                        uploadPath = request.getServletContext().getRealPath("/") + IMAGE_BASE_PATH + File.separator + IMAGE_LIST;
                                        //elimina file img vecchio
                                        FileUtils.deleteFile(uploadPath + File.separator + shoppingList.getImg());
                                        //salva l'immagine e get il nome salvato
                                        listImg = FileUtils.upload(listImgFileItem, uploadPath, IMAGE_WIDTH, IMAGE_HEIGHT);
                                        //set nuovo file
                                        shoppingList.setImg(listImg);
                                }

                                //aggiorna la lista
                                listDAO.update(shoppingList);

                        }
                        catch (DAOException ex)
                        {
                                throw new ServletException(ex.getMessage(), ex);
                        }

                        result = "ListUpdateOk";
                        //ritorna alla pagina di lista specificata
                        prevUrl = getServletContext().getContextPath() + "/mylist?listId=" + shoppingList.getId();
                }

                //se valore di action è sconosciuto
                else
                {
                        throw new ServletException("valore di action non riconosciuto");
                }

                //passare lo risultato  di operazione
                request.getSession().setAttribute("result", result);
                response.sendRedirect(response.encodeRedirectURL(prevUrl));
        }

}
