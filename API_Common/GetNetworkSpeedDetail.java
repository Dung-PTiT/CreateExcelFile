package API_Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GetNetworkSpeedDetail {

    InputStruct inputData;

    public GetNetworkSpeedDetail(InputStruct inputData) {
        this.inputData = inputData;
    }

    public ArrayList<OutResult> getData() {
        ArrayList<OutResult> resultList = new ArrayList<>();
        try {
            String sql = "SELECT time, longitude, latitude, `value`,kpi FROM `" + inputData.table + "` "
                    + "WHERE kpi='" + inputData.kpi + "' AND operator=" + inputData.operator
                    + " AND network_type='" + inputData.network_type + "'";
            Statement stmt = inputData.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                OutResult outResult = new OutResult();
                String date_time = rs.getString("time");
                outResult.date = date_time.split(" ")[0];
                outResult.time = date_time.split(" ")[1];
                outResult.lat = rs.getDouble("latitude");
                outResult.lng = rs.getDouble("longitude");
                outResult.speed = rs.getDouble("value");
                resultList.add(outResult);
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
        public Double speed = null;
    }
}
