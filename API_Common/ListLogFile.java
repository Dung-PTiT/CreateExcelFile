package API_Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ListLogFile {

    ArrayList<InputStruct> inputStructList;

    public ListLogFile(ArrayList<InputStruct> inputStructList) {
        this.inputStructList = inputStructList;
    }

    public HashMap<String, ArrayList<String>> getData() {
        HashMap<String, ArrayList<String>> resultMap = new HashMap<>();
        for (InputStruct inputStruct : inputStructList) {
            try {
                String kpi_id = inputStruct.kpi_id;
                String sql_where_network_type = "";
                if (inputStruct.network_type != null) {
                    sql_where_network_type = " AND network_type='" + inputStruct.network_type + "' ";
                }
                String sql = "SELECT log_file FROM `" + inputStruct.table + "` "
                        + "WHERE log_file!='' AND kpi='" + inputStruct.kpi + "' "
                        + sql_where_network_type + " AND operator=" + inputStruct.operator
                        + " GROUP BY log_file ";
                if (inputStruct.addtional_condition != null) {
                    sql = "SELECT log_file FROM `" + inputStruct.table + "` "
                            + "WHERE log_file!='' AND kpi='" + inputStruct.kpi + "' "
                            + sql_where_network_type + " AND operator=" + inputStruct.operator
                            + " AND " + inputStruct.addtional_condition + " GROUP BY log_file ";
                }
                Statement stmt = inputStruct.conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String log_file = rs.getString("log_file");
                    if (resultMap.containsKey(log_file)) {
                        ArrayList<String> listKpi = resultMap.get(log_file);
                        if (!listKpi.contains(kpi_id)) {
                            listKpi.add(kpi_id);
                        }
                    } else {
                        ArrayList<String> listKpi = new ArrayList<>();
                        listKpi.add(kpi_id);
                        resultMap.put(log_file, listKpi);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    public static class InputStruct {

        public String kpi_id = null;
        public Connection conn = null;
        public String table = null;
        public Integer operator = null;
        public String network_type = null;
        public String kpi = null;
        public String addtional_condition = null;
    }
}
