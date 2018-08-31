package it.unitn.webprogramming18.dellmm.util;

import it.unitn.webprogramming18.dellmm.db.daos.CategoryProductDAO;
import it.unitn.webprogramming18.dellmm.db.daos.ListDAO;
import it.unitn.webprogramming18.dellmm.db.utils.exceptions.DAOException;

import javax.servlet.http.Part;
import java.util.HashMap;

public class ProductValidator {
        public static final String I18N_ERROR_STRING_PREFIX = "validateProduct.errors";

    public static final String
            NAME_KEY = "Name",
            DESCRIPTION_KEY = "Description",
            IMG_KEY = "Img",
            LOGO_KEY = "Logo",
            CATEGORY_KEY = "CategoryId",
            PRIVATE_LIST_ID = "PrivateListId";

    public static final int
            NAME_MAX_LEN = 44,
            DESCRIPTION_MAX_LEN = 44,
            IMG_MIN_SIZE = 0,
            IMG_MAX_SIZE = 15 * 1000000;

    private static final String REGEX_ALLOWED_IMG_TYPES = ".*(jpg|jpeg|png|gif|bmp).*";

    public static ValidatorUtils.ErrorType validateName(String name) {
        return ValidatorUtils.validateString(name, NAME_MAX_LEN);
    }

    public static ValidatorUtils.ErrorType validateDescription(String description)  {
        return ValidatorUtils.validateString(description, DESCRIPTION_MAX_LEN);
    }

    public static ValidatorUtils.ErrorType validateImg(Part img) {
        return ValidatorUtils.validateImg(img, IMG_MIN_SIZE, IMG_MAX_SIZE, REGEX_ALLOWED_IMG_TYPES);
    }

    public static ValidatorUtils.ErrorType validateLogo(Part logo) {
        return ValidatorUtils.validateImg(logo, IMG_MIN_SIZE, IMG_MAX_SIZE, REGEX_ALLOWED_IMG_TYPES);
    }

    public static ValidatorUtils.ErrorType validateCategory(Integer id, CategoryProductDAO categoryProductDAO) {
        if (categoryProductDAO == null) {
            return null;
        }


        try {
            if(categoryProductDAO.getByPrimaryKey(id) != null) {
                return null;
            }
        } catch (DAOException e) {
            e.printStackTrace();
            // If error occurs use default error(not present)
        }

        return ValidatorUtils.ErrorType.NOT_PRESENT;
    }

    public static ValidatorUtils.ErrorType validatePrivateList(Integer id, ListDAO listDAO) {
        if (listDAO == null || id == null) {
            return null;
        }

        try {
            if(listDAO.getByPrimaryKey(id) != null) {
                return null;
            }
        } catch (DAOException e) {
                 // If error occurs use default error(not present)

        }

        return ValidatorUtils.ErrorType.NOT_PRESENT;
    }


    public static HashMap<String, ValidatorUtils.ErrorType> partialValidate(
            HashMap<String, Object> kv,
            CategoryProductDAO categoryProductDAO,
            ListDAO listDAO
    ) {
        HashMap<String, ValidatorUtils.ErrorType> r = new HashMap<>();

        if (kv.containsKey(NAME_KEY)) {
            ValidatorUtils.putIfNotNull(
                    r,
                    ProductValidator::validateName,
                    NAME_KEY,
                    kv.get(NAME_KEY)
            );
        }

        if (kv.containsKey(DESCRIPTION_KEY)) {
            ValidatorUtils.putIfNotNull(
                    r,
                    ProductValidator::validateDescription,
                    DESCRIPTION_KEY,
                    kv.get(DESCRIPTION_KEY)
            );
        }

        if (kv.containsKey(IMG_KEY)) {
            ValidatorUtils.putIfNotNull(
                    r,
                    ProductValidator::validateImg,
                    IMG_KEY,
                    kv.get(IMG_KEY)
            );
        }

        if (kv.containsKey(LOGO_KEY)) {
            ValidatorUtils.putIfNotNull(
                    r,
                    ProductValidator::validateLogo,
                    LOGO_KEY,
                    kv.get(LOGO_KEY)
            );
        }

        if (kv.containsKey(CATEGORY_KEY)) {
            ValidatorUtils.putIfNotNull(
                    r,
                    (Integer i) -> validateCategory(i, categoryProductDAO),
                    CATEGORY_KEY,
                    kv.get(CATEGORY_KEY)
            );
        }

        if (kv.containsKey(PRIVATE_LIST_ID)) {
            ValidatorUtils.putIfNotNull(
                    r,
                    (Integer i) -> validatePrivateList(i, listDAO) ,
                    PRIVATE_LIST_ID,
                    kv.get(PRIVATE_LIST_ID)
            );
        }

        return r;
    }

    public static HashMap<String, ValidatorUtils.ErrorType> completeValidate(
            String name,
            String description,
            Part image,
            Part logo,
            Integer categoryProductId,
            Integer privateListId,
            CategoryProductDAO categoryProductDAO,
            ListDAO listDAO
    ) {
        return partialValidate(new HashMap<String, Object>(){{
            put(NAME_KEY, name);
            put(DESCRIPTION_KEY, description);
            put(IMG_KEY, image);
            put(LOGO_KEY, logo);
            put(CATEGORY_KEY, categoryProductId);
            put(PRIVATE_LIST_ID, privateListId);
        }}, categoryProductDAO, listDAO);
    }

}
