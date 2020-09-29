package API_Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OtherDBStuff {

    public static String getProvineByCode(Connection conn, String code) {
        String provine = "NA";
        try {
            String sql = "SELECT name FROM `province` WHERE code='" + code + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();
            provine = rs.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provine;
    }
}
