package com.piccfsit.image.service;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {
    public void DownloadZip(String ids, HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader("Content-Disposition","attachment; filename=\"images.zip\"");     //压缩包名称

        String strArray[]=null;
        strArray=ids.split(",");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        for(String fileName : strArray) {
            ZipEntry zipEntry = new ZipEntry(fileName+".jpg");                            //图片名
            zipOutputStream.putNextEntry(zipEntry);
            FileInputStream inputStream = new FileInputStream("D:/image/"+fileName);
            IOUtils.copy(inputStream,zipOutputStream);
            inputStream.close();
        }

        zipOutputStream.closeEntry();
        zipOutputStream.close();
    }
}
