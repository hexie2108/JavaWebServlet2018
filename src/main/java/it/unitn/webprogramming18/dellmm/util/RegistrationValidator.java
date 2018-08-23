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

/**
 * validatore della registrazione
 */
public class RegistrationValidator {

    public static final String FIRST_NAME_KEY = "FirstName",
            LAST_NAME_KEY = "LastName",
            INF_PRIVACY_KEY = "InfPrivacy",
            FIRST_PWD_KEY = "Password",
            SECOND_PWD_KEY = "Password2",
            EMAIL_KEY = "Email",
            AVATAR_KEY = "Avatar",
            AVATAR_IMG_KEY = "AvatarImg";
    public static final int MAX_LEN_FILE = 15 * 1000000,
            MIN_LEN_FILE = 0;

    //array di avatar default
    public static final List<String> DEFAULT_AVATARS = Collections.unmodifiableList(Arrays.asList(
            "user.svg",
            "user-astronaut.svg",
            "user-graduate.svg",
            "user-md.svg",
            "user-ninja.svg",
            "user-secret.svg"
    ));
    public static final String CUSTOM_AVATAR = "custom";
    public static final String I18N_ERROR_STRING_PREFIX = "validateUser.errors.";
    // --- Configurazioni per la validazione dei campi
    private static final int FIRST_NAME_MAX_LEN = 44,
            LAST_NAME_MAX_LEN = 44,
            PWD_MAX_LEN = 44,
            EMAIL_MAX_LEN = 44;
    private static final int PWD_MIN_LEN = 8,
            PWD_MIN_UPPER = 1,
            PWD_MIN_LOWER = 1,
            PWD_MIN_NUMBER = 1,
            PWD_MIN_SYMBOL = 1;

    // --- Funzioni di validazioneTypeError

    /**
     * verifica il formatto dell'email sia valido
     *
     * @param email String di email da verificare
     * @return true valido | false non valido
     */
    private static boolean validateEmailFormat(String email) {
        boolean ris = false;
        try {
            InternetAddress addr = new InternetAddress(email);
            addr.validate();
            ris = true;
        } catch (Exception ignored) {
        }

        return ris;
    }

    // --- Funzioni di validazione

    /**
     * verifica che non ci sia la ripetizione dell'email in DB
     *
     * @param email   String di email da verificare
     * @param userDAO DAO per accedere tabella utente in DB
     * @return null se valido, altrimenti ErrorMessage
     */
    public static ErrorMessage validateEmail(String email, UserDAO userDAO) {
        if (email == null || email.isEmpty()) {
            return ErrorMessage.EMAIL_MISSING;
        }

        if (email.length() > EMAIL_MAX_LEN) {
            return ErrorMessage.EMAIL_TOO_LONG;
        }

        if (!validateEmailFormat(email)) { // Controllo che la mail abbia un formato valido
            return ErrorMessage.EMAIL_NOT_VALID;
        } else {
            try {
                if (userDAO != null && userDAO.checkUserRegisteredByEmail(email) != 0) {
                    return ErrorMessage.EMAIL_ALREADY_USED;
                }
            } catch (DAOException ignored) {
                // If the check doesn't work postpone to real registration
            }
        }

        return null;
    }

    /**
     * verifica se il firstName sia valido
     *
     * @param firstName String da verificare
     * @return null se valido, altrimenti ErrorMessage
     */
    public static ErrorMessage validateFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            return ErrorMessage.FIRST_NAME_MISSING;
        }

        if (firstName.length() > FIRST_NAME_MAX_LEN) {
            return ErrorMessage.FIRST_NAME_TOO_LONG;
        }

        return null;
    }

    /**
     * verifica se il lastName sia valido
     *
     * @param lastName String da verificare
     * @return null se valido, altrimenti ErrorMessage
     */
    public static ErrorMessage validateLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            return ErrorMessage.LAST_NAME_MISSING;
        }

        if (lastName.length() > LAST_NAME_MAX_LEN) {
            return ErrorMessage.LAST_NAME_TOO_LONG;
        }

        return null;
    }

    /**
     * verifica se la password sia valido
     *
     * @param password String da verificare
     * @return null se valido, altrimenti ErrorMessage
     */
    public static ErrorMessage validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return ErrorMessage.PASSWORD_MISSING;
        }

        if (password.length() > PWD_MAX_LEN) {
            return ErrorMessage.PASSWORD_TOO_LONG;
        }

        int upper = 0;
        int lower = 0;
        int number = 0;
        int symbol = 0;

        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i)))
                lower++;
            else if (Character.isUpperCase(password.charAt(i)))
                upper++;
            else if (Character.isDigit(password.charAt(i)))
                number++;
            else if (!Character.isWhitespace(password.charAt(i)))
                symbol++;
        }

        if ((password.length() < PWD_MIN_LEN) ||
                (upper < PWD_MIN_UPPER) ||
                (lower < PWD_MIN_LOWER) ||
                (number < PWD_MIN_NUMBER) ||
                (symbol < PWD_MIN_SYMBOL)) {
            return ErrorMessage.PASSWORD_NOT_VALID;
        }

        return null;
    }

    /**
     * verifica se la ripetizione di password sia valida e sia uguale al
     * password
     *
     * @param firstPassword  password principale
     * @param secondPassword ripetizione di password
     * @return null se valido, altrimenti ErrorMessage
     */
    public static ErrorMessage validateSecondPassword(String firstPassword, String secondPassword) {
        if (firstPassword == null
                || secondPassword == null
                || firstPassword.isEmpty()
                || secondPassword.isEmpty()) {
            return ErrorMessage.PASSWORD2_MISSING;
        }

        if (firstPassword.length() > PWD_MAX_LEN) {
            return ErrorMessage.PASSWORD_TOO_LONG;
        }

        if (!firstPassword.equals(secondPassword)) { // Controlla che le due password coincidano
            return ErrorMessage.PASSWORD2_NOT_SAME;
        }

        return null;
    }

    /**
     * verifica se l'utente ha accettato la privacy
     *
     * @param infPrivacy valore di input da form
     * @return null se ok, altrimenti ErrorMessage
     */
    public static ErrorMessage validateInfPrivacy(String infPrivacy) {
        if (infPrivacy == null) { // Controlla che sia stata accettata l'informativa alla privacy
            return ErrorMessage.INF_PRIVACY_MISSING;
        }

        return null;
    }

    /**
     * verifica se l'avatar selezionato dall' utente sia valido
     *
     * @param avatar String di avatar
     * @return null se valido, altrimenti ErrorMessage
     */
    public static ErrorMessage validateAvatar(String avatar) {
        // Se avatar è custom allora controllo il file, se nessun controllo segnala errori esco immediatamente
        if (avatar == null || avatar.isEmpty()) {
            return ErrorMessage.AVATAR_MISSING;
        }

        // Se avatar non è custom controllo se è tra i valori permessi
        if (!avatar.equals(CUSTOM_AVATAR) && DEFAULT_AVATARS.stream().noneMatch(avatar::equals)) {
            return ErrorMessage.AVATAR_NOT_VALID;
        }

        return null;
    }

    /**
     * verifica la grandezza e il percorso dell'immagine sia valido
     *
     * @param filePart
     * @return
     */
    public static ErrorMessage validateAvatarImg(Part filePart) {
        if (filePart == null) {
            return ErrorMessage.AVATAR_IMG_MISSING;
        } else if (filePart.getSize() <= RegistrationValidator.MIN_LEN_FILE) {
            return ErrorMessage.AVATAR_IMG_ZERO_DIM;
        } else if (filePart.getSize() > RegistrationValidator.MAX_LEN_FILE) { // Non permettere dimensioni superiori ai ~15MB
            return ErrorMessage.AVATAR_IMG_TOO_BIG;
        } else if (!filePart.getContentType().startsWith("image/")) { // Not safe, uses extensions
            return ErrorMessage.AVATAR_IMG_NOT_IMG;
        }

        return null;
    }

    /**
     * filtrare e convalidare hashMap della registrazione
     *
     * @param userDAO DAO per accedere tabella utente
     * @param kv      hashMap della registrazione da filtrare
     * @return
     */
    public static HashMap<String, ErrorMessage> partialValidate(
            UserDAO userDAO,
            HashMap<String, Object> kv
    ) {
        HashMap<String, ErrorMessage> messages = new HashMap<>();

        if (kv.containsKey(FIRST_NAME_KEY)) {
            String firstName = (String) kv.get(FIRST_NAME_KEY);

            ErrorMessage messageFirstName = validateFirstName(firstName);
            if (messageFirstName != null) {
                messages.put(FIRST_NAME_KEY, messageFirstName);
            }
        }

        if (kv.containsKey(LAST_NAME_KEY)) {
            String lastName = (String) kv.get(LAST_NAME_KEY);

            ErrorMessage messageLastName = validateLastName(lastName);
            if (messageLastName != null) {
                messages.put(LAST_NAME_KEY, messageLastName);
            }
        }

        if (kv.containsKey(INF_PRIVACY_KEY)) {
            String infPrivacy = (String) kv.get(INF_PRIVACY_KEY);

            ErrorMessage messageInfPrivacy = validateInfPrivacy(infPrivacy);
            if (messageInfPrivacy != null) {
                messages.put(INF_PRIVACY_KEY, messageInfPrivacy);
            }
        }

        if (kv.containsKey(FIRST_PWD_KEY)) {
            String firstPassword = (String) kv.get(FIRST_PWD_KEY);

            ErrorMessage messagePwd = validatePassword(firstPassword);
            if (messagePwd != null) {
                messages.put(FIRST_PWD_KEY, messagePwd);
            }
        }

        if (kv.containsKey(FIRST_PWD_KEY) && kv.containsKey(SECOND_PWD_KEY)) {
            String firstPassword = (String) kv.get(FIRST_PWD_KEY);
            String secondPassword = (String) kv.get(SECOND_PWD_KEY);

            ErrorMessage messageSecondPassword = validateSecondPassword(firstPassword, secondPassword);
            if (messageSecondPassword != null) {
                messages.put(SECOND_PWD_KEY, messageSecondPassword);
            }
        }

        if (kv.containsKey(EMAIL_KEY)) {
            String email = (String) kv.get(EMAIL_KEY);

            ErrorMessage messageValidateEmail = validateEmail(email, userDAO);
            if (messageValidateEmail != null) {
                messages.put(EMAIL_KEY, messageValidateEmail);
            }
        }

        if (kv.containsKey(AVATAR_KEY)) {
            String avatar = (String) kv.get(AVATAR_KEY);

            ErrorMessage messageValidateAvatar = validateAvatar(avatar);
            if (messageValidateAvatar != null) {
                messages.put(AVATAR_KEY, messageValidateAvatar);
            }
        }

        if (kv.containsKey(AVATAR_KEY) && kv.containsKey(AVATAR_IMG_KEY) && kv.get(AVATAR_KEY).equals(CUSTOM_AVATAR)) {
            Part filePart = (Part) kv.get(AVATAR_IMG_KEY);

            ErrorMessage messageValidateAvatarImg = validateAvatarImg(filePart);
            if (messageValidateAvatarImg != null) {
                messages.put(AVATAR_IMG_KEY, messageValidateAvatarImg);
            }
        }

        return messages;
    }

    /**
     * crea un hashMap per memorizzare tutti i dati della registrazione
     *
     * @param userDAO
     * @param firstName
     * @param lastName
     * @param email
     * @param firstPassword
     * @param secondPassword
     * @param infPrivacy
     * @param avatar
     * @param avatarImg
     * @return
     */

    public static HashMap<String, ErrorMessage> createValidationMessages(
            UserDAO userDAO,
            String firstName,
            String lastName,
            String email,
            String firstPassword,
            String secondPassword,
            String infPrivacy,
            String avatar,
            Part avatarImg
    ) {
        HashMap<String, Object> kv = new HashMap<>();
        kv.put(FIRST_NAME_KEY, firstName);
        kv.put(LAST_NAME_KEY, lastName);
        kv.put(EMAIL_KEY, email);
        kv.put(FIRST_PWD_KEY, firstPassword);
        kv.put(SECOND_PWD_KEY, secondPassword);
        kv.put(INF_PRIVACY_KEY, infPrivacy);
        kv.put(AVATAR_KEY, avatar);
        kv.put(AVATAR_IMG_KEY, avatarImg);

        return partialValidate(userDAO, kv);
    }

    //enumerazione di errore
    public enum ErrorMessage {
        FIRST_NAME_MISSING,
        FIRST_NAME_TOO_LONG,
        LAST_NAME_MISSING,
        LAST_NAME_TOO_LONG,
        PASSWORD_MISSING,
        PASSWORD_TOO_LONG,
        PASSWORD_NOT_VALID,
        PASSWORDS_MISSING,
        PASSWORD2_MISSING,
        PASSWORD2_NOT_SAME,
        EMAIL_MISSING,
        EMAIL_TOO_LONG,
        EMAIL_NOT_VALID,
        EMAIL_ALREADY_USED,
        INF_PRIVACY_MISSING,
        AVATAR_MISSING,
        AVATAR_NOT_VALID,
        AVATAR_IMG_MISSING,
        AVATAR_IMG_ZERO_DIM,
        AVATAR_IMG_TOO_BIG,
        AVATAR_IMG_NOT_IMG,
    }
}
