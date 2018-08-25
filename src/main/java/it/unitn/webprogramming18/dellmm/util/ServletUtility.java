package it.unitn.webprogramming18.dellmm.util;

import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
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
    static String insertImage(
            HttpServletRequest request,
            HttpServletResponse response,
            Path path,
            String prevImg,
            InputStream inputStream,
            int width,
            int height) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageUtils.convertImg2(inputStream, os, width, height);

        String newImgName = UUID.randomUUID().toString() + ".jpg";

        try {
            File file = new File(path.toString(), newImgName);
            Files.copy(new ByteArrayInputStream(os.toByteArray()), file.toPath());
        } catch (FileAlreadyExistsException ex) { // Molta sfiga
            ServletUtility.sendError(request, response, 500, "generic.errors.fileCollision");
            request.getServletContext().log("File \"" + newImgName + "\" already exists on the server");
            return null;
        } catch (RuntimeException ex) {
            ServletUtility.sendError(request, response, 500, "generic.errors.unuploudableFile");
            request.getServletContext().log("impossible to upload the file", ex);
            return null;
        }

        if (prevImg != null) {
            ServletUtility.deleteFile(path, prevImg, request.getServletContext());
        }

        return newImgName;
    }

}
