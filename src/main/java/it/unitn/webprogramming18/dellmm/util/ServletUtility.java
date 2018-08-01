package it.unitn.webprogramming18.dellmm.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ResourceBundle;

public interface ServletUtility {
    static void sendJSON(HttpServletRequest request, HttpServletResponse response, int statusCode, java.io.Serializable json) throws IOException {
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

    static void sendError(HttpServletRequest request, HttpServletResponse response, int statusCode, String error) throws IOException {
        if(request.getRequestURI().endsWith(".json")) { // Richiesta sincrona
            HashMap<String, String> obj = new HashMap<>();
            ResourceBundle bundle = it.unitn.webprogramming18.dellmm.util.i18n.getBundle(request);
            obj.put("message", bundle.getString(error));

            sendJSON(request,response, statusCode, obj);
        } else { // Richiesta asincrona
            response.sendError(statusCode, error);
        }
    }


}
