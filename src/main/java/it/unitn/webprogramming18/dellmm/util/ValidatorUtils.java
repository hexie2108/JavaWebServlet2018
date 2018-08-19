package it.unitn.webprogramming18.dellmm.util;

import javax.servlet.http.Part;
import java.util.HashMap;
import java.util.function.Function;

public class ValidatorUtils {
    public static ErrorType validateString(String s, int max_len){
        if(s == null || s.trim().isEmpty()) {
            return ErrorType.STRING_EMPTY_OR_NULL;
        } else if(s.length() > max_len) {
            return ErrorType.STRING_TOO_LONG;
        }

        return null;
    }

    public static ErrorType validateImg(Part file, int min_size, int max_size, String regexTypes) {
        if (file == null || file.getSize() == 0){
            // Non permettere file vuoti o mancanti
            return ErrorType.FILE_EMPTY_OR_NULL;
        } else if (file.getSize() <= min_size) {
            // Non permettere dimensioni minori a min_size
            return ErrorType.FILE_TOO_SMALL;
        } else if (file.getSize() > max_size) {
            // Non permettere dimensioni superiori a max_file
            return ErrorType.FILE_TOO_BIG;
        }

        return isValidFileExtension(file.getContentType(), regexTypes);
    }


    private static ErrorType isValidFileExtension(String contentType, String regexTypes) {
        if (null == contentType || contentType.trim().isEmpty()) {
            // Non permettere file senza campo contentType o campo vuoto
            return ErrorType.FILE_CONTENT_TYPE_MISSING_OR_EMPTY;
        }

        if (!contentType.matches(regexTypes)) {
            // Non permettere file di tipo diverso da quello specificato
            return ErrorType.FILE_OF_WRONG_TYPE;
        }

        return null;
    }

    public static <TYPE, ERROR extends ValidatorUtils.ErrorType> void putIfNotNull(HashMap<String, ERROR> h, Function<TYPE, ERROR> validateMethod, String key, Object val) {
        TYPE v = null;

        try{
            v = (TYPE) val;
        } catch (ClassCastException e) {
            h.put(key, (ERROR)ValidatorUtils.ErrorType.OBJECT_WRONG_TYPE);
            return;
        }

        ERROR e = validateMethod.apply(v);

        if (e != null) {
            h.put(key, e);
        }
    }

    public enum ErrorType{
        STRING_EMPTY_OR_NULL,
        STRING_TOO_LONG,
        FILE_EMPTY_OR_NULL,
        FILE_TOO_SMALL,
        FILE_TOO_BIG,
        FILE_CONTENT_TYPE_MISSING_OR_EMPTY,
        FILE_OF_WRONG_TYPE,
        OBJECT_WRONG_TYPE
    }
}
