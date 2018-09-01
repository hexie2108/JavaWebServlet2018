package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@MultipartConfig
public class ModifyUserService extends HttpServlet
{
        private String subImg(HttpServletRequest request, Path path, String prevImg, InputStream inputStream) throws IOException, ServletException {
            // If prevImg is one of default's set prevImg to null to prevent deletion of the file
            if (prevImg != null && FormValidator.DEFAULT_AVATARS.stream().anyMatch(prevImg::equals)) {
                    prevImg = null;
            }

            return ServletUtility.insertImage(
                    request,
                    path,
                    prevImg,
                    inputStream,
                    ConstantsUtils.IMAGE_OF_USER_WIDTH,
                    ConstantsUtils.IMAGE_OF_USER_HEIGHT
            );
        }


        private UserDAO userDAO;

        @Override
        public void init() throws ServletException
        {
                DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
                if (daoFactory == null)
                {
                        throw new ServletException("Impossible to get db factory for user storage system");
                }

                try
                {
                        userDAO = daoFactory.getDAO(UserDAO.class);
                }
                catch (DAOFactoryException ex)
                {
                        throw new ServletException("Impossible to get UserDAO for user storage system", ex);
                }
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                Path path = ServletUtility.getFolder(getServletContext(), "avatarsFolder");

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
                        ServletUtility.sendError(request, response, 500, "error.parseRequest");
                        return;
                }

                String firstName = null;
                String lastName = null;
                String password = null;
                String avatar = null;

                FileItem customAvatarImgFile = null;

                if (items != null && items.size() > 0)
                {
                        for (FileItem item : items)
                        {
                                //se oggetto è un campo di form
                                if (item.isFormField())
                                {
                                        switch (item.getFieldName())
                                        {

                                                case FormValidator.FIRST_NAME_KEY:
                                                        firstName = item.getString("UTF-8");
                                                        break;
                                                case FormValidator.LAST_NAME_KEY:
                                                        lastName = item.getString("UTF-8");
                                                        break;
                                                case FormValidator.FIRST_PWD_KEY:
                                                        password = item.getString("UTF-8");
                                                        break;
                                                case FormValidator.AVATAR_KEY:
                                                        avatar = item.getString("UTF-8");
                                                        break;

                                        }
                                }
                                //se item non è un campo normale di form e non è vuoto
                                else if (!item.getString("UTF-8").equals(""))
                                {

                                        // se name uguale "productImg",
                                        if (item.getFieldName().equals(FormValidator.AVATAR_IMG_KEY))
                                        {
                                                customAvatarImgFile = item;
                                        }

                                }

                        }
                }

                //check tutti parametri necessari
                if (!FormValidator.validateFirstName(firstName)){
                     ServletUtility.sendError(request, response, 400, "validateUser.errors.FIRST_NAME_NOT_VALID");
                     return;
                }
                if (!FormValidator.validateLastName(lastName)){
                     ServletUtility.sendError(request, response, 400, "validateUser.errors.LAST_NAME_NOT_VALID");
                     return;
                }

                User user = (User) request.getSession().getAttribute("user");
                //aggiorna i dati di user in db
                try
                {
                        //se vuole modificare avatar
                        if (avatar != null)
                        {
                                if (!FormValidator.validateAvatar(avatar)){
                                     ServletUtility.sendError(request, response, 400, "validateUser.errors.AVATAR_NOT_VALID");
                                     return;
                                }

                                //elimina avatar vecchio

                                //se vuole caricare una nuova avatar personale
                                if (avatar.equals(FormValidator.CUSTOM_AVATAR))
                                {
                                        //check file caricato
                                        if (customAvatarImgFile == null || customAvatarImgFile.getSize() == 0) {
                                            ServletUtility.sendError(request, response, 400, "validateUser.errors.AVATAR_IMG_MISSING");
                                            return;
                                        }

                                        if (!FormValidator.validateCustomAvatarImg(customAvatarImgFile)){
                                             ServletUtility.sendError(request, response, 400, "validateUser.errors.AVATAR_IMG_NOT_VALID");
                                             return;
                                        }

                                        avatar = subImg(request, path, user.getImg(), customAvatarImgFile.getInputStream());
                                }

                                user.setImg(avatar);
                        }

                        //se vuole cambiare anche la password
                        if (password != null && !password.isEmpty())
                        {
                                if (!FormValidator.validatePassword(password)){
                                     ServletUtility.sendError(request, response, 400, "validateUser.errors.PASSWORD_NOT_VALID");
                                     return;
                                }

                                user.setPassword(MD5Utils.getMD5(password));
                        }

                        user.setName(firstName);
                        user.setSurname(lastName);

                        userDAO.update(user);
                        //set user aggiornato
                        request.getSession().setAttribute("user", user);
                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);

                }
                catch (NoSuchAlgorithmException ex)
                {
                        throw new ServletException(rb.getString("errros.noSuchAlgorithmMD5"), ex);
                }

                //ritorna alla pagina di login
                String prevUrl = getServletContext().getContextPath() + "/modifyUser?update=ok";
                response.sendRedirect(response.encodeRedirectURL(prevUrl));
        }

}
