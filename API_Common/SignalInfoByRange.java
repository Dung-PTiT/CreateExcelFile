package API_Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignalInfoByRange {

    InputStruct inputStruct;

    public SignalInfoByRange(InputStruct inputStruct) {
        this.inputStruct = inputStruct;
    }

    public Integer[] getData() {
        Integer[] outResultArray = new Integer[inputStruct.range.length + 1];
        for (int i = 0; i <= inputStruct.range.length; i++) {
            try {
                String sql = null;
                if (i == 0) {
                    sql = "SELECT count(*) as count FROM " + inputStruct.table + " WHERE `value`+0.0 <" + inputStruct.range[i] + "";
                } else if (i == inputStruct.range.length) {
                    sql = "SELECT count(*) as count FROM " + inputStruct.table + " WHERE `value`+0.0 >=" + inputStruct.range[i - 1] + "";
                } else {
                    sql = "SELECT count(*) as count FROM " + inputStruct.table + " WHERE "
                            + "`value`+0.0 >=" + inputStruct.range[i - 1] + " AND `value`+0.0 <" + inputStruct.range[i] + "";
                }
                sql = sql + " AND kpi='" + inputStruct.kpi + "' AND network_type='" + inputStruct.network_type + "' AND operator=" + inputStruct.operator + "";
                Statement stmt = inputStruct.conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                rs.last();
                Integer outResult = rs.getInt("count");
                outResultArray[i] = outResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return outResultArray;
    }

    public static class InputStruct {

        public Connection conn = null;
        public String table = null;
        public Integer operator = null;
        public String network_type = null;
        public String kpi = null;
        public Float[] range = null;
    }
}
