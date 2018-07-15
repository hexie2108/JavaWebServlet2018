package it.unitn.webprogramming18.dellmm.util;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;

import javax.mail.internet.InternetAddress;
import java.util.HashMap;

public class RegistrationValidator {
    // --- Configurazioni per la validazione dei campi
    private static final int FIRST_NAME_MAX_LEN = 44,
                             LAST_NAME_MAX_LEN  = 44,
                             PWD_MAX_LEN        = 44,
                             EMAIL_MAX_LEN      = 44;

    private static final int PWD_MIN_LEN        =  8,
                             PWD_MIN_UPPER      =  1,
                             PWD_MIN_LOWER      =  1,
                             PWD_MIN_NUMBER     =  1,
                             PWD_MIN_SYMBOL     =  1;

    public static final String FIRST_NAME_KEY   = "FirstName",
                               LAST_NAME_KEY    = "LastName",
                               INF_PRIVACY_KEY  = "InfPrivacy",
                               FIRST_PWD_KEY    = "Password",
                               SECOND_PWD_KEY   = "Password2",
                               EMAIL_KEY        = "Email";

    // --- Funzioni di validazione

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

    public static String validateFirstName(String firstName) {
        if ((firstName == null) ||
            (firstName.isEmpty())) {
            return "Complete il campo first name";
        }

        if (firstName.length() > FIRST_NAME_MAX_LEN) {
            return "Nome troppo lungo";
        }

        return null;
    }

    public static String validateLastName(String lastName) {
        if ((lastName == null) ||
            (lastName.isEmpty())) {
            return "Completa il campo last name";
        }

        if (lastName.length() > LAST_NAME_MAX_LEN) {
            return "Nome troppo lungo";
        }

        return null;
    }

    public static String validatePassword(String password) {
        if ((password == null) ||
                (password.isEmpty())) {
            return "Completa questo campo";
        }


        if (password.length() > PWD_MAX_LEN) {
            return "Password troppo lunga";
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
            (upper  < PWD_MIN_UPPER)  ||
            (lower  < PWD_MIN_LOWER)  ||
            (number < PWD_MIN_NUMBER) ||
            (symbol < PWD_MIN_SYMBOL)) {
            return "La password deve essere:<br>" +
                   "  1. Lunga almeno 8 caratteri<br>" +
                   "  2. Avere almeno una lettera minuscola<br>" +
                   "  3. Avere almeno una lettera maiuscola<br>" +
                   "  4. Avere almeno un numero<br>" +
                   "  5. Avere almeno un simbolo<br>";
        }

        return null;
    }

    public static String validateSecondPassword(String firstPassword, String secondPassword) {
        if ((firstPassword == null) ||
            (secondPassword == null) ||
            (firstPassword.isEmpty()) ||
            (secondPassword.isEmpty())) {
            return "Completa tutti e due i campi password";
        }

        if (firstPassword.length() > PWD_MAX_LEN) {
            return "Password troppo lunga";
        }

        if (!firstPassword.equals(secondPassword)) { // Controlla che le due password coincidano
            return "Le due password non corrispondono";
        }

        return null;
    }

    public static String validateEmail(String email, UserDAO userDAO) {
        if ((email == null) ||
            (email.isEmpty())) {
            return "Completa il campo email";
        }

        if (email.length() > EMAIL_MAX_LEN) {
            return "Indirizzo email troppo lungo";
        }

        if (!validateEmailFormat(email)) { // Controllo che la mail abbia un formato valido
            return "Indirizzo email non valido";
        } else {
            try {
                if (userDAO.checkUserRegisteredByEmail(email) != 0) {
                    return "Indirizzo email già usato";
                }
            } catch (DAOException ignored) {
                // If the check doesn't work postpone to real registration
            }
        }

        return null;
    }

    public static String validateInfPrivacy(String infPrivacy) {
        if (infPrivacy == null) { // Controlla che sia stata accettata l'informativa alla privacy
            return "Devi accettare l'informativa alla privacy";
        }

        return null;
    }

    public static HashMap<String, String> createValidationMessages(
            UserDAO userDAO,
            String firstName,
            String lastName,
            String email,
            String firstPassword,
            String secondPassword,
            String infPrivacy) {
        HashMap<String, String> messages = new HashMap<>();

        String messageFirstName = validateFirstName(firstName);
        if (messageFirstName != null) {
            messages.put(FIRST_NAME_KEY, messageFirstName);
        }

        String messageLastName = validateLastName(lastName);
        if (messageLastName != null) {
            messages.put(LAST_NAME_KEY, messageLastName);
        }

        String messageInfPrivacy = validateInfPrivacy(infPrivacy);
        if (messageInfPrivacy != null) {
            messages.put(INF_PRIVACY_KEY, messageInfPrivacy);
        }

        String messagePwd = validatePassword(firstPassword);
        if (messagePwd != null) {
            messages.put(FIRST_PWD_KEY, messagePwd);
        }

        String messageSecondPassword = validateSecondPassword(firstPassword, secondPassword);
        if (messageSecondPassword != null) {
            messages.put(SECOND_PWD_KEY, messageSecondPassword);
        }

        String messageValidateEmail = validateEmail(email, userDAO);
        if (messageValidateEmail != null) {
            messages.put(EMAIL_KEY, messageValidateEmail);
        }

        return messages;
    }
}
