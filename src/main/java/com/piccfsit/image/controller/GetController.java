package com.piccfsit.image.controller;

import com.piccfsit.image.config.ISConfiguration;
import com.piccfsit.image.exception.MyException;

import com.piccfsit.image.service.ImageServiceImpl;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 实现图片获取：裁剪图 缩略图 文件打包
 * @author wangwq
 * @date 2017.11.3 11:51
 */

@RestController
public class GetController {

    @Autowired
    private ISConfiguration isconfig;

    @Autowired
    private ImageServiceImpl imageService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)          //获取 裁剪图
    public byte[] ReadImage(@PathVariable("id") String id) throws IOException{

        String cpath=isconfig.ip.getCpath();
        String path=cpath+"/"+id;
        FileInputStream fs = new FileInputStream(path);   //读取本地绝对路径
        View(id);
        this.logger.info("获取裁剪图成功");
        return IOUtils.toByteArray(fs);
    }

    @GetMapping(value = "/thumb/{id}", produces = MediaType.IMAGE_JPEG_VALUE)          //获取 缩略图
    public byte[] ReadThumbImage(@PathVariable("id") String id) throws IOException{

        String tpath=isconfig.ip.getTpath();
        String  path=tpath+"/"+id;
        FileInputStream fs = new FileInputStream(path);   //读取本地绝对路径
        View(id);                  //记录访问次数
        this.logger.info("获取缩略图成功");
        return IOUtils.toByteArray(fs);
    }

    @PostMapping(value = "zip")                                                       //打包图片，返回压缩包images.zip
    public void downloadZipFile(@RequestParam("ids") String ids, HttpServletResponse response) throws IOException {

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

    @RequestMapping("/json")                   //异常测试
    public String json() throws MyException{
        throw new MyException("发生错误2");
    }

    //更新数据库访问次数
    private void View(String imageid){

        this.imageService.updateViewTimes(imageid);
        this.logger.info("访问数据库成功 ");

    }
}
