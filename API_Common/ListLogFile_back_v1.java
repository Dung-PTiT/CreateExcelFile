package API_Common;

import java.sql.Connection;
import java.util.ArrayList;

public class ListLogFile_back_v1 {

    InputStruct inputStruct;

    public ListLogFile_back_v1(InputStruct inputStruct) {
        this.inputStruct = inputStruct;
    }

    public ArrayList<String> getData() {
        ArrayList<String> resultList = new ArrayList<>();
        try {
            String sql = "SELECT log_file FROM `" + inputStruct.table + "` "
                    + "WHERE log_file!='' AND kpi='" + inputStruct.kpi + "' "
                    + "AND network_type='" + inputStruct.network_type + "' AND operator=" + inputStruct.operator
                    + " GROUP BY log_file ";
            if (inputStruct.addtional_condition != null) {
                sql = "SELECT log_file FROM `" + inputStruct.table + "` "
                        + "WHERE log_file!='' AND kpi='" + inputStruct.kpi + "' "
                        + "AND network_type='" + inputStruct.network_type + "' AND operator=" + inputStruct.operator
                        + " AND " + inputStruct.addtional_condition + " GROUP BY log_file ";
            }
            System.out.println(sql);
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
        public String addtional_condition = null;
    }
}
