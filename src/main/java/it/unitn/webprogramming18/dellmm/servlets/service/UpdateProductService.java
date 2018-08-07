package it.unitn.webprogramming18.dellmm.servlets.service;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.PermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCPermissionDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.jdbc.JDBCProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.javaBeans.Permission;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;
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
 * servizio per inserire il prodotto privato
 *
 * @author luca_morgese
 */
public class UpdateProductService extends HttpServlet
{

        private PermissionDAO permissionDAO;
        private ProductDAO productDAO;
        private ProductInListDAO productInListDAO;

        //il percorso base per tutte le immagini
        private static final String IMAGE_BASE_PATH = "image";

        // la cartella per immagine di prodotto
        private static final String IMAGE_PRODUCT = "product";
        // la cartella per immagine di logo di prodotto
        private static final String IMAGE_LOGO_PRODUCT = "productLogo";

        //le dimensioni dell'immagini
        private static final int IMAGE_WIDTH = 800;
        private static final int IMAGE_HEIGHT = 800;

        //le dimensioni del logo
        private static final int LOGO_WIDTH = 800;
        private static final int LOGO_HEIGHT = 400;

        @Override
        public void init() throws ServletException
        {

                productDAO = new JDBCProductDAO();
                permissionDAO = new JDBCPermissionDAO();
                productInListDAO = new JDBCProductInListDAO();
        }

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

                String productName = null;
                String productCategory = null;
                String productDescription = null;
                String listId = null;
                String prodcutImg = null;
                String productLogo = null;
                FileItem prodcutImgFileItem = null;
                FileItem prodcutLogoFileItem = null;

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
                                                case "productName":
                                                        productName = item.getString();
                                                        break;
                                                case "productCategory":
                                                        productCategory = item.getString();
                                                        break;
                                                case "productDescription":
                                                        productDescription = item.getString();
                                                        break;
                                                case "listId":
                                                        listId = item.getString();
                                                        break;

                                        }
                                }
                                //se item non è un campo normale di form e non è vuoto
                                else if (!item.getString().equals(""))
                                {

                                        // se name uguale "productImg",
                                        if (item.getFieldName().equals("productImg"))
                                        {
                                                prodcutImgFileItem = item;
                                        }
                                        //se item name uguale "prodocutLogo"
                                        else if (item.getFieldName().equals("prodocutLogo"))
                                        {
                                                prodcutLogoFileItem = item;
                                        }

                                }

                        }
                }

                //se variabile sono nullo
                CheckErrorUtils.isNull(productName, "manca il parametro productName");
                CheckErrorUtils.isNull(productCategory, "manca il parametro productCategory");
                CheckErrorUtils.isNull(productDescription, "manca il parametro productDescription");
                CheckErrorUtils.isNull(listId, "manca il parametro listId");
                CheckErrorUtils.isNull(prodcutImgFileItem, "manca il prametro file prodcutImgFileItem");
                CheckErrorUtils.isNull(prodcutLogoFileItem, "manca il prametro file prodcutLogoFileItem");

                //get user corrente
                User user = (User) request.getSession().getAttribute("user");
                //get permesso dell'utente su tale lista
                try
                {
                        Permission permission = permissionDAO.getUserPermissionOnListByIds(user.getId(), Integer.parseInt(listId));
                        //se il permesso è  nullo
                        CheckErrorUtils.isNull(permission, "non hai nessun permesso su tale lista");
                        //se utente non ha il permesso per inserire il prodotto
                        CheckErrorUtils.isFalse(permission.isAddObject(), "non hai il permesso di inserire il prodotto a tale lista");
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);
                }

                //set il percorso complete per salvare immagine
                String uploadPath = request.getServletContext().getRealPath("/") + IMAGE_BASE_PATH;
                //salva l'immagine di prodotto e get il nome salvato
                prodcutImg = FileUtils.upload(prodcutImgFileItem, uploadPath + File.separator + IMAGE_PRODUCT, IMAGE_WIDTH, IMAGE_HEIGHT);
                //salva l'immagine di logo e get il nome salvato
                productLogo = FileUtils.upload(prodcutLogoFileItem, uploadPath + File.separator + IMAGE_LOGO_PRODUCT, LOGO_WIDTH, LOGO_HEIGHT);

                //crea beans di prodotto
                Product product = new Product();
                product.setName(productName);
                product.setDescription(productDescription);
                product.setImg(prodcutImg);
                product.setLogo(productLogo);
                product.setCategoryProductId(Integer.parseInt(productCategory));
                product.setPrivateListId(Integer.parseInt(listId));

                try
                {
                        //inserire il prodotto in DB e get id
                        int newProductId = productDAO.insert(product);
                        //crea il beans prodctInlist tra nuovo prodotto e la lista
                        ProductInList productInList = new ProductInList();
                        productInList.setProductId(newProductId);
                        productInList.setListId(Integer.parseInt(listId));
                        productInList.setStatus(false);
                        //inserire la relazione prodctInlist
                        productInListDAO.insert(productInList);
                }
                catch (DAOException ex)
                {
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
