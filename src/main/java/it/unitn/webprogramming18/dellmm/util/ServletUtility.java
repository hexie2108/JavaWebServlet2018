package it.unitn.webprogramming18.dellmm.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public interface ServletUtility {

    /**
     * metodo statico per mandare dati in formati JSON
     *
     * @param request
     * @param response
     * @param statusCode
     * @param json
     * @throws IOException
     */
    static void sendJSON(HttpServletRequest request, HttpServletResponse response, int statusCode, Object json) throws IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // Send json
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        out.print(gson.toJson(json));
        response.setStatus(statusCode);
    }

    /**
     * metodo statico per mandare alla pagina di errore
     *
     * @param request
     * @param response
     * @param statusCode
     * @param error
     * @throws IOException
     */
    static void sendError(HttpServletRequest request, HttpServletResponse response, int statusCode, String error) throws IOException {
        if (request.getRequestURI().endsWith(".json")) {
            HashMap<String, String> obj = new HashMap<>();
            ResourceBundle bundle = it.unitn.webprogramming18.dellmm.util.i18n.getBundle(request);
            obj.put("message", bundle.getString(error));

            sendJSON(request, response, statusCode, obj);
        } else {
            response.sendError(statusCode, "[" + error + "]");
        }
    }

    /**
     * metodo statico per mandare alla pagina di errore per errore di validazione
     *
     * @param request
     * @param response
     * @param statusCode
     * @param errMap
     * @throws IOException
     */
    static void sendValidationError(HttpServletRequest request, HttpServletResponse response, int statusCode, Map<String, String> errMap) throws IOException {
        if (request.getRequestURI().endsWith(".json")) {
            ResourceBundle bundle = it.unitn.webprogramming18.dellmm.util.i18n.getBundle(request);

            Map<String, String> obj = errMap.entrySet().stream().collect(Collectors.toMap(
                    (Map.Entry<String, String> e) -> e.getKey(),
                    (Map.Entry<String, String> e) -> bundle.getString(e.getValue())
            ));

            obj.put("message", "ValidationFail");

            sendJSON(request, response, statusCode, obj);
        } else {
            response.sendError(
                    statusCode,
                    "["
                            + errMap.entrySet()
                            .stream()
                            .map((Map.Entry<String, String> e) -> e.getValue())
                            .collect(Collectors.joining(","))
                            + "]");
        }
    }
}
