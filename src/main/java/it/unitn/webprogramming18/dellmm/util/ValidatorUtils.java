package it.unitn.webprogramming18.dellmm.util;

import javax.servlet.http.Part;
import java.util.HashMap;
import java.util.function.Function;

public class ValidatorUtils {
    /**
     * Checks if a string is empty/null or is longer than max_len
     * @param s string to check
     * @param max_len max length permitted without error
     * @return STRING_EMPTY_OR_NULL if s is empty or null, STRING_TOO_LONG if s is longer than max_len, null if no error is found
     */
    public static ErrorType validateString(String s, int max_len){
        if(s == null || s.trim().isEmpty()) {
            return ErrorType.STRING_EMPTY_OR_NULL;
        } else if(s.length() > max_len) {
            return ErrorType.STRING_TOO_LONG;
        }

        return null;
    }

    /**
     * Validate an image
     * @param file file to validate
     * @param min_size minimum size of the file
     * @param max_size maximum size of the file
     * @param regexTypes regex of the contentTYpe of the file
     * @return null if no error,
     *          FILE_EMPTY_OR_NULL if file is empty or null,
     *          FILE_TOO_SMALL if size of the file is <= min_size,
     *          FILE_TOO_BIG if size of the file is > max_size,
     *          FILE_CONTENT_TYPE_MISSING_OR_EMPTY if file's contentType is null or empty,
     *          FILE_OF_WRONG_TYPE if file's contentType doesn't match regexTypes
     */
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


    /**
     * Check if passed contentType is valid(using the regex passed
     * @param contentType string that represents the contentype of the file
     * @param regexTypes regex that the contentType has to match
     * @return null if no error,
     *          FILE_OF_WRONG_TYPE if contentType is not the one requested,
     *          FILE_CONTENT_TYPE_MISSING_OR_EMPTY if the contentType is empty or null
     */
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

    /**
     * Validates type of val and after user-defined validateMethod
     * @param h hashmap in which we put the error
     * @param validateMethod method to use to validate val(must return null for no error or TYPE value for specific error)
     * @param key key to use to insert the error in h
     * @param val value to validate
     * @param <TYPE> TYPE that validateMethod accepts as argument and is (before calling validateMethod) checked(instanceof)
     * @param <ERROR> enum that inherits ValidatorUtils.ErrorType that describes errors
     */
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
