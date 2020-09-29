package API_Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GetPlaceList {

    InputStruct inputStruct;

    public GetPlaceList(InputStruct inputStruct) {
        this.inputStruct = inputStruct;
    }

    public ArrayList<String> getData() {
        ArrayList<String> resultList = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT place_name FROM `" + inputStruct.table + "` WHERE operator=" + inputStruct.operator + " ";

            if (inputStruct.district != null) {
                sql = sql + " AND district= '" + inputStruct.district + "' ";
            }
            Statement stmt = inputStruct.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                try {
                    String place_name = rs.getString("place_name");
                    resultList.add(place_name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static class InputStruct {

        public Connection conn = null;
        public String table = null;
        public Integer operator = null;
        public String district = null;
    }

}
