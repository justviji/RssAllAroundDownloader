package com.example.downloaderinfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownload {


    String getYtId(String originalLink){
        int cutPointId = 0;
        for (Character c : originalLink.toCharArray()){
            if (!(c == '=')){
                cutPointId ++;
            }else {
                break;
            }


        }

        return originalLink.substring(cutPointId);
    }

    public static void dowloadFromYt(){ //goes against the yt guidelines!
        //seach for url_encoded_fmt_stream_map

    }




    public static void downloadFile(String fileUrl, File saveFilePath) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpConn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("Server returned HTTP response code: " + responseCode);
        }
        httpConn.disconnect();

    }
}
