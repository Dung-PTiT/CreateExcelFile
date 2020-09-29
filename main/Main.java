package main;

import JDBC.JDBCConnection;
import com.mysql.jdbc.JDBC4Connection;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import runExport.QCVN81_4G;
import template.QCVN;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Connection connection = JDBCConnection.getInstance().getConnection();
        InputStream inp = new FileInputStream("C:\\Users\\Dell\\Downloads\\QCVN81_4G_Template.xlsx");
        XSSFWorkbook fWorkbook = new XSSFWorkbook(inp);
        String tablePrefix = "2020_p0514092020ph3_hgg";
        // tạo sheet
        QCVN81_4G qcvn81 = new QCVN81_4G(fWorkbook, connection, tablePrefix);
        qcvn81.createSheet1();
//        qcvn81.createSheet2();
//        qcvn81.createSheet3();
//        qcvn81.createSheet4();
//        qcvn81.createSheet51DL();
//        qcvn81.createSheet51UL();
//        qcvn81.createSheet52();
//        qcvn81.createSheetListOfLogFile();
        // ghi file
        FileOutputStream os = new FileOutputStream("C:\\Users\\Dell\\Downloads\\Test.xlsx");
        fWorkbook.setForceFormulaRecalculation(true);
        fWorkbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        fWorkbook.write(os);
    }
}
