/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RequestImg;

import API_Common.ListKpiValueType1;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.apache.poi.util.IOUtils;

/**
 *
 * @author hung_
 */
public class GetImage {

    ArrayList<MarkerInfo> inputPointList;
    public static final double MAX_DISTANCE = 20;

    public static final String imgServer = "http://14.232.245.200:11000/up_data";

    public GetImage(ArrayList<MarkerInfo> inputPointList) {
        this.inputPointList = inputPointList;
    }

    ArrayList<MarkerInfo> reducedPointList(ArrayList<MarkerInfo> inputPointList) {
        ArrayList<MarkerInfo> outPointList = new ArrayList<>();
        MarkerInfo lastPoint = null;
        for (int i = 0; i < inputPointList.size(); i++) {
            try {
                MarkerInfo inPoint = inputPointList.get(i);
                if (i == 0) {
                    lastPoint = inPoint;
                } else {
                    double distance = getdistance(lastPoint.lat, inPoint.lat, lastPoint.lng, inPoint.lng, 0, 0);
                    if (distance < MAX_DISTANCE) {
                        if (lastPoint.level != inPoint.level) {
                            outPointList.add(inPoint);
                            lastPoint = inPoint;
                        }
                    } else {
                        outPointList.add(inPoint);
                        lastPoint = inPoint;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outPointList;
    }

    public byte[] getImgBytes() {
        try {
            Gson gson = new Gson();
            ArrayList<MarkerInfo> outPointList = reducedPointList(inputPointList);
            PointListInfo pointListInfo = new PointListInfo();
            pointListInfo.type = "rscp";
            pointListInfo.pointList = outPointList;
            String dataString = gson.toJson(pointListInfo);
            byte[] requestData = dataString.getBytes();
            //send request
            URL url = new URL(imgServer);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = connection.getOutputStream();
            os.write(requestData, 0, requestData.length);
            os.flush();
            os.close();
            InputStream is = connection.getInputStream();

            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[1];
    }

    public static double getdistance(double lat1, double lat2, double lon1,
            double lon2, double el1, double el2) {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }
}
