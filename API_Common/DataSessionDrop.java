package API_Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DataSessionDrop {

    InputStruct inputStruct;

    public DataSessionDrop(InputStruct inputStruct) {
        this.inputStruct = inputStruct;
    }

    public ArrayList<OutResult> getData() {
        ArrayList<OutResult> resultList = new ArrayList<>();
        try {
            String sql = "SELECT time, longitude, latitude, `value`,kpi FROM `" + inputStruct.table + "` "
                    + "WHERE (kpi='" + inputStruct.kpi1 + "' OR kpi='" + inputStruct.kpi2 + "') "
                    + "AND operator=" + inputStruct.operator + " AND network_type='" + inputStruct.network_type + "'";
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
                    outResult.kpi = kpi;
                    if (kpi.equals(inputStruct.kpi1)) {
                        outResult.avg_through_put = rs.getDouble("value");
                    } else if (kpi.equals(inputStruct.kpi2)) {
                        outResult.avg_through_put_not_complete = rs.getDouble("value");
                    }
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
        public String kpi1 = null;
        public String kpi2 = null;
    }

    public static class OutResult {

        public String kpi = null;
        public String time = null;
        public String date = null;
        public Double lat = null;
        public Double lng = null;
        public Double avg_through_put = null;
        public Double avg_through_put_not_complete = null;
    }
}
