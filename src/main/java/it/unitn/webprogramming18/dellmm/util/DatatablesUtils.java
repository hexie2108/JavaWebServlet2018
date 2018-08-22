package it.unitn.webprogramming18.dellmm.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DatatablesUtils {
    public static String getColumnName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get orderBy string, if missing or empty send error
        String orderBy = request.getParameter("order[0][column]");
        if (orderBy == null || orderBy.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.orderByMissing");
            return null;
        }

        // convert orderBy to int, if not possible send error
        int columnId;
        try {
            columnId = Integer.parseInt(orderBy);
        } catch (NumberFormatException e){
            ServletUtility.sendError(request, response, 400, "datatables.errors.orderByNotInt");
            return null;
        }

        // Get name of column(using index previously found), if missing or empty send error
        String columnName = request.getParameter("columns[" + columnId + "][name]");
        if (columnName == null || columnName.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.columnNameMissing");
            return null;
        }

        return columnName;
    }

    public static Boolean getDirection(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get direction string, if missing or empty send error
        String direction = request.getParameter("order[0][dir]");
        if (direction == null || direction.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.dirMissing");
            return null;
        }

        // Convert to boolean(true for ascendent, true for descendent), if not possible send error
        switch (direction) {
            case "asc": return true;
            case "desc": return false;
            default:
                ServletUtility.sendError(request, response, 400, "datatables.errors.dirUnrecognized");
                return null;
        }
    }

    public static Integer getOffset(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the offset string, if missing or empty send error
        String offset = request.getParameter("start");
        if(offset == null || offset.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.offsetMissing");
            return null;
        }

        // Cast offset to int, if an error occurs send error
        try{
            return Integer.parseInt(offset);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.offsetNotInt");
            return null;
        }
    }

    public static Integer getLength(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the length string, if missing or empty send error
        String length = request.getParameter("length");
        if(length == null || length.trim().isEmpty()) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.lengthMissing");
            return null;
        }

        // Cast length to int, if an error occurs send error
        try{
            return Integer.parseInt(length);
        } catch (NumberFormatException e) {
            ServletUtility.sendError(request, response, 400, "datatables.errors.lengthNotInt");
            return null;
        }
    }
}
