package it.unitn.webprogramming18.dellmm.util;

import javax.servlet.ServletException;

/**
 * metodo statico per check variaible e oggetto
 *
 * @author mikuc
 */
public class CheckErrorUtils {

    /**
     * controlla se l'oggetto è vuoto, se è vuoto lancia un servletException
     *
     * @param object       oggetto da controllare
     * @param errorMessage messaggio di errore da passare in caso di errore
     * @throws ServletException
     */
    public static void isNull(Object object, String errorMessage) throws ServletException {
        if (object == null) {
            throw new ServletException(errorMessage);
        }
    }

    /**
     * controlla se booleano è false, se è false lancia un servletException
     *
     * @param flag         valore booleano da controllare
     * @param errorMessage messaggio di errore da passare in caso di errore
     * @throws ServletException
     */
    public static void isFalse(boolean flag, String errorMessage) throws ServletException {
        if (!flag) {
            throw new ServletException(errorMessage);
        }
    }

}
