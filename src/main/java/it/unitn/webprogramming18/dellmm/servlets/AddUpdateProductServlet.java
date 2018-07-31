/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductInListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.javaBeans.ProductInList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * SOME STUFF (LINKS, ERROR ACTIONS) YET TO BE DEFINED
 * 
 * This servlet works as follows:
 * 
 * STD USER
 * The case where a std user is trying to modify a product has already been filtered in PrepareAddUpdateProductPageServlet,
 *      so if user.isAdmin() == false, this servlet _creates_ a private product, and links the product to the list,
 *      as we know that the only action a std user can be performing is that of creating a private product to add it to a list.
 *      In this scenario, listId _must not_ be null. If it's null, it's an error.
 * 
 * ADMIN USER
 * In case the user is an admin, we know that
 *      if listId parameter is null:
 *          the admin is either creating a new product, or updating an existing product.
 *              so if a productId parameter != null, it means that a product is being updated
 *              if productId parameter == null, a product is being created.
 *      if listId parameter != null:
 *          the admin is either creating a new product, which will be added to a selected list,
 *          or updating an existing product.
 *              so if a productId parameter != null, it means that only the action of updating a product will be performed.
 *              if a productId parameter == null, it means that a new product will be created, and then added to the list
 * 
 * @author luca_morgese
 */
public class AddUpdateProductServlet extends HttpServlet {

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
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get DAOs for user storage system", ex);
        }
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
// Ottieni configurazione cartella immagini
        String productImgsFolder = getServletContext().getInitParameter("productImgsFolder");
        if (productImgsFolder == null) {
            throw new ServletException("productImgs folder not configured");
        }

        // TODO: Controllare quanto questa cosa sia orribile
        String realContextPath = request.getServletContext().getRealPath(File.separator);
        if (!realContextPath.endsWith("/")) {
            realContextPath += "/";
        }

        Path path = Paths.get(realContextPath + productImgsFolder);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        
        
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (session == null || user == null) {
            //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            
            //ProductId, listId null or not null values drive flow of casistics
            Integer listId = Integer.valueOf(request.getParameter("listId"));
            Integer productId = Integer.valueOf(request.getParameter("productId"));
            
            //Non admin user. A "private" product must be created.
            //Non update action => productId attribute is existing and not null
            if (user.isIsAdmin() == false) {
                
                //Additional check on must-exist variables
                if (productId != null || listId == null) {
                    //ERROR abort action to implement
                }
                
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                Integer categoryProductId = Integer.valueOf(request.getParameter("name"));
                Integer privateListId = Integer.valueOf(request.getParameter("lsitId"));
                
                //String img = request.getParameter("img");
                //String logo = request.getParameter("logo");                

                //IMGS

                String uuidImg = null;
                String uuidLogo = null;

                //PART PARAMETER TO BE DEFINED
                String SPECIFIC_FORM_PART1_NAME = null;
                Part filePart1 = request.getPart(SPECIFIC_FORM_PART1_NAME);

                //PART PARAMETER TO BE DEFINED
                String SPECIFIC_FORM_PART2_NAME = null;
                Part filePart2 = request.getPart(SPECIFIC_FORM_PART2_NAME);


                if (filePart1 == null || filePart2 == null) {
                    response.sendError(400, "No image selected for product");
                    return;
                } else if(filePart1.getSize() == 0 || filePart1.getSize() == 0){
                    response.sendError(400, "Image has zero size");
                    return;
                } else if(filePart1.getSize() > 15 * 1000000
                        ||filePart2.getSize() > 15 * 1000000){ // Non permettere dimensioni superiori ai ~15MB
                    response.sendError(400, "One or more images have size > 15MB");
                    return;
                } else {
                    uuidImg = UUID.randomUUID().toString();
                    uuidLogo = UUID.randomUUID().toString();

                    try (InputStream fileContent = filePart1.getInputStream()) {
                        File file1 = new File(path.toString(), uuidImg.toString());
                        System.out.println(file1.toPath());
                        Files.copy(fileContent, file1.toPath());

                        File file2 = new File(path.toString(), uuidLogo.toString());
                        System.out.println(file2.toPath());
                        Files.copy(fileContent, file2.toPath());
    

                    } catch (FileAlreadyExistsException ex) { // Molta sfiga
                        getServletContext().log("One or more of the files you added already exist on the server");
                    } catch (RuntimeException ex) {
                        //TODO: handle the exception
                        getServletContext().log("impossible to upload the file", ex);
                    }
                }


                
                
                try {
                    
                    //Creates product bean and insert in database
                    Product product = new Product();

                    product.setName(name);
                    product.setDescription(description);
                    product.setImg(uuidImg);
                    product.setLogo(uuidLogo);
                    product.setCategoryProductId(categoryProductId);
                    product.setPrivateListId(privateListId);

                    productId = productDAO.insert(product);
                    
                    //Inserts (and "links") newly created private product in list
                    ProductInList productInList = new ProductInList();
                    
                    productInList.setListId(listId);
                    productInList.setProductId(productId);
                    productInList.setStatus(false);

                    productInListDAO.insert(productInList);
                    
                    //LINK TO BE DEFINED
                    request.getRequestDispatcher("/WEB-INF/jsp/RESEARCH_PRODUCT_TO_ADD_TO_LIST_LINK").forward(request, response);
                    return;
                    
                } catch (DAOException ex) {
                    ex.printStackTrace();
                    throw new ServletException("Impossible to add new private product");
                }
            
                
            //Admin user
            } else {
                
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                Integer categoryProductId = Integer.valueOf(request.getParameter("name"));
                
                //String img = request.getParameter("img");
                //String logo = request.getParameter("logo");
                
                

                //IMGS

                String uuidImg = null;
                String uuidLogo = null;

                //PART PARAMETER TO BE DEFINED
                String SPECIFIC_FORM_PART1_NAME = null;
                Part filePart1 = request.getPart(SPECIFIC_FORM_PART1_NAME);

                //PART PARAMETER TO BE DEFINED
                String SPECIFIC_FORM_PART2_NAME = null;
                Part filePart2 = request.getPart(SPECIFIC_FORM_PART2_NAME);


                if (filePart1 == null || filePart2 == null) {
                    response.sendError(400, "No image selected for product");
                    return;
                } else if(filePart1.getSize() == 0 || filePart1.getSize() == 0){
                    response.sendError(400, "Image has zero size");
                    return;
                } else if(filePart1.getSize() > 15 * 1000000
                        ||filePart2.getSize() > 15 * 1000000){ // Non permettere dimensioni superiori ai ~15MB
                    response.sendError(400, "One or more images have size > 15MB");
                    return;
                } else {
                    uuidImg = UUID.randomUUID().toString();
                    uuidLogo = UUID.randomUUID().toString();

                    try (InputStream fileContent = filePart1.getInputStream()) {
                        File file1 = new File(path.toString(), uuidImg.toString());
                        System.out.println(file1.toPath());
                        Files.copy(fileContent, file1.toPath());

                        File file2 = new File(path.toString(), uuidLogo.toString());
                        System.out.println(file2.toPath());
                        Files.copy(fileContent, file2.toPath());
    

                    } catch (FileAlreadyExistsException ex) { // Molta sfiga
                        getServletContext().log("One or more of the files you added already exist on the server");
                    } catch (RuntimeException ex) {
                        //TODO: handle the exception
                        getServletContext().log("impossible to upload the file", ex);
                    }
                }


                
                
                try {
                    
                    Product product = new Product();

                    product.setId(productId);
                    
                    product.setName(name);
                    product.setDescription(description);
                    product.setImg(uuidImg);
                    product.setLogo(uuidLogo);
                    product.setCategoryProductId(categoryProductId);
                    
                    //Product created by admin is universally visualizable
                    Integer nullInt = null;
                    product.setPrivateListId(nullInt);
                    
                    //new product is being created by admin
                    if (productId == null) {
                        productId = productDAO.insert(product);
                        //It is being created from "add to list" action, inserts it in admin-user's list as well
                        if (listId != null) {
                            ProductInList productInList = new ProductInList();
                            productInList.setListId(listId);
                            productInList.setProductId(productId);
                            productInList.setStatus(false);

                            productInListDAO.insert(productInList);
                            
                            //FORWARD LINK TO BE DEFINED
                            request.getRequestDispatcher("RESEARCH_PRODUCT_TO_ADD_TO_LIST_LINK").forward(request, response);
                            return;
                        } else {
                            //REDIRECT PAGE TO BE DEFINED
                            response.sendRedirect("/WEB-INF/jsp/ADMIN_CREATE_PRODUCT.jsp");
                            return;
                        }
                        
                    //Else means that productId != null, so an existing product is being modified
                    //and no further action is required
                    } else {
                        productDAO.update(product);
                        if (listId != null) {
                            //FORWARD LINK TO BE DEFINED
                            request.getRequestDispatcher("RESEARCH_PRODUCT_TO_ADD_TO_LIST_LINK").forward(request, response);
                            return;
                        } else {
                            //REDIRECT PAGE TO BE DEFINED
                            response.sendRedirect("/WEB-INF/jsp/ADMIN_CREATE_PRODUCT.jsp");
                            return;
                        }
                    }
                    
                } catch (DAOException ex) {
                    ex.printStackTrace();
                    throw new ServletException("Impossible to add new, or modify a, product");
                }
            }
            
            
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet that manages product creation/update page accordingly to"
                + "user status (std, admin), "
                + "and user action (admin-> add/create product; std, admin -> insert product in list)";
    }// </editor-fold>

}
