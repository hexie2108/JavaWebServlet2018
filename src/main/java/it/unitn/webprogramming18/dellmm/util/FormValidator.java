package it.unitn.webprogramming18.dellmm.util;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.Part;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.mail.internet.AddressException;
import org.apache.commons.fileupload.FileItem;

/**
 * validatore della registrazione
 */
public class FormValidator
{

        public static final String FIRST_NAME_KEY = "FirstName",
                    LAST_NAME_KEY = "LastName",
                    INF_PRIVACY_KEY = "InfPrivacy",
                    FIRST_PWD_KEY = "Password",
                    SECOND_PWD_KEY = "Password2",
                    EMAIL_KEY = "Email",
                    AVATAR_KEY = "Avatar",
                    AVATAR_IMG_KEY = "AvatarImg";

        //Configurazioni per la validazione dei campi di input
        public static final int GENERAL_INPUT_MAX_LEN = 44;

        public static final int FIRST_NAME_MAX_LEN = 44,
                    LAST_NAME_MAX_LEN = 44,
                    PWD_MAX_LEN = 44,
                    EMAIL_MAX_LEN = 44;
        public static final int PWD_MIN_LEN = 8,
                    PWD_MIN_UPPER = 1,
                    PWD_MIN_LOWER = 1,
                    PWD_MIN_NUMBER = 1,
                    PWD_MIN_SYMBOL = 1;

        public static final int MAX_LEN_FILE = 15 * 1000000,
                    MIN_LEN_FILE = 0;

        //array di avatar default
        public static final List<String> DEFAULT_AVATARS = Collections.unmodifiableList(Arrays.asList(
                    "user.svg",
                    "user-astronaut.svg",
                    "user-ninja.svg",
                    "user-secret.svg"
        ));
        
        //array di pagina di userSystem
        public static final String[] pageNameOfUserSystem = {
                    "register",
                    "forgotPassword",
                    "resetPassword"
        };

        //custom avatar
        public static final String CUSTOM_AVATAR = "custom";

        public static final String I18N_ERROR_STRING_PREFIX = "validateUser.errors.";

        public static final String PREV_URL_KEY = "prevUrl",
                    REMEMBER_KEY = "remember";

        

        // --- Funzioni di validazione
        /**
         * verifica il valore di un input generico sia valido
         *
         * @param value valore da valutare
         * @return return true valido | false non valido
         */
        public static boolean validateGeneralInput(String value)
        {

                boolean ris = true;
                if (value == null || value.isEmpty())
                {
                        ris = false;
                }

                else if (value.length() > GENERAL_INPUT_MAX_LEN)
                {
                        ris = false;
                }
                return ris;
        }

        /**
         * verifica il formatto dell'email sia valido
         *
         * @param email String di email da verificare
         * @return true valido | false non valido
         */
        private static boolean validateEmailFormat(String email)
        {
                boolean ris = true;

                try {
                        InternetAddress addr = new InternetAddress(email);
                        addr.validate();
                } catch (AddressException ex) {
                        ris = false;
                }

                return ris;
        }

        /**
         * verifica che non ci sia la ripetizione dell'email in DB
         *
         * @param email String di email da verificare
         * @param userDAO DAO per accedere tabella utente in DB
         * @return true se non ce la ripetizione | false se ce la ripetizione
         */
        public static boolean checkEmailRepeat(String email, UserDAO userDAO)
        {
                 boolean ris = true;
                //controlla la sua esistenza
                try {
                        if (userDAO != null && userDAO.checkExistenceOfEmail(email)) {
                                ris = false;
                        }
                } catch (DAOException ignored) {
                       ris = false;
                }
                return ris;
        }

        /**
         * verifica base e il formato del email
         *
         * @param email String di email da verificare
         * @return true valido | false non valido
         */
        public static boolean validateEmail(String email)
        {
                String pattern = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
                boolean ris = true;

                if (email == null || email.isEmpty()) {
                        ris = false;
                }
                else if (email.length() > EMAIL_MAX_LEN) {
                        ris = false;
                } else if (!validateEmailFormat(email) || !email.matches(pattern)) {
                        //se il formato non è valido
                        ris = false;
                }

                return ris;
        }

        /**
         * verifica se il firstName sia valido
         *
         * @param firstName String da verificare
         * @return true valido | false non valido
         */
        public static boolean validateFirstName(String firstName)
        {
                boolean ris = true;
                if (firstName == null || firstName.isEmpty()) {
                        ris = false;
                }
                else if (firstName.length() > FIRST_NAME_MAX_LEN) {
                        ris = false;
                }

                return ris;
        }

        /**
         * verifica se il lastName sia valido
         *
         * @param lastName String da verificare
         * @return true valido | false non valido
         */
        public static boolean validateLastName(String lastName)
        {
                boolean ris = true;

                if (lastName == null || lastName.isEmpty()) {
                        ris = false;
                }
                else if (lastName.length() > LAST_NAME_MAX_LEN) {
                        ris = false;
                }

                return ris;
        }

        /**
         * verifica se la password sia valido
         *
         * @param password String da verificare
         * @return true valido | false non valido
         */
        public static boolean validatePassword(String password)
        {
                boolean ris = true;

                if (password == null || password.isEmpty()) {
                        ris = false;
                }
                else if (password.length() > PWD_MAX_LEN || password.length() < PWD_MIN_LEN) {
                        ris = false;
                }
                else {
                        //controlla se contiene un minuscolo, un maiuscolo, un numero , un simbolo

                        int upper = 0;
                        int lower = 0;
                        int number = 0;
                        int symbol = 0;

                        for (int i = 0; i < password.length(); i++)
                        {
                                if (Character.isLowerCase(password.charAt(i)))
                                {
                                        lower++;
                                }
                                else if (Character.isUpperCase(password.charAt(i)))
                                {
                                        upper++;
                                }
                                else if (Character.isDigit(password.charAt(i)))
                                {
                                        number++;
                                }
                                else if (!Character.isWhitespace(password.charAt(i)))
                                {
                                        symbol++;
                                }
                        }

                        if ((upper < PWD_MIN_UPPER)
                                    || (lower < PWD_MIN_LOWER)
                                    || (number < PWD_MIN_NUMBER)
                                    || (symbol < PWD_MIN_SYMBOL))
                        {
                                ris = false;
                        }
                }

                return ris;
        }

        /**
         * verifica se l'avatar selezionato dall' utente sia valido
         *
         * @param avatar String di avatar
         * @return true valido | false non valido
         */
        public static boolean validateAvatar(String avatar)
        {
                boolean ris = true;

                // Se avatar è custom allora controllo il file, se nessun controllo segnala errori esco immediatamente
                if (avatar == null || avatar.isEmpty()) {
                        ris = false;
                }
                // Se avatar non è custom controllo se è tra i valori permessi
                else if (!avatar.equals(CUSTOM_AVATAR) && DEFAULT_AVATARS.stream().noneMatch(avatar::equals)) {
                        ris = false;
                }

                return ris;
        }

        /**
         * verifica la grandezza e il percorso dell'immagine sia valido
         *
         * @param fileItem
         * @return true valido | false non valido
         */
        public static boolean validateCustomAvatarImg(FileItem fileItem)
        {
                boolean ris = true;

                if (fileItem == null) {
                        ris = false;
                }
                else if (fileItem.getSize() <= FormValidator.MIN_LEN_FILE) {
                        ris = false;
                }
                else if (fileItem.getSize() > FormValidator.MAX_LEN_FILE) {
                        // Non permettere dimensioni superiori ai ~15MB
                        ris = false;
                }
                //controlla il tipo di file
                else if (!isValidFileExtension(fileItem.getContentType())) {
                        ris = false;
                }

                return ris;
        }

        /**
         * controlla se il tipo di file corrisponde un tra tipi di file valido
         *
         * @param contentType il tipo di file input
         * @return true se è uno tra tipi file valido, false altrimenti
         */
        public static boolean isValidFileExtension(String contentType)
        {
                String[] allowTypes = new String[]
                {
                        "jpg", "jpeg", "png", "gif", "bmp"
                };
                if (null == contentType || "".equals(contentType))
                {
                        return false;
                }
                for (String type : allowTypes)
                {
                        if (contentType.contains(type))
                        {
                                return true;
                        }
                }

                return false;

        }

}
