/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RequestImg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestPostRequest {

    public static void main_2(String[] args) {
        try {
            URL url = new URL("http://14.232.245.200:11000/test");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            OutputStream os = new FileOutputStream(new File("test.png"));
            byte[] b = new byte[4096];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            File file = new File("rscp.json");
            byte[] array = Files.readAllBytes(file.toPath());
            URL url = new URL("http://14.232.245.200:11000/up_data");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = connection.getOutputStream();
            os.write(array, 0, array.length);
            os.flush();
            os.close();

            int res = connection.getResponseCode();

            InputStream is = connection.getInputStream();
            OutputStream os2 = new FileOutputStream(new File("test33.png"));

            byte[] b = new byte[4096];
            int length;
            while ((length = is.read(b)) != -1) {
                os2.write(b, 0, length);
            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
