package it.unitn.webprogramming18.dellmm.util;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;

import javax.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.mail.internet.AddressException;
import org.apache.commons.fileupload.FileItem;

/**
 * validatore della registrazione
 */
public class FormValidator {
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
        public static final String[] pageNameOfUserSystem =
        {
                "login",
                "register",
                "forgotPassword",
                "resetPassword",
                "resendEmail",
                "activateUser",
                "service"
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
        public static boolean validateGeneralInput(String value) {
            return ValidatorUtils.validateString(value, GENERAL_INPUT_MAX_LEN) == null;
        }

        /**
         * verifica il formatto dell'email sia valido
         *
         * @param email String di email da verificare
         * @return true valido | false non valido
         */
        private static boolean validateEmailFormat(String email) {
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
        public static boolean validateEmail(String email) {
                return UserValidator.validateEmail(email, null) == null;
        }

        /**
         * verifica se il firstName sia valido
         *
         * @param firstName String da verificare
         * @return true valido | false non valido
         */
        public static boolean validateFirstName(String firstName) {
            return UserValidator.validateFirstName(firstName) == null;
        }

        /**
         * verifica se il lastName sia valido
         *
         * @param lastName String da verificare
         * @return true valido | false non valido
         */
        public static boolean validateLastName(String lastName) {
                return UserValidator.validateLastName(lastName) == null;
        }

        /**
         * verifica se la password sia valido
         *
         * @param password String da verificare
         * @return true valido | false non valido
         */
        public static boolean validatePassword(String password) {
            return UserValidator.validatePassword(password) == null;
        }

        /**
         * verifica se l'avatar selezionato dall' utente sia valido
         *
         * @param avatar String di avatar
         * @return true valido | false non valido
         */
        public static boolean validateAvatar(String avatar) {
            return UserValidator.validateAvatar(avatar) == null;
        }

        /**
         * verifica la grandezza e il percorso dell'immagine sia valido
         *
         * @param fileItem
         * @return true valido | false non valido
         */
        public static boolean validateCustomAvatarImg(FileItem fileItem) {
            return UserValidator.validateCustomAvatarImg(fileItem) == null;
        }

        /**
         * controlla se il tipo di file corrisponde un tra tipi di file valido
         *
         * @param contentType il tipo di file input
         * @return true se è uno tra tipi file valido, false altrimenti
         */
        public static boolean isValidFileExtension(String contentType) {
                return UserValidator.isValidFileExtension(contentType);
        }

}
