/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package template;

import java.sql.Connection;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Dell
 */
public class QCVN {

    public XSSFWorkbook templateFile;
    public Connection connection;
    public String tablePrefix;

    public QCVN(XSSFWorkbook templateFile, Connection connection, String tablePrefix) {
        this.templateFile = templateFile;
        this.connection = connection;
        this.tablePrefix = tablePrefix;
    }
}
