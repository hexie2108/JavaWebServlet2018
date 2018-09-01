package it.unitn.webprogramming18.dellmm.servlets.adminSystem;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.CategoryList;
import it.unitn.webprogramming18.dellmm.util.CategoryListValidator;
import it.unitn.webprogramming18.dellmm.util.ConstantsUtils;
import it.unitn.webprogramming18.dellmm.util.DatatablesUtils;
import it.unitn.webprogramming18.dellmm.util.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "JSONCategoryListsServlet")
@MultipartConfig
public class JSONCategoryListsServlet extends HttpServlet {
    private CategoryListDAO categoryListDAO = null;

    private String subImg(HttpServletRequest request, HttpServletResponse response, Path path, String prevImg, InputStream fileContent) throws IOException, ServletException {
        return ServletUtility.insertImage(
                request,
                path,
                prevImg,
                fileContent,
                ConstantsUtils.IMAGE_OF_CATEGORY_LIST_WIDTH,
                ConstantsUtils.IMAGE_OF_CATEGORY_LIST_HEIGHT
        );
    }

    private void modifyCategoryList(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        Path path = ServletUtility.getFolder(getServletContext(), "categoryListImgsFolder");

        Integer id;

        try {
            String sId = request.getParameter("id");
            id = sId == null ? null : Integer.parseInt(sId);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "categoryLists.errors.idNotInt");
            return;
        }

        String name = request.getParameter(CategoryListValidator.NAME_KEY);
        String description = request.getParameter(CategoryListValidator.DESCRIPTION_KEY);

        String deleteImg2 = request.getParameter("deleteImg2");
        String deleteImg3 = request.getParameter("deleteImg3");

        Part img1 = request.getPart(CategoryListValidator.IMG1_KEY);
        Part img2 = request.getPart(CategoryListValidator.IMG2_KEY);
        Part img3 = request.getPart(CategoryListValidator.IMG3_KEY);

        HashMap<String, Object> kv = new HashMap<>();

        if (name != null && !name.trim().isEmpty()) {
            kv.put(CategoryListValidator.NAME_KEY, name);
        } else {
            name = "";
        }

        if (description != null && !description.trim().isEmpty()) {
            kv.put(CategoryListValidator.DESCRIPTION_KEY, description);
        } else {
            description = "";
        }

        if (img1 != null && img1.getSize() != 0) {
            kv.put(CategoryListValidator.IMG1_KEY, img1);
        }

        if (!deleteImg2.equalsIgnoreCase("delete") && img2 != null && img2.getSize() != 0) {
            kv.put(CategoryListValidator.IMG2_KEY, img2);
        }

        if (!deleteImg3.equalsIgnoreCase("delete") && img3 != null && img3.getSize() != 0) {
            kv.put(CategoryListValidator.IMG3_KEY, img3);
        }


        // Usa il validator per verifiacare la conformità
        Map<String, String> messages =
                CategoryListValidator.partialValidate(kv)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                (Map.Entry<String, CategoryListValidator.ErrorMessage> e) -> CategoryListValidator.I18N_ERROR_STRING_PREFIX + e.getValue().toString()
                                )
                        );

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(request, response, 400, messages);
            return;
        }

        CategoryList categoryList;
        try {
            categoryList = categoryListDAO.getByPrimaryKey(id);
        } catch (DAOException e) {
            ServletUtility.sendError(request, response, 400, "categoryLists.errors.impossibleSearchDb");
            return;
        }

        if (!name.isEmpty()) {
            categoryList.setName(name);
        }

        if (!description.isEmpty()) {
            categoryList.setDescription(description);
        }

        if (img1 != null && img1.getSize() != 0) {
            String r = subImg(request, response, path, categoryList.getImg1(), img1.getInputStream());
            categoryList.setImg1(r);
        }

        if (deleteImg2.equalsIgnoreCase("delete")) {
            ServletUtility.deleteFile(path, categoryList.getImg2(), getServletContext());
            categoryList.setImg2(null);
        } else if (img2 != null && img2.getSize() != 0) {
            String r = subImg(request, response, path, categoryList.getImg2(), img2.getInputStream());
            categoryList.setImg2(r);
        }

        if (deleteImg3.equalsIgnoreCase("delete")) {
            ServletUtility.deleteFile(path, categoryList.getImg3(), getServletContext());
            categoryList.setImg3(null);
        } else if (img3 != null && img3.getSize() != 0) {
            String r = subImg(request, response, path, categoryList.getImg3(), img3.getInputStream());
            categoryList.setImg3(r);
        }

        try {
            categoryListDAO.update(categoryList);
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "categoryList.errors.unupdatable");
            return;
        }

        ServletUtility.sendJSON(request, response, 200, new HashMap<>());
    }

    private void deleteCategoryList(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Path path = ServletUtility.getFolder(getServletContext(), "categoryListImgsFolder");

        Integer id;
        try {
            String sId = request.getParameter("id");
            id = sId == null ? null : Integer.parseInt(sId);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "categoryLists.errors.idNotInt");
            return;
        }

        try {
            // Get category list to later delete its images
            CategoryList categoryList = categoryListDAO.getByPrimaryKey(id);

            // Delete category list
            categoryListDAO.delete(id);

            ServletUtility.deleteFile(path, categoryList.getImg1(), getServletContext());
            ServletUtility.deleteFile(path, categoryList.getImg2(), getServletContext());
            ServletUtility.deleteFile(path, categoryList.getImg3(), getServletContext());
        } catch (DAOException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                ServletUtility.sendError(request, response, 500, "categoryList.errors.otherListDepend");
                return;
            }

            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "categoryList.errors.undeletable");
            return;
        }

        ServletUtility.sendJSON(request, response, 200, new HashMap<>());
    }

    private void createCategoryList(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Path path = ServletUtility.getFolder(getServletContext(), "categoryListImgsFolder");

        String name = request.getParameter(CategoryListValidator.NAME_KEY);
        String description = request.getParameter(CategoryListValidator.DESCRIPTION_KEY);

        Part img1 = request.getPart(CategoryListValidator.IMG1_KEY);
        Part img2 = request.getPart(CategoryListValidator.IMG2_KEY);
        Part img3 = request.getPart(CategoryListValidator.IMG3_KEY);


        Map<String, String> messages =
                CategoryListValidator.completeValidate(name, description, img1, img2, img3)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                (Map.Entry<String, CategoryListValidator.ErrorMessage> e) -> CategoryListValidator.I18N_ERROR_STRING_PREFIX + e.getValue().toString()
                                )
                        );

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(request, response, 400, messages);
            return;
        }

        String sImg1 = subImg(request, response, path, null, img1.getInputStream());
        if (sImg1 == null) {
            return;
        }

        String sImg2 = null;
        if (img2 != null && img2.getSize() != 0) {
            sImg2 = subImg(request, response, path, null, img2.getInputStream());
            if (sImg2 == null) {
                return;
            }
        }

        String sImg3 = null;
        if (img3 != null && img3.getSize() != 0) {
            sImg3 = subImg(request, response, path, null, img3.getInputStream());
            if (sImg3 == null) {
                return;
            }
        }

        CategoryList categoryList = new CategoryList();
        categoryList.setName(name);
        categoryList.setDescription(description);
        categoryList.setImg1(sImg1);
        categoryList.setImg2(sImg2);
        categoryList.setImg3(sImg3);

        try {
            categoryListDAO.insert(categoryList);
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "categoryLists.errors.impossibleNew");
            return;
        }

        ServletUtility.sendJSON(request, response, 200, new HashMap<>());
    }


    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            categoryListDAO = daoFactory.getDAO(CategoryListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get CategoryListDAO for user storage system", ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            ServletUtility.sendError(request, response, 400, "categoryLists.errors.missingAction");
            return;
        }

        if (action.equalsIgnoreCase("modify")) {
            modifyCategoryList(request, response);
            return;
        }

        if (action.equalsIgnoreCase("delete")) {
            deleteCategoryList(request, response);
            return;
        }

        if (action.equalsIgnoreCase("create")) {
            createCategoryList(request, response);
            return;
        }

        ServletUtility.sendError(request, response, 400, "categoryLists.errors.unrecognizedAction");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters for ordering(what column)
        CategoryListDAO.OrderableColumns column;

        { // Used to limit orderBy scope
            String columnName = DatatablesUtils.getColumnName(request, response);
            if(columnName == null) {
                return;
            }

            // Get column and save as enum, if not valid send error
            switch (columnName) {
                case "id":
                    column = CategoryListDAO.OrderableColumns.ID;
                    break;
                case "name":
                    column = CategoryListDAO.OrderableColumns.NAME;
                    break;
                case "description":
                    column = CategoryListDAO.OrderableColumns.DESCRIPTION;
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

        // Get parameters for filtering
        Integer iId;
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        { // Used to limit id scope
            // Get id, cast it to int, if error during cast send error
            String id = request.getParameter("id");
            try {
                iId = id == null || id.trim().isEmpty() ? null : Integer.parseInt(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ServletUtility.sendError(request, response, 400, "categoryLists.errors.idNotInt");
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
            List<CategoryList> categoryLists = categoryListDAO.filter(iId, name, description, column,dir ,iOffset ,iLength);

            Long totalCount = categoryListDAO.getCount();
            Long filteredCount = categoryListDAO.getCountFilter(iId, name, description);

            HashMap<String, Object> h = new HashMap<>();
            h.put("recordsTotal", totalCount);
            h.put("recordsFiltered", filteredCount);
            h.put("data", categoryLists);

            ServletUtility.sendJSON(
                    request,
                    response,
                    200,
                    h
            );
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "categoryLists.errors.impossibleDbFilter");
        }
    }
}
