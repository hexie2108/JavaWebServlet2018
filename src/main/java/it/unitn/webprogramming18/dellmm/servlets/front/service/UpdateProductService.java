package it.unitn.webprogramming18.dellmm.servlets.front.service;

import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.FileUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * servizio per inserire il prodotto privato
 *
 * @author luca_morgese
 */
public class UpdateProductService extends HttpServlet {

    private PermissionDAO permissionDAO;
    private ProductDAO productDAO;
    private ProductInListDAO productInListDAO;


    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            productInListDAO = daoFactory.getDAO(ProductInListDAO.class);
            permissionDAO = daoFactory.getDAO(PermissionDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ProductDAO or ProductInListDAO or PermissionDAO for user storage system", ex);
        }
    }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {

                //Language bundle
                ResourceBundle rb = i18n.getBundle(request);

                // usa un metodo statico per controllare se la richiesta è codificato in formato multipart/form-data
                if (!ServletFileUpload.isMultipartContent(request)){
                     ServletUtility.sendError(request, response, 400, "error.notMultipart");
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
                        throw new ServletException(rb.getString("error.parseRequest"));
                }

        String productName = null;
        String productCategory = null;
        String productDescription = null;
        String listId = null;
        String productImg = null;
        String productLogo = null;
        FileItem productImgFileItem = null;
        FileItem productLogoFileItem = null;

        if (items != null && items.size() > 0) {
            for (FileItem item : items) {
                //se oggetto è un campo di form
                if (item.isFormField()) {
                    switch (item.getFieldName()) {
                        //get il valore di vari parametri
                        case "productName":
                            productName = item.getString("UTF-8");
                            break;
                        case "productCategory":
                            productCategory = item.getString("UTF-8");
                            break;
                        case "productDescription":
                            productDescription = item.getString("UTF-8");
                            break;
                        case "listId":
                            listId = item.getString("UTF-8");
                            break;

                    }
                }
                //se item non è un campo normale di form e non è vuoto
                else if (!item.getString("UTF-8").equals("")) {

                    // se name uguale "productImg",
                    if (item.getFieldName().equals("productImg")) {
                        productImgFileItem = item;
                    }
                    //se item name uguale "prodocutLogo"
                    else if (item.getFieldName().equals("prodocutLogo")) {
                        productLogoFileItem = item;
                    }
                }
            }
        }

        //se variabili sono nulle
        if(productName == null){
            ServletUtility.sendError(request, response, 400, "servlet.errors.nameMissing"); //manca il parametro listName
            return;
	}
        if (!FormValidator.validateGeneralInput(productName)){
            ServletUtility.sendError(request, response, 400, "servlet.errors.textLengthExceeded"); //"il parametro productName ha superato la lunghezza massima consentita";
            return;
        }
        if(productCategory == null){
            ServletUtility.sendError(request, response, 400, "servlet.errors.categoryMissing"); //manca il parametro productCategory
            return;
	}
        if(productDescription == null){
            ServletUtility.sendError(request, response, 400, "servlet.errors.descriptionMissing"); //manca il parametro productDescription
            return;
        }

        //ListId is of logic competence, no need to i18n for user
        CheckErrorUtils.isNull(listId, "manca il parametro listId");

        if(productImgFileItem == null){
            ServletUtility.sendError(request, response, 400, "servlet.errors.imageMissing"); //manca il prametro file productImg
            return;
        }
        if(productLogoFileItem == null){
            ServletUtility.sendError(request, response, 400, "servlet.errors.logoMissing"); //manca il prametro file logoImg
            return;
        }
        if (!FormValidator.isValidFileExtension(productImgFileItem.getContentType())) {
            ServletUtility.sendError(request, response, 400, "servlet.errors.invalidFileFormat"); // "il file caricato non è un tipo valido";
            return;
        }
        if (!FormValidator.isValidFileExtension(productLogoFileItem.getContentType())) {
            ServletUtility.sendError(request, response, 400, "servlet.errors.invalidFileFormat"); // "il file caricato non è un tipo valido";
            return;
        }

        //get user corrente
        User user = (User) request.getSession().getAttribute("user");
        //get permesso dell'utente su tale lista
        try {
            Permission permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
            //se il permesso è  nullo
            if(permission == null){
                ServletUtility.sendError(request, response, 400, "servlet.errors.noPermissionOnList");
                return;
            }
            //se utente non ha il permesso per inserire il prodotto permission.insertItemNotAllowed
            if(!permission.isAddObject()) {
                ServletUtility.sendError(request, response, 400, "permission.insertItemNotAllowed");
                return;
            }
        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        //set il percorso complete per salvare immagine
        String uploadPath = request.getServletContext().getRealPath("/") + ConstantsUtils.IMAGE_BASE_PATH;
        //salva l'immagine di prodotto e get il nome salvato
        productImg = FileUtils.upload(productImgFileItem, uploadPath + File.separator + ConstantsUtils.IMAGE_OF_PRODUCT, ConstantsUtils.IMAGE_OF_PRODUCT_WIDTH, ConstantsUtils.IMAGE_OF_PRODUCT_HEIGHT);
        //salva l'immagine di logo e get il nome salvato
        productLogo = FileUtils.upload(productLogoFileItem, uploadPath + File.separator + ConstantsUtils.IMAGE_LOGO_OF_PRODUCT, ConstantsUtils.IMAGE_LOGO_OF_PRODUCT_WIDTH, ConstantsUtils.IMAGE_LOGO_OF_PRODUCT_HEIGHT);

        //crea beans di prodotto
        Product product = new Product();
        product.setName(productName);
        product.setDescription(productDescription);
        product.setImg(productImg);
        product.setLogo(productLogo);
        product.setCategoryProductId(Integer.parseInt(productCategory));
        product.setPrivateListId(Integer.parseInt(listId));

        try {
            //inserire il prodotto in DB e get id
            int newProductId = productDAO.insert(product);
            //crea il beans prodctInlist tra nuovo prodotto e la lista
            ProductInList productInList = new ProductInList();
            productInList.setProductId(newProductId);
            productInList.setListId(Integer.parseInt(listId));
            productInList.setStatus(false);
            //inserire la relazione prodctInlist
            productInListDAO.insert(productInList);
        } catch (DAOException ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        String result = "ProductInsertOk";
        //ritorna alla pagina di lista specifica dove è stata inserita il prodotto
        String prevUrl = getServletContext().getContextPath() + "/mylist?listId=" + listId;

        //passare lo risultato  di operazione
        request.getSession().setAttribute("result", result);
        response.sendRedirect(response.encodeRedirectURL(prevUrl));
    }

}
