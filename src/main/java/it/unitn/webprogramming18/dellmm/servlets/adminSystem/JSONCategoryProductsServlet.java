package it.unitn.webprogramming18.dellmm.servlets.adminSystem;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryProduct;
import it.unitn.webprogramming18.dellmm.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet(name = "JSONCategoryProductsServlet")
@MultipartConfig
public class JSONCategoryProductsServlet extends HttpServlet {
    private CategoryProductDAO categoryProductDAO = null;

    private static final int
        IMG_WIDTH = 1000,
        IMG_HEIGHT = 1000;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            categoryProductDAO = daoFactory.getDAO(CategoryProductDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get UserDAO for user storage system", ex);
        }
    }

    private String subImg(HttpServletRequest request, HttpServletResponse response, Path path, String prevImg, InputStream inputStream) throws IOException {
        String newImgName = UUID.randomUUID().toString() + ".jpg";

        try {
            File file = new File(path.toString(), newImgName.toString());
            Files.copy(inputStream, file.toPath());
        } catch (FileAlreadyExistsException ex) { // Molta sfiga
            ServletUtility.sendError(request, response, 500, "generic.errors.fileCollision");
            getServletContext().log("File \"" + newImgName.toString() + "\" already exists on the server");
            return null;
        } catch (RuntimeException ex) {
            ServletUtility.sendError(request, response, 500, "generic.errors.unuploudableFile");
            getServletContext().log("impossible to upload the file", ex);
            return null;
        }

        if (prevImg != null) {
            ServletUtility.deleteFile(path, prevImg, getServletContext());
        }

        return newImgName;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderBy = request.getParameter("order[0][column]");
        if (orderBy == null || orderBy.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.orderByMissing");
            return;
        }

        int columnId;

        try {
            columnId = Integer.parseInt(orderBy);
        } catch (NumberFormatException e){
            ServletUtility.sendError(request, response, 400, "datatables.errors.orderByNotInt");
            return;
        }


        String columnName = request.getParameter("columns[" + columnId + "][name]");
        if (columnName == null || columnName.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.columnNameMissing");
            return;
        }

        CategoryProductDAO.OrderableColumns column;
        switch (columnName) {
            case "id":
                column = CategoryProductDAO.OrderableColumns.ID;
                break;
            case "name":
                column = CategoryProductDAO.OrderableColumns.NAME;
                break;
            case "description":
                column = CategoryProductDAO.OrderableColumns.DESCRIPTION;
                break;
            default:
                ServletUtility.sendError(request, response, 400, "datatables.errors.columnNameUnrecognized");
                return;
        }

        String direction = request.getParameter("order[0][dir]");
        if (direction == null || direction.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.dirMissing");
            return;
        }

        boolean dir;
        switch (direction) {
            case "asc": dir = true; break;
            case "desc": dir = false; break;
            default:
                ServletUtility.sendError(request, response, 400, "datatables.errors.dirUnrecognized");
                return;
        }

        String offset = request.getParameter("start");
        if(offset == null || offset.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.offsetMissing");
            return;
        }


        int iOffset;
        try{
            iOffset = Integer.parseInt(offset);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.offsetNotInt");
            return;
        }

        String length = request.getParameter("length");
        if(length == null || length.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.lengthMissing");
            return;
        }

        int iLength;
        try{
            iLength = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.lengthNotInt");
            return;
        }


        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        Integer iId;
        try {
            iId = id == null || id.trim().isEmpty() ? null : Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 400, "categoryProducts.errors.idNotInt");
            return;
        }

        if (name != null && name.trim().isEmpty()) {
            name = null;
        }

        if (description != null && description.trim().isEmpty()) {
            description = null;
        }

        try {
            List<CategoryProduct> categoryProducts = categoryProductDAO.filter(iId, name, description, column, dir, iOffset, iLength);
            Long totalCount = categoryProductDAO.getCount();
            Long filteredCount = categoryProductDAO.getCountFilter(iId, name, description);

            HashMap<String, Object> h = new HashMap<>();
            h.put("recordsTotal", totalCount);
            h.put("recordsFiltered", filteredCount);
            h.put("data", categoryProducts);

            ServletUtility.sendJSON(
                    request,
                    response,
                    200,
                    h
            );
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "categoryProducts.errors.impossibleDbFilter");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            ServletUtility.sendError(request, response, 400, "categoryProducts.errors.missingAction");
            return;
        }

        if (action.equalsIgnoreCase("modify")) {
            modifyCategoryProduct(request, response);
            return;
        }

        if (action.equalsIgnoreCase("delete")) {
            deleteCategoryProduct(request, response);
            return;
        }

        if (action.equalsIgnoreCase("create")) {
            createCategoryProduct(request, response);
            return;
        }

        ServletUtility.sendError(request, response, 400, "categoryProducts.errors.unrecognizedAction");
    }

    private void modifyCategoryProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Path path = ServletUtility.getFolder(getServletContext(), "categoryProductImgsFolder");

        Integer id;

        try {
            String sId = request.getParameter("id");
            id = sId == null ? null : Integer.parseInt(sId);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "categoryProducts.errors.idNotInt");
            return;
        }

        String name = request.getParameter(CategoryProductValidator.NAME_KEY);
        String description = request.getParameter(CategoryProductValidator.DESCRIPTION_KEY);

        Part img = request.getPart(CategoryProductValidator.IMG_KEY);

        HashMap<String, Object> kv = new HashMap<>();

        if (name != null && !name.trim().isEmpty()) {
            kv.put(CategoryProductValidator.NAME_KEY, name);

        }

        if (description != null && !description.trim().isEmpty()) {
            kv.put(CategoryProductValidator.DESCRIPTION_KEY, description);
        }

        if (img != null && img.getSize() != 0) {
            kv.put(CategoryProductValidator.IMG_KEY, img);
        }

        // Usa il validator per verifiacare la conformità
        Map<String, String> messages =
                CategoryProductValidator.partialValidate(kv)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                (Map.Entry<String, ValidatorUtils.ErrorType> e) -> e.getKey(),
                                (Map.Entry<String, ValidatorUtils.ErrorType> e) -> CategoryProductValidator.I18N_ERROR_STRING_PREFIX + '.' + e.getKey() + '.' + e.getValue().toString()
                                )
                        );

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(request, response, 400, messages);
            return;
        }

        CategoryProduct categoryProduct;
        try {
            categoryProduct = categoryProductDAO.getByPrimaryKey(id);
        } catch (DAOException e) {
            ServletUtility.sendError(request, response, 400, "categoryProducts.errors.impossibleSearchDb");
            return;
        }

        if (name != null && !name.trim().isEmpty()) {
            categoryProduct.setName(name);
        }

        if (description != null && !description.trim().isEmpty()) {
            categoryProduct.setDescription(description);
        }

        if (img != null && img.getSize() != 0) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageUtils.convertImg2(img.getInputStream(), os, IMG_WIDTH, IMG_HEIGHT);

            String r = subImg(request, response, path, categoryProduct.getImg(), new ByteArrayInputStream(os.toByteArray()));
            if (r == null) {
                return;
            } else {
                categoryProduct.setImg(r);
            }
        }


        try {
            categoryProductDAO.update(categoryProduct);
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "categoryProducts.errors.unupdatable");
            return;
        }

        ServletUtility.sendJSON(request, response, 200, new HashMap<>());
    }

    private void deleteCategoryProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id;

        try {
            String sId = request.getParameter("id");
            id = sId == null ? null : Integer.parseInt(sId);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "categoryProducts.errors.idNotInt");
            return;
        }

        try {
            categoryProductDAO.delete(id);
        } catch (DAOException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                ServletUtility.sendError(request, response, 500, "categoryProducts.errors.otherProductDepend");
                return;
            }

            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "categoryProducts.errors.undeletable");
            return;
        }

        ServletUtility.sendJSON(request, response, 200, new HashMap<>());
    }

    private void createCategoryProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Path path = ServletUtility.getFolder(getServletContext(), "categoryProductImgsFolder");

        String name = request.getParameter(CategoryProductValidator.NAME_KEY);
        String description = request.getParameter(CategoryProductValidator.DESCRIPTION_KEY);

        Part img = request.getPart(CategoryProductValidator.IMG_KEY);

        // Usa il validator per verifiacare la conformità
        Map<String, String> messages =
                CategoryProductValidator.completeValidate(name, description, img)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                (Map.Entry<String, ValidatorUtils.ErrorType> e) -> e.getKey(),
                                (Map.Entry<String, ValidatorUtils.ErrorType> e) -> CategoryProductValidator.I18N_ERROR_STRING_PREFIX + '.' + e.getKey() + '.' + e.getValue().toString()
                                )
                        );

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(request, response, 400, messages);
            return;
        }


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageUtils.convertImg2(img.getInputStream(), os, IMG_WIDTH, IMG_HEIGHT);

        String r = subImg(request, response, path, null, new ByteArrayInputStream(os.toByteArray()));
        if (r == null) {
            return;
        }

        CategoryProduct categoryProduct = new CategoryProduct();

        categoryProduct.setName(name);
        categoryProduct.setDescription(description);
        categoryProduct.setImg(r);


        try {
            categoryProductDAO.insert(categoryProduct);
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "categoryProducts.errors.uncreatable");
            return;
        }

        ServletUtility.sendJSON(request, response, 200, new HashMap<>());
    }

}
