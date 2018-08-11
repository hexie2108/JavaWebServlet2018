package it.unitn.webprogramming18.dellmm.util;

import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public interface ServletUtility {
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
                    "[" +
                            errMap.entrySet()
                                    .stream()
                                    .map((Map.Entry<String, String> e) -> e.getValue())
                                    .collect(Collectors.joining(",")) +
                            "]");
        }
    }

    static Path getFolder(ServletContext ctx, String parameterName) throws ServletException, IOException {
        // Ottieni configurazione cartella avatars
        String categoryListImgFolder = ctx.getInitParameter(parameterName);
        if (categoryListImgFolder == null) {
            throw new ServletException(parameterName + " folder not configured");
        }

        String realContextPath = ctx.getRealPath(File.separator);
        if (!realContextPath.endsWith("/")) {
            realContextPath += "/";
        }

        Path path = Paths.get(realContextPath + categoryListImgFolder);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        return path;
    }

    static void deleteFile(Path path, String toDeleteImg, ServletContext ctx) {
        Path toDelete = Paths.get(path.toString(), toDeleteImg);
        try {
            Files.delete(toDelete);
        } catch (IOException e) {
            // If we can't delete the old image we just log and continue
            ctx.log("File " + toDelete.toString() + " cannot be delete");
        }
    }
}
