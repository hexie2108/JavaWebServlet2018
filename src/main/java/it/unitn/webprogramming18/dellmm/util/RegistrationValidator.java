package it.unitn.webprogramming18.dellmm.util;

import it.unitn.webprogramming18.dellmm.db.daos.UserDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;

import javax.mail.internet.InternetAddress;
import java.util.HashMap;

public class RegistrationValidator {
    private static boolean validateMail(String email) {
        boolean ris = false;
        try {
            InternetAddress addr = new InternetAddress(email);
            addr.validate();
            ris = true;
        } catch (Exception ignored) {
        }

        return ris;
    }

    public static String validatePassword(String password) {
        if ((password == null) ||
                (password.isEmpty())) {
            return "Completa questo campo";
        } else if (password.length() > 254) {
            return "Password troppo lunga";
        } else {
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

            if ((password.length() < 8) ||
                    (upper < 1) ||
                    (lower < 1) ||
                    (number < 1) ||
                    (symbol < 1)) {
                return "La password deve essere:<br>" +
                       "  1. Lunga almeno 8 caratteri<br>" +
                       "  2. Avere almeno una lettera minuscola<br>" +
                       "  3. Avere almeno una lettera maiuscola<br>" +
                       "  4. Avere almeno un numero<br>" +
                       "  5. Avere almeno un simbolo<br>";
            }
        }

        return null;
    }


    public static HashMap<String, String> createValidationMessages(
            UserDAO userDAO,
            String first_name,
            String last_name,
            String email,
            String password,
            String password2,
            String infPrivacy) {
        HashMap<String, String> messages = new HashMap<>();

        if ((first_name == null) ||
                (first_name.isEmpty())) {
            messages.put("FirstName", "Complete il campo first name");
        } else if (first_name.length() > 44) {
            messages.put("FirstName", "Nome troppo lungo");
        }

        if ((last_name == null) ||
                (last_name.isEmpty())) {
            messages.put("LastName", "Completa il campo last name");
        } else if (last_name.length() > 44) {
            messages.put("LastName", "Nome troppo lungo");
        }

        if (infPrivacy == null) { // Controlla che sia stata accettata l'informativa alla privacy
            messages.put("InfPrivacy", "Devi accettare l'informativa alla privacy");
        }

        String messagePwd = validatePassword(password);
        if (messagePwd != null) {
            messages.put("Password", messagePwd);
        }

        if ((password == null) ||
                (password2 == null) ||
                (password.isEmpty()) ||
                (password2.isEmpty())) {
            messages.put("Password2", "Completa tutti e due i campi password");
        } else if (password.length() > 44) {
            messages.put("Password2", "Password troppo lunga");
        } else if (!password.equals(password2)) { // Controlla che le due password coincidano
            messages.put("Password2", "Le due password non corrispondono");
        }

        if ((email == null) ||
                (email.isEmpty())) {
            messages.put("Email", "Completa il campo email");
        } else if (!validateMail(email)) { // Controllo che la mail abbia un formato valido
            messages.put("Email", "Indirizzo email non valido");
        } else if (email.length() > 44) {
            messages.put("Email", "Indirizzo email troppo lungo");
        } else {
            try {
                if (userDAO.checkUserRegisteredByEmail(email) != 0) {
                    messages.put("Email", "Indirizzo email già usato");
                }
            } catch (DAOException ignored) {
                // If the check doesn't work postpone to real registration
            }
        }

        return messages;
    }
}
