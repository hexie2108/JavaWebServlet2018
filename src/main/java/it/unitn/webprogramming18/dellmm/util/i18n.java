package it.unitn.webprogramming18.dellmm.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.Cookie;

public class i18n
{

        public static final HashMap<String, String> SUPPORTED_LANGUAGES = new HashMap()
        {
                {
                        put("en", "English");
                        put("it", "Italiano");
                }
        };
        private static final String BUNDLE_NAME = "text";

        public static Locale getLocale(HttpServletRequest request)
        {
                String language=null;

                //get la cookie dell'utente
                Cookie[] cookies = request.getCookies();
                //se utente ha la cookie di auto login key
                if (cookies != null && cookies.length > 0)
                {
                        for (Cookie cookie : cookies)
                        {
                                if (cookie.getName().equals("language"))
                                {
                                        language = cookie.getValue();
                                }
                        }
                }

                Locale locale;
                //se cookie language non è vuoto
                if (language != null)
                {
                        locale = Locale.forLanguageTag(language);
                }
                //altrimento si prende quella di browser
                else
                {
                        locale = request.getLocale();
                }
                //se la lingua ottenuta non è supportato, usa inglese come defualt
                if (SUPPORTED_LANGUAGES.get(locale.getLanguage()) == null)
                {
                        // Se il locale non è tra quelli supportati
                        locale = Locale.forLanguageTag("en");
                }

                return locale;
        }

        public static ResourceBundle getBundle(Locale locale)
        {
                return ResourceBundle.getBundle("text", locale);
        }

        public static ResourceBundle getBundle(HttpServletRequest request)
        {
                return getBundle(getLocale(request));
        }
}
