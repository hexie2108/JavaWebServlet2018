package it.unitn.webprogramming18.dellmm.util;

import javax.servlet.http.Part;
import java.util.HashMap;

public class CategoryListValidator {
    public static final String
            NAME_KEY = "Name",
            DESCRIPTION_KEY = "Description",
            IMG1_KEY = "Img1",
            IMG2_KEY = "Img2",
            IMG3_KEY = "Img3";

    public static final String I18N_ERROR_STRING_PREFIX = "validateCategoryList.errors.";

    public static final int
            NAME_MAX_LEN = 44,
            DESCRIPTION_MAX_LEN = 44;

    public static final int
            MAX_LEN_FILE = 15 * 1000000,
            MIN_LEN_FILE = 0;

    public enum ErrorMessage {
        NAME_MISSING,
        NAME_TOO_LONG,
        DESCRIPTION_MISSING,
        DESCRIPTION_TOO_LONG,
        IMG1_MISSING,
        IMG_ZERO_DIM,
        IMG_TOO_BIG,
        IMG_NOT_IMG
    }

    private static ErrorMessage validateImg(Part img) {
        if(img.getSize() > CategoryListValidator.MAX_LEN_FILE) { // Non permettere dimensioni superiori ai ~15MB
            return ErrorMessage.IMG_TOO_BIG;
        } else if(!img.getContentType().startsWith("image/")) { // Not safe, uses extensions
            return ErrorMessage.IMG_NOT_IMG;
        }

        return null;
    }

    public static ErrorMessage validateName(String name) {
        if (name == null || name.isEmpty()){
            return ErrorMessage.NAME_MISSING;
        } else if (name.length() > NAME_MAX_LEN){
            return ErrorMessage.NAME_TOO_LONG;
        }

        return null;
    }

    public static ErrorMessage validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            return ErrorMessage.DESCRIPTION_MISSING;
        } else if (description.length() > DESCRIPTION_MAX_LEN) {
            return ErrorMessage.DESCRIPTION_TOO_LONG;
        }

        return null;
    }

    public static ErrorMessage validateImg1(Part img1) {
        if(img1 == null) {
            return ErrorMessage.IMG1_MISSING;
        } else if(img1.getSize() <= CategoryListValidator.MIN_LEN_FILE) {
            return ErrorMessage.IMG_ZERO_DIM;
        }

        return validateImg(img1);
    }

    public static ErrorMessage validateImg2(Part img2) {
        if (img2 != null && img2.getSize() > 0){
            return validateImg(img2);
        }

        return null;
    }

    public static ErrorMessage validateImg3(Part img3) {
        if (img3 != null && img3.getSize() > 0){
            return validateImg(img3);
        }

        return null;
    }

    private static void HMinsertErrorIfNotNull(HashMap<String, ErrorMessage> h, String k, ErrorMessage e){
        if(e != null) {
            h.put(k, e);
        }
    }

    public static HashMap<String, ErrorMessage> partialValidate(
        HashMap<String, Object> kv
    ) {
        HashMap<String, ErrorMessage> messages = new HashMap<>();

        if(kv.containsKey(NAME_KEY)) {
            HMinsertErrorIfNotNull(messages, NAME_KEY, validateName((String)kv.get(NAME_KEY)));
        }

        if(kv.containsKey(DESCRIPTION_KEY)) {
            HMinsertErrorIfNotNull(messages, DESCRIPTION_KEY, validateDescription((String)kv.get(DESCRIPTION_KEY)));
        }

        if(kv.containsKey(IMG1_KEY)) {
            HMinsertErrorIfNotNull(messages, IMG1_KEY, validateImg1((Part)kv.get(IMG1_KEY)));
        }

        if(kv.containsKey(IMG2_KEY)) {
            HMinsertErrorIfNotNull(messages, IMG2_KEY, validateImg2((Part)kv.get(IMG2_KEY)));
        }

        if(kv.containsKey(IMG3_KEY)) {
            HMinsertErrorIfNotNull(messages, IMG3_KEY, validateImg3((Part) kv.get(IMG3_KEY)));
        }

        return messages;
    }

    public static HashMap<String, ErrorMessage> completeValidate(
            String name,
            String description,
            Part img1,
            Part img2,
            Part img3
    ) {
        HashMap<String, Object> kv = new HashMap<>();

        kv.put(NAME_KEY, name);
        kv.put(DESCRIPTION_KEY, description);
        kv.put(IMG1_KEY, img1 );
        kv.put(IMG2_KEY, img2);
        kv.put(IMG3_KEY, img3);

        return partialValidate(kv);
    }
}
