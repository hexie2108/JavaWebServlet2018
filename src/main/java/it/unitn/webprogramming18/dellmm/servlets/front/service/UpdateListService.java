/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets.front.service;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.FileUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sun.security.tools.PathList;

/**
 * servizio per creare e modificare la lista
 *
 * @author luca_morgese
 */
public class UpdateListService extends HttpServlet {

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


    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            listDAO = daoFactory.getDAO(ListDAO.class);
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ProductDAO or ListDAO or PermissionDAO for user storage system", ex);
        }

    }

    /**
     * get occupa elimina della lista
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //Language bundle
        ResourceBundle rb = i18n.getBundle(request);
        
        //get l'azione che vuoi fare
        action = request.getParameter("action");
        //se azione è nullo
        if(action == null){
            ServletUtility.sendError(request, response, 400, "users.errors.missingAction"); //manca il parametro action
            return;
        }

        Path pathImg = ServletUtility.getFolder(getServletContext(),"listImgsFolder");
        Path pathLogo = ServletUtility.getFolder(getServletContext(),"productLogoImgsFolder");
        Path pathList = ServletUtility.getFolder(getServletContext(),"listImgsFolder");

        //in caso di delete
        if (action.equals("delete")) {

                        //get id lista
                        listId = request.getParameter("listId");
                        if (listId == null) {
                            ServletUtility.sendError(request, response, 400, "error.missingListId");
                            return;
                        }

            //get user corrente
            user = (User) request.getSession().getAttribute("user");

                        try
                        {
                                //get beans di lista da DB
                                shoppingList = listDAO.getByPrimaryKey(Integer.parseInt(listId));
                                //se lista è nullo
                                if (shoppingList == null) {
                                    ServletUtility.sendError(request, response, 400, "error.ListNotExist");
                                    return;
                                }

                //get permesso dell'utente su tale lista
                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));

                //se il permesso è  nullo
                if (permission == null) {
                    ServletUtility.sendError(request, response, 400, "servlet.errors.noPermissionOnList");
                    return;
                }

                //se utente non ha il permesso di eliminare la lista
                if(!permission.isDeleteList()) {
                    ServletUtility.sendError(request, response, 400, "permission.deleteListNotAllowed");
                    return;
                }

                //prima deve eliminare img di lista
                //set il percorso complete per salvare immagine
                uploadPath = request.getServletContext().getRealPath("/") + ConstantsUtils.IMAGE_BASE_PATH;

                //prima deve eliminare tutti immagini e log dei prodotti privati presente in questa lista
                List<Product> productList = productDAO.getPrivateProductByListId(Integer.parseInt(listId));
                for (Product product : productList) {
                    //elimina file img del prodtto
                    ServletUtility.deleteFile(pathImg, product.getImg(), getServletContext());
                    //elimina file img del logo del prodtto
                    ServletUtility.deleteFile(pathLogo, product.getLogo(), getServletContext());
                }

                //elimina file img della lista
                ServletUtility.deleteFile(pathList, shoppingList.getImg(), getServletContext());

                //elimina la lista
                listDAO.deleteListByListId(Integer.parseInt(listId));

            } catch (DAOException ex) {
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
                        throw new ServletException(rb.getString("errors.unrecognizedAction"));
                }

    }

        /**
         * post occupa insert e update della lista
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

            Path pathList = ServletUtility.getFolder(getServletContext(),"listImgsFolder");

                //Language bundle
                ResourceBundle rb = i18n.getBundle(request);

                // usa un metodo statico per controllare se la richiesta è codificato in formato multipart/form-data
                if (!ServletFileUpload.isMultipartContent(request)) {
                    ServletUtility.sendError(request, response, 400,  "error.notMultipart");
                    return;
                }

                List<FileItem> items = null;
                try
                {
                        //in caso di richiesta codificato in formato multipart, deve usare questo metodo per ottenre i parametri in formato di lista
                        items = FileUtils.initial().parseRequest(request);
                }
                catch (FileUploadException ex)
                {
                    ServletUtility.sendError(request, response, 500,  "error.parseRequest");
                    return;
                }

        String listName = null;
        String listCategory = null;
        String listDescription = null;
        String listImg = null;
        FileItem listImgFileItem = null;

        if (items != null && items.size() > 0) {
            for (FileItem item : items) {
                //se oggetto è un campo di form
                if (item.isFormField()) {
                    switch (item.getFieldName()) {
                        //get il valore di vari parametri
                        case "action":
                            action = item.getString("UTF-8");
                            break;
                        case "listName":
                            listName = item.getString("UTF-8");
                            break;
                        case "listCategory":
                            listCategory = item.getString("UTF-8");
                            break;
                        case "listDescription":
                            listDescription = item.getString("UTF-8");
                            break;
                        case "listId":
                            listId = item.getString("UTF-8");
                            break;
                    }
                }
                //se item name uguale "listImg", allora è file img
                else if (item.getFieldName().equals("listImg") && !item.getString("UTF-8").equals("")) {
                    listImgFileItem = item;
                }
            }
        }
        
        //se variabile sono nullo
        if(action == null){
            ServletUtility.sendError(request, response, 400, "users.errors.missingAction"); //manca il parametro action
            return;
	}
        if(listName == null){
            ServletUtility.sendError(request, response, 400, "servlet.errors.nameMissing"); //manca il parametro listName
            return;
	}
        if (!FormValidator.validateGeneralInput(listName)){
            ServletUtility.sendError(request, response, 400, "servlet.errors.textLengthExceeded"); //"il parametro listaName ha superato la lunghezza massima consentita";
            return;
        }
        if(listCategory == null){
            ServletUtility.sendError(request, response, 400, "servlet.errors.categoryMissing"); //manca il parametro listCategory
            return;
	}
        if(listDescription == null){
            ServletUtility.sendError(request, response, 400, "servlet.errors.descriptionMissing"); //manca il parametro listDescription
            return;
	}
        if (listImgFileItem != null){
            if (!FormValidator.isValidFileExtension(listImgFileItem.getContentType())) {
                ServletUtility.sendError(request, response, 400, "servlet.errors.invalidFileFormat"); // "il file caricato non è un tipo valido";
                return;
            }
        }

        //get user corrente
        {
            user = (User) request.getSession().getAttribute("user");
        }

        //in caso di inserimento
        if (action.equals("insert")) {
            if(listImgFileItem == null){
		ServletUtility.sendError(request, response, 400, "servlet.errors.imageMissing"); //manca il prametro file listImg
		return;
            }

            //set il percorso complete per salvare immagine
            uploadPath = request.getServletContext().getRealPath("/") + ConstantsUtils.IMAGE_BASE_PATH + File.separator + ConstantsUtils.IMAGE_OF_LIST;
            //salva l'immagine e get il nome salvato
            listImg = FileUtils.upload(listImgFileItem, uploadPath, ConstantsUtils.IMAGE_OF_LIST_WIDTH, ConstantsUtils.IMAGE_OF_LIST_HEIGHT);
            //crea beans di lista
            shoppingList = new ShoppingList();
            shoppingList.setName(listName);
            shoppingList.setCategoryList(Integer.parseInt(listCategory));
            shoppingList.setDescription(listDescription);
            shoppingList.setImg(listImg);
            shoppingList.setOwnerId(user.getId());

            int newlistId;
            try {
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
            } catch (DAOException ex) {
                throw new ServletException(ex.getMessage(), ex);
            }

            result = "ListInsertOk";
            //ritorna alla pagina di lista appena creata

            prevUrl = getServletContext().getContextPath() + "/mylist?listId=" + newlistId;

                        //eliminare cookie di  check di negozio in vicinanza per riattivare check
                        Cookie NearShopChecked = new Cookie("NearShopChecked", "");
                        NearShopChecked.setPath(getServletContext().getContextPath());
                        //set la vita di cookie per 0 secondi
                        NearShopChecked.setMaxAge(0);
                        response.addCookie(NearShopChecked);

                }

                //in caso di update
                else if (action.equals("update"))
                {
                    Path path = ServletUtility.getFolder(getServletContext(),"listImgsFolder");

                        //se manca id lista da aggiornare
                        if (listId == null) {
                            ServletUtility.sendError(request, response, 400, "error.missingListId");
                            return;
                        }

            //get permesso dell'utente su tale lista
            try {
                permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
                //se il permesso è  nullo
                if(permission == null){
                    ServletUtility.sendError(request, response, 400, "servlet.errors.noPermissionOnList");
                    return;
                }
                //se utente non ha il permesso per modificare la lista
                if(!permission.isModifyList()) {
                    ServletUtility.sendError(request, response, 400, "permission.modifyListNotAllowed");
                    return;
                }

                                //get beans di lista da DB
                                shoppingList = listDAO.getByPrimaryKey(Integer.parseInt(listId));
                                //se la lista è nulla
                                if (shoppingList == null) {
                                    ServletUtility.sendError(request, response, 400, "error.ListNotExist");
                                    return;
                                }

                shoppingList.setName(listName);
                shoppingList.setCategoryList(Integer.parseInt(listCategory));
                shoppingList.setDescription(listDescription);

                //se è stato selezionato un file durante questa richiesta
                if (listImgFileItem != null) {

                    //set il percorso complete per salvare immagine
                    uploadPath = request.getServletContext().getRealPath("/") + ConstantsUtils.IMAGE_BASE_PATH + File.separator + ConstantsUtils.IMAGE_OF_LIST;
                    //elimina file img vecchio
                    ServletUtility.deleteFile(pathList, shoppingList.getImg(), getServletContext());
                    //salva l'immagine e get il nome salvato
                    listImg = FileUtils.upload(listImgFileItem, uploadPath, ConstantsUtils.IMAGE_OF_LIST_WIDTH, ConstantsUtils.IMAGE_OF_LIST_HEIGHT);
                    //set nuovo file
                    shoppingList.setImg(listImg);
                }

                //aggiorna la lista
                listDAO.update(shoppingList);

            } catch (DAOException ex) {
                throw new ServletException(ex.getMessage(), ex);
            }

            result = "ListUpdateOk";
            //ritorna alla pagina di lista specificata
            prevUrl = getServletContext().getContextPath() + "/mylist?listId=" + shoppingList.getId();
        }

                //se valore di action è sconosciuto
                else
                {
                        throw new ServletException(rb.getString("errors.unrecognizedAction"));
                }

        //passare lo risultato  di operazione
        request.getSession().setAttribute("result", result);
        response.sendRedirect(response.encodeRedirectURL(prevUrl));
    }

}
