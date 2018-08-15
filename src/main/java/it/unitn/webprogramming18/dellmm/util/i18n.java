package it.unitn.webprogramming18.dellmm.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class i18n {
    public static final HashMap<String, String> SUPPORTED_LANGUAGES = new HashMap() {{
        put("en", "English");
        put("it", "Italiano");
    }};
    private static final String BUNDLE_NAME = "text";

    public static Locale getLocale(HttpServletRequest request) {
        String language = request.getParameter("language");
        HttpSession session = request.getSession(true);

        Locale locale;

        if (language != null) {
            locale = Locale.forLanguageTag(language);
        } else if (session.getAttribute("language") != null) {
            language = (String) session.getAttribute("language");
            locale = Locale.forLanguageTag(language);
        } else {
            locale = request.getLocale();
        }

        if (SUPPORTED_LANGUAGES.get(locale.getLanguage()) == null) {
            // Se il locale non è tra quelli supportati
            locale = Locale.forLanguageTag("en");
        }

        session.setAttribute("language", locale.getLanguage());

        return locale;
    }

    public static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("text", locale);
    }

    public static ResourceBundle getBundle(HttpServletRequest request) {
        return getBundle(getLocale(request));
    }
}
