package API_Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ListKpiValueType1 {

    InputStruct inputStruct;

    public ListKpiValueType1(InputStruct inputStruct) {
        this.inputStruct = inputStruct;
    }

    public ArrayList<OutResult> getData() {
        ArrayList<OutResult> resultList = new ArrayList<>();
        try {

            String sql = "SELECT time, longitude, latitude, `value`,kpi FROM `" + inputStruct.table + "` "
                    + "WHERE kpi='" + inputStruct.kpi + "' "
                    + "AND operator=" + inputStruct.operator + " AND network_type='" + inputStruct.network_type + "'";
            if (inputStruct.network_type == null) {
                sql = "SELECT time, longitude, latitude, `value`,kpi FROM `" + inputStruct.table + "` "
                        + "WHERE kpi='" + inputStruct.kpi + "' "
                        + "AND operator=" + inputStruct.operator;
            }

            Statement stmt = inputStruct.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                try {
                    OutResult outResult = new OutResult();
                    String date_time = rs.getString("time");
                    outResult.date = date_time.split(" ")[0];
                    outResult.time = date_time.split(" ")[1];
                    outResult.lat = rs.getDouble("latitude");
                    outResult.lng = rs.getDouble("longitude");
                    String kpi = rs.getString("kpi");
                    outResult.kpiValue = rs.getString("value");

                    resultList.add(outResult);
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
        public String network_type = null;
        public String kpi = null;
    }

    public static class OutResult {

        public String time = null;
        public String date = null;
        public Double lat = null;
        public Double lng = null;
        public String kpiValue = null;
    }
}
