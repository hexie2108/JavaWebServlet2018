/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;
import it.unitn.webprogramming18.dellmm.util.i18n;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author luca_morgese
 */
public class AddUpdateCategoryListServlet extends HttpServlet {


    private CategoryListDAO categoryListDAO;
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            categoryListDAO = daoFactory.getDAO(CategoryListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ListDAO for user storage system", ex);
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
        String categoryListImgsFolder = getServletContext().getInitParameter("categoryListImgsFolder");
        if (categoryListImgsFolder == null) {
            throw new ServletException("CategoryListImgs folder not configured");
        }

        // TODO: Controllare quanto questa cosa sia orribile
        String realContextPath = request.getServletContext().getRealPath(File.separator);
        if (!realContextPath.endsWith("/")) {
            realContextPath += "/";
        }

        Path path = Paths.get(realContextPath + categoryListImgsFolder);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        
        
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (session == null || user == null) {
            //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            
            //i18n language
            ResourceBundle languageBundle = i18n.getBundle(request);
            
            //Being null or not null defines if occurring action is of "modify catList" or "create catList"
            Integer categoryListId = null;
            categoryListId = Integer.valueOf(request.getParameter("categoryListId"));
            
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            
            //String img1 = request.getParameter("img1");
            //String img2 = request.getParameter("img2");
            //String img3 = request.getParameter("img3");
            
            
            //IMGS
            
            String uuidImg1 = null;
            String uuidImg2 = null;
            String uuidImg3 = null;

            //PART PARAMETER TO BE DEFINED
            String SPECIFIC_FORM_PART1_NAME = null;
            Part filePart1 = request.getPart(SPECIFIC_FORM_PART1_NAME);
            
            //PART PARAMETER TO BE DEFINED
            String SPECIFIC_FORM_PART2_NAME = null;
            Part filePart2 = request.getPart(SPECIFIC_FORM_PART2_NAME);
            
            //PART PARAMETER TO BE DEFINED
            String SPECIFIC_FORM_PART3_NAME = null;
            Part filePart3 = request.getPart(SPECIFIC_FORM_PART3_NAME);
            
            if (filePart1 == null) {
                ServletUtility.sendError(request, response, 400, "servlet.errors.noImg");
                return;
            } else if(filePart1.getSize() == 0){
                ServletUtility.sendError(request, response, 400, "servlet.errors.imgZeroSize");
                return;
            } else if(filePart1.getSize() > 15 * 1000000
                    ||filePart2.getSize() > 15 * 1000000
                    ||filePart3.getSize() > 15 * 1000000){ // Non permettere dimensioni superiori ai ~15MB
                ServletUtility.sendError(request, response, 400, "servlet.errors.imgOverSize");
                return;
            } else {
                uuidImg1 = UUID.randomUUID().toString();
                
                if (filePart2 != null) {
                    uuidImg2 = UUID.randomUUID().toString();
                }
                if (filePart3 != null) {
                    uuidImg3 = UUID.randomUUID().toString();
                }

                try (InputStream fileContent = filePart1.getInputStream()) {
                    File file1 = new File(path.toString(), uuidImg1.toString());
                    System.out.println(file1.toPath());
                    Files.copy(fileContent, file1.toPath());
                    
                    if (filePart2 != null) {
                        File file2 = new File(path.toString(), uuidImg2.toString());
                        System.out.println(file2.toPath());
                        Files.copy(fileContent, file2.toPath());
                    }
                    if (filePart3 != null) {
                        File file3 = new File(path.toString(), uuidImg3.toString());
                        System.out.println(file3.toPath());
                        Files.copy(fileContent, file3.toPath());
                    }      
                    
                } catch (FileAlreadyExistsException ex) { // Molta sfiga
                    getServletContext().log(languageBundle.getString("servlet.errors.alreadyExistingFiles"));
                } catch (RuntimeException ex) {
                    //TODO: handle the exception
                    getServletContext().log(languageBundle.getString("servlet.errors.unaploadableFile"), ex);
                }
            }
            
            
            try {
                CategoryList categoryList = new CategoryList();

                categoryList.setId(categoryListId);

                categoryList.setName(name);
                categoryList.setDescription(description);
                categoryList.setImg1(uuidImg1);
                categoryList.setImg2(uuidImg2);
                categoryList.setImg3(uuidImg3);

                if (categoryListId == null) {
                    categoryListDAO.insert(categoryList);
                } else {
                    categoryListDAO.update(categoryList);
                }
                
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException(languageBundle.getString("servlet.errors.addUpdateFailed"));
            }
            
            response.sendRedirect("/WEB-INF/jsp/yourHome.jsp");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Inserts into persistence system new CategoryList entry";
    }// </editor-fold>

}
