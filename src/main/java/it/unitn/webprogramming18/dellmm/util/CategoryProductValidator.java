package it.unitn.webprogramming18.dellmm.util;

import javax.servlet.http.Part;
import java.util.HashMap;

public class CategoryProductValidator {
    public static final String I18N_ERROR_STRING_PREFIX = "validateCategoryProduct.errors";

    public static final String
            NAME_KEY = "Name",
            DESCRIPTION_KEY = "Description",
            IMG_KEY = "Img";

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

    public static ValidatorUtils.ErrorType validateImg(Part file) {
        return ValidatorUtils.validateImg(file, IMG_MIN_SIZE, IMG_MAX_SIZE, REGEX_ALLOWED_IMG_TYPES);
    }

    public static HashMap<String, ValidatorUtils.ErrorType> partialValidate(HashMap<String, Object> kv) {
        HashMap<String, ValidatorUtils.ErrorType> r = new HashMap<>();

        if (kv.containsKey(NAME_KEY)) {
            ValidatorUtils.putIfNotNull(r, CategoryProductValidator::validateName, NAME_KEY, kv.get(NAME_KEY));
        }

        if (kv.containsKey(DESCRIPTION_KEY)) {
            ValidatorUtils.putIfNotNull(
                    r,
                    CategoryProductValidator::validateDescription,
                    DESCRIPTION_KEY,
                    kv.get(DESCRIPTION_KEY)
            );
        }

        if (kv.containsKey(IMG_KEY)) {
            ValidatorUtils.putIfNotNull(
                    r,
                    CategoryProductValidator::validateImg,
                    IMG_KEY,
                    kv.get(IMG_KEY)
            );
        }

        return r;
    }

    public static HashMap<String, ValidatorUtils.ErrorType> completeValidate(
            String name,
            String description,
            Part image
    ) {
        return partialValidate(new HashMap<String, Object>(){{
            put(NAME_KEY, name);
            put(DESCRIPTION_KEY, description);
            put(IMG_KEY, image);
        }});
    }
}
