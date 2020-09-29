package runExport;

import API_Common.DataSessionDrop;
import API_Common.GetNetworkSpeedDetail;
import API_Common.ListKpiValueType1;
import API_Common.ListKpiValueType2;
import API_Common.ListKpiValueType2.OutResult;
import API_Common.ListLogFile;
import API_Common.SignalCommonInfo;
import API_Common.SignalInfoByRange;
import RequestImg.GetImage;
import RequestImg.MarkerInfo;
import template.QCVN;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QCVN81_4G extends QCVN {

    public QCVN81_4G(XSSFWorkbook templateFile, Connection connection, String tablePrefix) {
        super(templateFile, connection, tablePrefix);
    }

    public void createSheet1() {
        try {
            XSSFSheet sheet = this.templateFile.getSheetAt(1);
            SignalCommonInfo.InputStruct rsrpInput = new SignalCommonInfo.InputStruct();
            rsrpInput.conn = this.connection;
            rsrpInput.table = this.tablePrefix + "_clvp_qcvn";
            rsrpInput.kpi = "25.1";
            rsrpInput.network_type = "4G";
            rsrpInput.operator = 2;

            SignalCommonInfo signalCommonInfo = new SignalCommonInfo(rsrpInput);
            SignalCommonInfo.OutResult rscpOutput = new SignalCommonInfo.OutResult();
            rscpOutput = signalCommonInfo.getData();

            Row row3 = sheet.getRow(3);
            row3.getCell(0).setCellValue(rscpOutput.mean);
            row3.getCell(1).setCellValue(rscpOutput.min);
            row3.getCell(2).setCellValue(rscpOutput.max);
            row3.getCell(3).setCellValue(rscpOutput.count);

            SignalInfoByRange.InputStruct rscpRangeIn = new SignalInfoByRange.InputStruct();
            rscpRangeIn.conn = this.connection;
            rscpRangeIn.table = this.tablePrefix + "_clvp_qcvn";
            rscpRangeIn.kpi = "25.1";
            rscpRangeIn.network_type = "4G";
            rscpRangeIn.operator = 2;
            rscpRangeIn.range = new Float[]{-121F};
            SignalInfoByRange signalInfoByRange = new SignalInfoByRange(rscpRangeIn);
            Integer[] resultArray = signalInfoByRange.getData();
            Row row7 = sheet.getRow(7);
            row7.getCell(1).setCellValue(resultArray[0]);
            row7.getCell(2).setCellValue(resultArray[1]);

            //get picture
            ListKpiValueType1.InputStruct rsrpInputT1 = new ListKpiValueType1.InputStruct();
            rsrpInputT1.conn = this.connection;
            rsrpInputT1.table = this.tablePrefix + "_clvp_qcvn";
            rsrpInputT1.kpi = "25.1";
            rsrpInputT1.operator = 2;
            rsrpInputT1.network_type = "4G";

            ArrayList<ListKpiValueType1.OutResult> rscpList = new ListKpiValueType1(rsrpInputT1).getData();
            ArrayList<MarkerInfo> markerList = new ArrayList<>();
            for (ListKpiValueType1.OutResult rscpPoint : rscpList) {
                try {
                    MarkerInfo markerInfo = new MarkerInfo();
                    markerInfo.lat = rscpPoint.lat;
                    markerInfo.lng = rscpPoint.lng;
                    markerInfo.level = 5;
                    double rscpValue = Double.parseDouble(rscpPoint.kpiValue);

                    if (rscpValue >= -121) {
                        markerInfo.level = 1;
                    }
                    markerList.add(markerInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            GetImage getImage = new GetImage(markerList);
            byte[] bytes = getImage.getImgBytes();
            int my_picture_id = templateFile.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            /* Create the drawing container */
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            /* Create an anchor point */
            XSSFClientAnchor my_anchor = new XSSFClientAnchor();
            /* Define top left corner, and we can resize picture suitable from there */
            my_anchor.setCol1(1);
            my_anchor.setCol2(9);
            my_anchor.setRow1(12);
            my_anchor.setRow2(33);
            XSSFPicture my_picture = drawing.createPicture(my_anchor, my_picture_id);
            my_picture.resize(0.7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSheet2() {
        try {
            XSSFSheet sheet = this.templateFile.getSheetAt(2);
            ListKpiValueType2.InputStruct rsrpInput = new ListKpiValueType2.InputStruct();
            rsrpInput.conn = this.connection;
            rsrpInput.table = this.tablePrefix + "_data_qcvn";
            rsrpInput.kpi1 = "35.1";
            rsrpInput.kpi2 = "35.2";
            rsrpInput.network_type = "4G";
            rsrpInput.operator = 2;

            ListKpiValueType2 listKpiValueType2 = new ListKpiValueType2(rsrpInput);
            ArrayList<ListKpiValueType2.OutResult> resultList = listKpiValueType2.getData();

            for (int i = 0; i < resultList.size(); i++) {
                ListKpiValueType2.OutResult rscpOutput = new ListKpiValueType2.OutResult();
                rscpOutput = resultList.get(i);
                Row row = sheet.getRow(i + 3);
                row.getCell(0).setCellValue(rscpOutput.time);
                row.getCell(1).setCellValue(rscpOutput.date);
                row.getCell(2).setCellValue(rscpOutput.lat);
                row.getCell(3).setCellValue(rscpOutput.lng);
                row.getCell(4).setCellValue(rscpOutput.kpi1Value);
                row.getCell(5).setCellValue(rscpOutput.kpi2Value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSheet3() {
        try {
            XSSFSheet sheet = this.templateFile.getSheetAt(3);
            ListKpiValueType2.InputStruct rsrpInput = new ListKpiValueType2.InputStruct();
            rsrpInput.conn = this.connection;
            rsrpInput.table = this.tablePrefix + "_data_qcvn";
            rsrpInput.kpi1 = "35.1";
            rsrpInput.kpi2 = "35.2";
            rsrpInput.network_type = "4G";
            rsrpInput.operator = 2;

            ListKpiValueType2 listKpiValueType2 = new ListKpiValueType2(rsrpInput);
            ArrayList<ListKpiValueType2.OutResult> resultList = listKpiValueType2.getData();

            for (int i = 0; i < resultList.size(); i++) {
                ListKpiValueType2.OutResult rscpOutput = new ListKpiValueType2.OutResult();
                rscpOutput = resultList.get(i);
                int tmp = i + 3;
                Row row = sheet.getRow(tmp);
                row.getCell(0).setCellValue(rscpOutput.time);
                row.getCell(1).setCellValue(rscpOutput.date);
                row.getCell(2).setCellValue(rscpOutput.lat);
                row.getCell(3).setCellValue(rscpOutput.lng);
                row.getCell(4).setCellValue(rscpOutput.kpi1Value);
                row.getCell(5).setCellValue(rscpOutput.kpi2Value);
                if (i == 0) {
                    row.getCell(6).setCellValue("");
                } else {
                    String formulaStr = "IF(LEN(F" + (i + 4) + ")>5, (A" + (i + 4) + "-A" + tmp + ")*86400,\" \")";
                    row.getCell(6).setCellFormula(formulaStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSheet4() {
        try {
            XSSFSheet sheet = this.templateFile.getSheetAt(4);
            DataSessionDrop.InputStruct rsrpInputDownload = new DataSessionDrop.InputStruct();
            rsrpInputDownload.conn = this.connection;
            rsrpInputDownload.table = this.tablePrefix + "_data_qcvn";
            rsrpInputDownload.kpi1 = "36.1";
            rsrpInputDownload.kpi2 = "36.2";
            rsrpInputDownload.network_type = "4G";
            rsrpInputDownload.operator = 2;

            DataSessionDrop downloadValue = new DataSessionDrop(rsrpInputDownload);
            ArrayList<DataSessionDrop.OutResult> resultDownloadList = downloadValue.getData();

            DataSessionDrop.InputStruct rsrpInputUpload = new DataSessionDrop.InputStruct();
            rsrpInputUpload.conn = this.connection;
            rsrpInputUpload.table = this.tablePrefix + "_data_qcvn";
            rsrpInputUpload.kpi1 = "36.3";
            rsrpInputUpload.kpi2 = "36.4";
            rsrpInputUpload.network_type = "4G";
            rsrpInputUpload.operator = 2;

            DataSessionDrop uploadValue = new DataSessionDrop(rsrpInputUpload);
            ArrayList<DataSessionDrop.OutResult> resultUploadList = uploadValue.getData();

            for (int i = 0; i < resultUploadList.size(); i++) {
                resultDownloadList.add(resultUploadList.get(i));
            }
            for (int i = 0; i < resultDownloadList.size(); i++) {
                DataSessionDrop.OutResult rscpOutput = new DataSessionDrop.OutResult();
                rscpOutput = resultDownloadList.get(i);
                Row row = sheet.getRow(i + 7);
                row.getCell(0).setCellValue(rscpOutput.time);
                row.getCell(1).setCellValue(rscpOutput.date);
                row.getCell(2).setCellValue(rscpOutput.lat);
                row.getCell(3).setCellValue(rscpOutput.lng);
                if (rscpOutput.avg_through_put != null) {
                    row.getCell(4).setCellValue(rscpOutput.avg_through_put);
                }
                if (rscpOutput.avg_through_put_not_complete != null) {
                    row.getCell(5).setCellValue(rscpOutput.avg_through_put_not_complete);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSheet51DL() {
        try {
            XSSFSheet sheet = this.templateFile.getSheet("5.1_DL");
            GetNetworkSpeedDetail.InputStruct rsrpInput = new GetNetworkSpeedDetail.InputStruct();
            rsrpInput.conn = this.connection;
            rsrpInput.table = this.tablePrefix + "_data_qcvn";
            rsrpInput.kpi = "36.1";
            rsrpInput.network_type = "4G";
            rsrpInput.operator = 2;

            GetNetworkSpeedDetail speedDetail = new GetNetworkSpeedDetail(rsrpInput);
            ArrayList<GetNetworkSpeedDetail.OutResult> speedtList = speedDetail.getData();

            for (int i = 0; i < speedtList.size(); i++) {
                GetNetworkSpeedDetail.OutResult rscpOutput = new GetNetworkSpeedDetail.OutResult();
                rscpOutput = speedtList.get(i);
                Row row = sheet.getRow(i + 3);
                row.getCell(0).setCellValue(rscpOutput.time);
                row.getCell(1).setCellValue(rscpOutput.date);
                row.getCell(2).setCellValue(rscpOutput.lat);
                row.getCell(3).setCellValue(rscpOutput.lng);
                row.getCell(4).setCellValue(rscpOutput.speed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSheet51UL() {
        try {
            XSSFSheet sheet = this.templateFile.getSheet("5.1_UL");
            GetNetworkSpeedDetail.InputStruct rsrpInput = new GetNetworkSpeedDetail.InputStruct();
            rsrpInput.conn = this.connection;
            rsrpInput.table = this.tablePrefix + "_data_qcvn";
            rsrpInput.kpi = "36.3";
            rsrpInput.network_type = "4G";
            rsrpInput.operator = 2;

            GetNetworkSpeedDetail speedDetail = new GetNetworkSpeedDetail(rsrpInput);
            ArrayList<GetNetworkSpeedDetail.OutResult> speedtList = speedDetail.getData();

            for (int i = 0; i < speedtList.size(); i++) {
                GetNetworkSpeedDetail.OutResult rscpOutput = new GetNetworkSpeedDetail.OutResult();
                rscpOutput = speedtList.get(i);
                Row row = sheet.getRow(i + 3);
                row.getCell(0).setCellValue(rscpOutput.time);
                row.getCell(1).setCellValue(rscpOutput.date);
                row.getCell(2).setCellValue(rscpOutput.lat);
                row.getCell(3).setCellValue(rscpOutput.lng);
                row.getCell(4).setCellValue(rscpOutput.speed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSheet52() {
        try {
            XSSFSheet sheet = this.templateFile.getSheet("5.2");
            GetNetworkSpeedDetail.InputStruct rsrpInput = new GetNetworkSpeedDetail.InputStruct();
            rsrpInput.conn = this.connection;
            rsrpInput.table = this.tablePrefix + "_data_qcvn";
            rsrpInput.kpi = "36.1";
            rsrpInput.network_type = "4G";
            rsrpInput.operator = 2;

            GetNetworkSpeedDetail speedDetail = new GetNetworkSpeedDetail(rsrpInput);
            ArrayList<GetNetworkSpeedDetail.OutResult> speedtList = speedDetail.getData();

            for (int i = 0; i < speedtList.size(); i++) {
                GetNetworkSpeedDetail.OutResult rscpOutput = new GetNetworkSpeedDetail.OutResult();
                rscpOutput = speedtList.get(i);
                Row row = sheet.getRow(i + 3);
                row.getCell(0).setCellValue(rscpOutput.time);
                row.getCell(1).setCellValue(rscpOutput.date);
                row.getCell(2).setCellValue(rscpOutput.lat);
                row.getCell(3).setCellValue(rscpOutput.lng);
                row.getCell(4).setCellValue(rscpOutput.speed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSheetListOfLogFile() {
        try {
            XSSFSheet sheet = this.templateFile.getSheet("List of Logfiles");
            ArrayList<ListLogFile.InputStruct> inputStructList = new ArrayList<>();
            String[] kpi_list = {"25.1", "35.1", "35.2", "36.1", "36.3", "36.1", "36.3"};
            String[] kpi_id = {"kpi_251", "kpi_351", "kpi_352", "kpi_361_drop", "kpi_363_drop", "kpi_361_pd", "kpi_363_pu"};
            for (int i = 0; i < kpi_list.length; i++) {
                ListLogFile.InputStruct rsrpInput = new ListLogFile.InputStruct();
                if (i == 0) {
                    rsrpInput.table = this.tablePrefix + "_clvp_qcvn";
                } else {
                    rsrpInput.table = this.tablePrefix + "_data_qcvn";
                }
                rsrpInput.kpi_id = kpi_id[i];
                rsrpInput.conn = this.connection;
                rsrpInput.kpi = kpi_list[i];
                rsrpInput.network_type = "4G";
                rsrpInput.operator = 2;
                rsrpInput.addtional_condition = null;
                inputStructList.add(rsrpInput);
            }
            ListLogFile listLogFile = new ListLogFile(inputStructList);
            HashMap<String, ArrayList<String>> dataHashMap = listLogFile.getData();
            Integer rowId = 2;
            for (String logFileName : dataHashMap.keySet()) {
                try {
                    ArrayList<String> kpiIdList = dataHashMap.get(logFileName);
                    Row row = sheet.getRow(rowId);
                    if (rowId > 100) {
                        row = sheet.createRow(rowId);
                    }
                    row.createCell(1).setCellValue(logFileName);

                    for (String kpiId : kpiIdList) {
                        switch (kpiId) {
                            case "kpi_251": //do san sang
                                row.createCell(3).setCellValue("x");
                                break;
                            case "kpi_351": //ty le truy nhap thanh cong
                                row.createCell(4).setCellValue("x");
                                break;
                            case "kpi_352": // thoi gian tre truy nhap
                                row.createCell(5).setCellValue("x");
                                break;
                            case "kpi_361_drop": //ty le truyen tai du lieu bi roi (dl)
                                row.createCell(6).setCellValue("x");
                                break;
                            case "kpi_363_drop": //ty le truyen tai du lieu bi roi (ul)
                                row.createCell(6).setCellValue("x");
                                break;
                            case "kpi_361_pd": //pd and %pd
                                row.createCell(7).setCellValue("x");
                                row.createCell(9).setCellValue("x");
                                break;
                            case "kpi_363_pu": //pu
                                row.createCell(8).setCellValue("x");
                                break;

                            default:
                                System.out.println("no match");
                        }
                    }
                    rowId++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
