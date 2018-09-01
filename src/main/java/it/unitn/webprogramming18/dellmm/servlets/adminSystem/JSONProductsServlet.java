package it.unitn.webprogramming18.dellmm.servlets.adminSystem;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ProductDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOFactoryException;
import it.unitn.webprogramming18.dellmm.db.utils.factories.DAOFactory;
import it.unitn.webprogramming18.dellmm.javaBeans.Product;
import it.unitn.webprogramming18.dellmm.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "JSONProductsServlet")
@MultipartConfig
public class JSONProductsServlet extends HttpServlet {
    private ProductDAO productDAO = null;
    private ListDAO listDAO = null;
    private CategoryProductDAO categoryProductDAO = null;

    private String subImg(HttpServletRequest request, HttpServletResponse response, Path path, String prevImg, InputStream inputStream) throws IOException, ServletException {
        return ServletUtility.insertImage(
                request,
                path,
                prevImg,
                inputStream,
                ConstantsUtils.IMAGE_OF_PRODUCT_WIDTH,
                ConstantsUtils.IMAGE_OF_PRODUCT_HEIGHT
        );
    }

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get db factory for user storage system");
        }

        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            listDAO = daoFactory.getDAO(ListDAO.class);
            categoryProductDAO = daoFactory.getDAO(CategoryProductDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get UserDAO for user storage system", ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters for ordering(what column)
        ProductDAO.OrderableColumns column;

        { // Used to limit orderBy scope
            String columnName = DatatablesUtils.getColumnName(request, response);
            if(columnName == null) {
                return;
            }

            // Get column and save as enum, if not valid send error
            switch (columnName) {
                case "id":
                    column = ProductDAO.OrderableColumns.ID;
                    break;
                case "name":
                    column = ProductDAO.OrderableColumns.NAME;
                    break;
                case "description":
                    column = ProductDAO.OrderableColumns.DESCRIPTION;
                    break;
                case "privateListId":
                    column = ProductDAO.OrderableColumns.PRIVATE_LIST_ID;
                    break;
                case "categoryName":
                    column = ProductDAO.OrderableColumns.CATEGORY_NAME;
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

        String[] catId = request.getParameterValues("catId");
        List<Integer> categories;
        if(catId == null) {
            categories = new ArrayList<>();
        } else {
            try{
                categories = Arrays.stream(catId).map(Integer::parseInt).collect(Collectors.toList());
            } catch(NumberFormatException e){
                ServletUtility.sendError(request, response, 400, "products.errors.catIdNotInt");
                return;
            }
        }

        Boolean publicOnly = request.getParameter("publicOnly") != null;

        Integer privateListId;
        {
            String sPrivateListId = request.getParameter("privateListId");
            try{
                privateListId = sPrivateListId == null || sPrivateListId.trim().isEmpty()? null: Integer.parseInt(sPrivateListId);
            } catch (NumberFormatException e) {
                ServletUtility.sendError(request, response, 400, "products.errors.privateListIdNotInt");
                return;
            }
        }

        try {
            List<HashMap> products =
                    productDAO
                            .filter(iId, name, description, categories, publicOnly, privateListId, column, dir, iOffset, iLength)
                            .stream()
                            .map( (AbstractMap.SimpleEntry<Product, String> e) -> {
                                Product pr = e.getKey();
                                String categoryName = e.getValue();

                                HashMap<String, Object> h = new HashMap<>();

                                h.put("id", pr.getId());
                                h.put("name", pr.getName());
                                h.put("description", pr.getDescription());
                                h.put("img", pr.getImg());
                                h.put("logo", pr.getLogo());
                                h.put("privateListId", pr.getPrivateListId());
                                h.put("categoryId", pr.getCategoryProductId());
                                h.put("categoryName", categoryName);

                                return h;
                            }).collect(Collectors.toList());

            Long totalCount = productDAO.getCount();
            Long filteredCount = productDAO.getCountFilter(iId, name, description, categories, publicOnly, privateListId);

            HashMap<String, Object> h = new HashMap<>();
            h.put("recordsTotal", totalCount);
            h.put("recordsFiltered", filteredCount);
            h.put("data", products);

            ServletUtility.sendJSON(
                    request,
                    response,
                    200,
                    h
            );
        } catch (DAOException e) {
            e.printStackTrace();
            ServletUtility.sendError(request, response, 500, "products.errors.impossibleDbFilter");
        }
    }

    private void modifyProduct(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Path path = ServletUtility.getFolder(getServletContext(), "productImgsFolder");

        Integer id;
        {
            String sId = req.getParameter("id");

            if(sId == null) {
                ServletUtility.sendError(req, res, 400, "products.errors.idNotInt");
                return;
            }

            try{
                id = Integer.parseInt(sId);
            } catch (NumberFormatException e) {
                ServletUtility.sendError(req, res, 400, "products.errors.idNotInt");
                return;
            }
        }

        String name = req.getParameter(ProductValidator.NAME_KEY);
        String description = req.getParameter(ProductValidator.DESCRIPTION_KEY);
        Part img = req.getPart(ProductValidator.IMG_KEY);
        Part logo = req.getPart(ProductValidator.LOGO_KEY);
        Integer categoryId = null;
        {
            String sCategoryId = req.getParameter(ProductValidator.CATEGORY_KEY);
            try {
                categoryId = sCategoryId == null || sCategoryId.trim().isEmpty()? null: Integer.parseInt(sCategoryId);
            } catch (NumberFormatException e) {
                ServletUtility.sendError(req, res, 400, "products.errors.catIdNotInt");
                return;
            }
        }

        Integer privateListId = null;
        {
            String sPrivateListId = req.getParameter(ProductValidator.PRIVATE_LIST_ID);

            try {
                privateListId = sPrivateListId == null || sPrivateListId.trim().isEmpty()? null: Integer.parseInt(sPrivateListId);
            } catch (NumberFormatException e) {
                ServletUtility.sendError(req, res, 400, "products.errors.privateListIdNotInt");
                return;
            }
        }

        HashMap<String, Object> kv = new HashMap<>();

        if (name != null && !name.trim().isEmpty()) {
            kv.put(ProductValidator.NAME_KEY, name);
        }

        if (description != null && !description.trim().isEmpty()) {
            kv.put(ProductValidator.DESCRIPTION_KEY, description);
        }

        if (img != null && img.getSize() > 0) {
            kv.put(ProductValidator.IMG_KEY, img);
        }

        if (logo != null && logo.getSize() > 0) {
            kv.put(ProductValidator.LOGO_KEY, logo);
        }

        if (categoryId != null) {
            kv.put(ProductValidator.CATEGORY_KEY, categoryId);
        }

        if (privateListId != null) {
            kv.put(ProductValidator.PRIVATE_LIST_ID, privateListId);
        }

        // Usa il validator per verifiacare la conformità
        Map<String, String> messages =
                ProductValidator.partialValidate(kv, categoryProductDAO, listDAO)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                (Map.Entry<String, ValidatorUtils.ErrorType> e) -> e.getKey(),
                                (Map.Entry<String, ValidatorUtils.ErrorType> e) -> ProductValidator.I18N_ERROR_STRING_PREFIX + '.' + e.getKey() + '.' + e.getValue().toString()
                                )
                        );

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(req, res, 400, messages);
            return;
        }

        Product product;

        try{
            product = productDAO.getByPrimaryKey(id);
        } catch (DAOException e) {
            ServletUtility.sendError(req, res, 400, "products.errors.impossibleSelect");
            return;
        }

        if (name != null && !name.trim().isEmpty()) {
            product.setName(name);
        }

        if (description != null && !description.trim().isEmpty()) {
            product.setDescription(description);
        }

        if (img != null && img.getSize() > 0) {
            String sImg = subImg(req, res, path, product.getImg(), img.getInputStream());
            product.setImg(sImg);
        }

        if (logo != null && logo.getSize() > 0) {
            String sLogo = subImg(req, res, path, product.getLogo(), logo.getInputStream());
            product.setLogo(sLogo);
        }

        if (categoryId != null) {
            product.setCategoryProductId(categoryId);
        }

        if (privateListId != null) {
            product.setPrivateListId(privateListId);
        }

        try {
            productDAO.update(product);
        } catch (DAOException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                if(e.getMessage().contains("fk_Product_CategoryProduct1")) {
                    HashMap<String, String> h = new HashMap<>();
                    h.put(
                            ProductValidator.CATEGORY_KEY,
                            ProductValidator.I18N_ERROR_STRING_PREFIX + '.' +
                                    ProductValidator.CATEGORY_KEY + '.' +
                                    ValidatorUtils.ErrorType.NOT_PRESENT.toString()
                    );

                    ServletUtility.sendValidationError(req, res, 400, h);
                    return;
                } else if(e.getMessage().contains("fk_Product_List1")){
                    HashMap<String, String> h = new HashMap<>();
                    h.put(
                            ProductValidator.PRIVATE_LIST_ID,
                            ProductValidator.I18N_ERROR_STRING_PREFIX +
                                    '.' + ProductValidator.PRIVATE_LIST_ID +
                                    '.' + ValidatorUtils.ErrorType.NOT_PRESENT.toString()
                    );

                    ServletUtility.sendValidationError(req, res, 400, h);
                    return;
                }
            }

            ServletUtility.sendError(req, res, 500, "products.errors.unupdatable");
            return;
        }

        ServletUtility.sendJSON(req, res, 200, new HashMap<>());
    }

    private void createProduct(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Path pathImg = ServletUtility.getFolder(getServletContext(), "productImgsFolder");
        Path pathLogo = ServletUtility.getFolder(getServletContext(), "productLogoImgsFolder");

        String name = req.getParameter(ProductValidator.NAME_KEY);
        String description = req.getParameter(ProductValidator.DESCRIPTION_KEY);
        Part img = req.getPart(ProductValidator.IMG_KEY);
        Part logo = req.getPart(ProductValidator.LOGO_KEY);
        Integer categoryId;
        {
            String sCategoryId = req.getParameter(ProductValidator.CATEGORY_KEY);
            if (sCategoryId == null) {
                ServletUtility.sendError(req, res, 400, "products.errors.catIdNotInt");
                return;
            }

            try {
                categoryId = Integer.parseInt(sCategoryId);
            } catch (NumberFormatException e) {
                ServletUtility.sendError(req, res, 400, "products.errors.catIdNotInt");
                return;
            }
        }

        Integer privateListId;
        {
            String sPrivateListId = req.getParameter(ProductValidator.PRIVATE_LIST_ID);

            if(sPrivateListId == null) {
                ServletUtility.sendError(req, res, 400, "products.errors.privateListIdNotInt");
                return;
            }

            try {
                privateListId = sPrivateListId == null || sPrivateListId.trim().isEmpty()? null: Integer.parseInt(sPrivateListId);
            } catch (NumberFormatException e) {
                ServletUtility.sendError(req, res, 400, "products.errors.privateListIdNotInt");
                return;
            }
        }

        // Usa il validator per verifiacare la conformità
        Map<String, String> messages =
                ProductValidator.completeValidate(
                        name,
                        description,
                        img,
                        logo,
                        categoryId,
                        privateListId,
                        categoryProductDAO,
                        listDAO
                ).entrySet()
                .stream()
                .collect(Collectors.toMap(
                        (Map.Entry<String, ValidatorUtils.ErrorType> e) -> e.getKey(),
                        (Map.Entry<String, ValidatorUtils.ErrorType> e) -> ProductValidator.I18N_ERROR_STRING_PREFIX + '.' + e.getKey() + '.' + e.getValue().toString()
                        )
                );

        if (!messages.isEmpty()) {
            ServletUtility.sendValidationError(req, res, 400, messages);
            return;
        }

        String sImg = subImg(req, res, pathImg, null, img.getInputStream());
        String sLogo = subImg(req, res, pathLogo, null, logo.getInputStream());

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategoryProductId(categoryId);
        product.setPrivateListId(privateListId);
        product.setImg(sImg);
        product.setLogo(sLogo);

        try {
            productDAO.insert(product);
        } catch (DAOException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                if(e.getMessage().contains("fk_Product_CategoryProduct1")) {
                    HashMap<String, String> h = new HashMap<>();
                    h.put(
                            ProductValidator.CATEGORY_KEY,
                            ProductValidator.I18N_ERROR_STRING_PREFIX + '.' +
                                    ProductValidator.CATEGORY_KEY + '.' +
                                    ValidatorUtils.ErrorType.NOT_PRESENT.toString()
                    );

                    ServletUtility.sendValidationError(req, res, 400, h);
                    return;
                } else if(e.getMessage().contains("fk_Product_List1")){
                    HashMap<String, String> h = new HashMap<>();
                    h.put(
                            ProductValidator.PRIVATE_LIST_ID,
                            ProductValidator.I18N_ERROR_STRING_PREFIX +
                                    '.' + ProductValidator.PRIVATE_LIST_ID +
                                    '.' + ValidatorUtils.ErrorType.NOT_PRESENT.toString()
                    );

                    ServletUtility.sendValidationError(req, res, 400, h);
                    return;
                }
            }

            ServletUtility.sendError(req, res, 500, "products.errors.unupdatable");
            return;
        }

        ServletUtility.sendJSON(req, res, 200, new HashMap<>());
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Path path = ServletUtility.getFolder(getServletContext(), "productImgsFolder");

        Integer id;
        {
            String sId = req.getParameter("id");

            if(sId == null) {
                ServletUtility.sendError(req, res, 400, "products.errors.idNotInt");
                return;
            }

            try{
                id = Integer.parseInt(sId);
            } catch (NumberFormatException e) {
                ServletUtility.sendError(req, res, 400, "products.errors.idNotInt");
                return;
            }
        }

        try {
            // Get the current status of the product to later delete its unused images
            Product pr = productDAO.getByPrimaryKey(id);

            // Delete the product
            productDAO.deleteProductById(id);

            // Delete now unused images
            ServletUtility.deleteFile(path, pr.getImg(), getServletContext());
        } catch (DAOException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                e.printStackTrace();
                ServletUtility.sendError(req, res, 500, "products.errors.otherDepend");
                return;
            }

            e.printStackTrace();
            ServletUtility.sendError(req, res, 500, "products.errors.undeletable");
            return;
        }

        ServletUtility.sendJSON(req, res, 200, new HashMap<>());
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action.equalsIgnoreCase("modify")) {
            modifyProduct(request, response);
            return;
        }

        if(action.equalsIgnoreCase("create")) {
            createProduct(request, response);
            return;
        }

        if(action.equalsIgnoreCase("delete")) {
            deleteProduct(request, response);
            return;
        }

        ServletUtility.sendError(request, response, 400, "products.errors.actionUnrecognized");
        return;
    }
}
