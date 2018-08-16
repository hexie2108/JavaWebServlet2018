package it.unitn.webprogramming18.dellmm.servlets.userSystem.service;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.email.EmailFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.User;
import it.unitn.webprogramming18.dellmm.util.CheckErrorUtils;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.FileUtils;
import it.unitn.webprogramming18.dellmm.util.FormValidator;
import it.unitn.webprogramming18.dellmm.util.MD5Utils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

public class RegisterService extends HttpServlet {

    private static final String JSP_PAGE_PATH = "/WEB-INF/jsp/userSystem/register.jsp";

    private UserDAO userDAO;
    private EmailFactory emailFactory;

    @Override
    public void init() throws ServletException {

        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            userDAO = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get UserDAO for user storage system", ex);
        }

        emailFactory = (EmailFactory) super.getServletContext().getAttribute("emailFactory");
        CheckErrorUtils.isNull(emailFactory, "Impossible to get email factory for email system");

    }

    /**
     * post occupa il servizio della registrazione
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

                String email = null;
                String firstName = null;
                String lastName = null;
                String password = null;
                String avatar = null;
                String privacy = null;
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
                                                //get il valore di vari parametri
                                                case FormValidator.EMAIL_KEY:
                                                        email = item.getString();
                                                        break;
                                                case FormValidator.FIRST_NAME_KEY:
                                                        firstName = item.getString();
                                                        break;
                                                case FormValidator.LAST_NAME_KEY:
                                                        lastName = item.getString();
                                                        break;
                                                case FormValidator.FIRST_PWD_KEY:
                                                        password = item.getString();
                                                        break;
                                                case FormValidator.AVATAR_KEY:
                                                        avatar = item.getString();
                                                        break;
                                                case FormValidator.INF_PRIVACY_KEY:
                                                        privacy = item.getString();
                                                        break;

                                        }
                                }
                                //se item non è un campo normale di form e non è vuoto
                                else if (!item.getString().equals(""))
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
                CheckErrorUtils.isFalse(FormValidator.validateEmail(email), "email non è valido");
                CheckErrorUtils.isFalse(FormValidator.checkEmailRepeat(email, userDAO), "email già presente");
                CheckErrorUtils.isFalse(FormValidator.validateFirstName(firstName), "il first name non è valido");
                CheckErrorUtils.isFalse(FormValidator.validateLastName(lastName), "il first name non è valido");
                CheckErrorUtils.isFalse(FormValidator.validatePassword(password), "la password non è valido");
                CheckErrorUtils.isFalse(FormValidator.validateAvatar(avatar), "l'avatar selezionato non è valido");


                boolean acceptedPrivacy = false;
                //se utente ha accettato la privacy durante fase di registrazione
                if (privacy != null)
                {
                        acceptedPrivacy = true;
                }

                //se avatar è custom
                if (avatar != null && avatar.equals(FormValidator.CUSTOM_AVATAR))
                {
                        //check file caricato
                        CheckErrorUtils.isNull(customAvatarImgFile, "file di img caricato è nullo");
                        CheckErrorUtils.isFalse(FormValidator.validateCustomAvatarImg(customAvatarImgFile), "file di img caricato non è valido");
                        //set il percorso complete per salvare immagine di user
                        String uploadPath = request.getServletContext().getRealPath("/") + ConstantsUtils.IMAGE_BASE_PATH + File.separator + ConstantsUtils.IMAGE_OF_USER;
                        //salva l'immagine di user e get il nome salvato
                        avatar = FileUtils.upload(customAvatarImgFile, uploadPath, ConstantsUtils.IMAGE_OF_USER_WIDTH, ConstantsUtils.IMAGE_OF_USER_HEIGHT);
                }

                // Genera l'utente, manda la mail di verifica e in caso visualizza gli errori
                try
                {
                        User user = userDAO.generateUser(
                                    firstName,
                                    lastName,
                                    email,
                                    MD5Utils.getMD5(password),
                                    avatar,
                                    acceptedPrivacy);

                        //manda email

                        emailFactory.sendEmailOfRegistration(user, request);

                }
                catch (DAOException ex)
                {
                        throw new ServletException(ex.getMessage(), ex);

                }
                catch (MessagingException ex)
                {
                        throw new ServletException("errore durente la creazione e l'invio del email per la registrazione", ex);
                }
                catch (UnsupportedEncodingException ex)
                {
                        throw new ServletException("errore durente la codifica dei caratteri", ex);
                }
                catch (NoSuchAlgorithmException ex)
                {
                        throw new ServletException("errore per la mancanza dell'algoritmo MD5 in ambiente di esecuzione", ex);
                }

                //ritorna alla pagina di login
                String prevUrl = getServletContext().getContextPath() + "/login?notice=awaitingActivation";
                response.sendRedirect(response.encodeRedirectURL(prevUrl));
        }
}
