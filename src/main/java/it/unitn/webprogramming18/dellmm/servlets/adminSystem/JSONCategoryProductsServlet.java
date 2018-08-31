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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "JSONCategoryProductsServlet")
@MultipartConfig
public class JSONCategoryProductsServlet extends HttpServlet {
    private CategoryProductDAO categoryProductDAO = null;

    private String subImg(HttpServletRequest request, HttpServletResponse response, Path path, String prevImg, InputStream inputStream) throws IOException, ServletException {
        return ServletUtility.insertImage(
                request,
                path,
                prevImg,
                inputStream,
                ConstantsUtils.IMAGE_OF_CATEGORY_PRODUCT_WIDTH,
                ConstantsUtils.IMAGE_OF_CATEGORY_PRODUCT_HEIGHT
        );
    }

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters for ordering(what column)
        CategoryProductDAO.OrderableColumns column;

        { // Used to limit orderBy scope
            String columnName = DatatablesUtils.getColumnName(request, response);
            if(columnName == null) {
                return;
            }

            // Get column and save as enum, if not valid send error
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
        }

        // get ordering direction
        Boolean dir = DatatablesUtils.getDirection(request, response);
        if(dir == null) {
            return;
        }

        // get parameters for pagination
        Integer iOffset = DatatablesUtils.getOffset(request, response);
        if (iOffset == null) {
            return;
        }

        Integer iLength = DatatablesUtils.getLength(request, response);
        if (iLength == null) {
            return;
        }

        Integer iId;
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        {
            String id = request.getParameter("id");
            try {
                iId = id == null || id.trim().isEmpty() ? null : Integer.parseInt(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ServletUtility.sendError(request, response, 400, "categoryProducts.errors.idNotInt");
                return;
            }
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

            if (sId == null) {
                ServletUtility.sendError(request, response, 400, "categoryProducts.errors.idNotInt");
                return;
            }

            id = Integer.parseInt(sId);
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
            String r = subImg(request, response, path, categoryProduct.getImg(), img.getInputStream());
            categoryProduct.setImg(r);
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

    private void deleteCategoryProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Path path = ServletUtility.getFolder(getServletContext(), "categoryProductImgsFolder");

        Integer id;
        try {
            String sId = request.getParameter("id");
            id = sId == null ? null : Integer.parseInt(sId);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "categoryProducts.errors.idNotInt");
            return;
        }

        try {
            // Get category product to delete its images
            CategoryProduct categoryProduct = categoryProductDAO.getByPrimaryKey(id);

            // Delete the category product
            categoryProductDAO.delete(id);

            // Delete the images
            Files.delete(Paths.get(path.toString(), categoryProduct.getImg()));
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

        String r = subImg(request, response, path, null, img.getInputStream());

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
