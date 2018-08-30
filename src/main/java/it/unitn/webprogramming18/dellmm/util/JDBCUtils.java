package it.unitn.webprogramming18.dellmm.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public interface JDBCUtils {
    static void setNullOrInt(PreparedStatement stm, int i, Integer num) throws SQLException {
        if (num == null) {
            stm.setNull(i, Types.INTEGER);
        } else {
            stm.setInt(i, num);
        }
    }

    static int setIntArray(PreparedStatement stm, int i, List<Integer> array) throws SQLException {
        for(Integer n: array) {
            stm.setInt(i, n);
            i++;
        }

        return i;
    }
}
