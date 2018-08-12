/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.servlets;

import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.ShoppingList;
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
 * @author luca_morgese
 */
public class AddUpdateListServlet extends HttpServlet {

    private ListDAO listDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            listDAO = daoFactory.getDAO(ListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get ListDAO for user storage system", ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // Ottieni configurazione cartella immagini
        String listImgsFolder = getServletContext().getInitParameter("listImgsFolder");
        if (listImgsFolder == null) {
            throw new ServletException("ListImgs folder not configured");
        }

        // TODO: Controllare quanto questa cosa sia orribile
        String realContextPath = request.getServletContext().getRealPath(File.separator);
        if (!realContextPath.endsWith("/")) {
            realContextPath += "/";
        }

        Path path = Paths.get(realContextPath + listImgsFolder);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }


        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (session == null || user == null) {
            //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {

            Integer listId = null;
            listId = Integer.valueOf(request.getParameter("listId"));

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            Integer categoryList = Integer.valueOf(request.getParameter("categoryList"));

            //String img = request.getParameter("img");


            //IMGS

            String uuidImg = null;
            //PART PARAMETER TO BE DEFINED
            String SPECIFIC_FORM_PART_NAME = null;
            Part filePart = request.getPart(SPECIFIC_FORM_PART_NAME);

            if (filePart == null) {
                response.sendError(400, "No image selected for list");
                return;
            } else if (filePart.getSize() == 0) {
                response.sendError(400, "Image has zero size");
                return;
            } else if (filePart.getSize() > 15 * 1000000) { // Non permettere dimensioni superiori ai ~15MB
                response.sendError(400, "Image has size > 15MB");
                return;
            } else {
                uuidImg = UUID.randomUUID().toString();

                try (InputStream fileContent = filePart.getInputStream()) {
                    File file = new File(path.toString(), uuidImg.toString());
                    System.out.println(file.toPath());
                    Files.copy(fileContent, file.toPath());

                } catch (FileAlreadyExistsException ex) { // Molta sfiga
                    getServletContext().log("File \"" + uuidImg.toString() + "\" already exists on the server");
                } catch (RuntimeException ex) {
                    //TODO: handle the exception
                    getServletContext().log("impossible to upload the file", ex);
                }
            }


            try {
                //List bean, NOT a java.util.List
                ShoppingList list = new ShoppingList();

                list.setId(listId);

                list.setName(name);
                list.setDescription(description);
                list.setImg(uuidImg);

                //Owner id is set if new list is created, otherwise, the field is not changed
                list.setCategoryList(categoryList);

                if (listId == null) {
                    //List is new and current user is its owner
                    list.setOwnerId(user.getId());
                    listDAO.insert(list);
                } else {
                    //List is being modified, listId is != null
                    //ownerId is the same as the one prior to the modification
                    ShoppingList prevList = listDAO.getByPrimaryKey(listId);
                    list.setOwnerId(prevList.getOwnerId());
                    listDAO.update(list);
                }
            } catch (DAOException ex) {
                ex.printStackTrace();
                throw new ServletException("Impossible to update or create new list");
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
        return "Servlet that gets info from form to update or create a new list";
    }// </editor-fold>

}
