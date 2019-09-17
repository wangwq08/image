package com.piccfsit.image.controller;

import com.piccfsit.image.config.ISConfiguration;
import com.piccfsit.image.domain.Image;
import com.piccfsit.image.service.ImageServiceImpl;
import com.piccfsit.image.service.ReduceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 实现图片的上传及保存：原图、裁剪图、缩略图
 * @author wangwq
 * @date 2017.9.26 11:51
 * @return code：1  success:true  data:图片ID  message:上传成功
 */

@Controller
public class UploadController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISConfiguration isconfig;

    @Autowired
    private ImageServiceImpl imageService;

    @Autowired
    private ReduceService reduceService;


    @RequestMapping("upload")                                           //图片上传
    @ResponseBody
    public Image upload(@RequestParam("filename") MultipartFile file) throws IOException {
        // 返回信息
        Image image = new Image(0,false,null,"请重新上传");

        if (file.isEmpty()) {
            image.setMessage("图片为空，请重新上传");
            return image;
        }

        if(!file.getContentType().substring(0,5).equals("image"))
        {
            image.setMessage("不是图片文件，请重新上传");
            return image;
        }

        if(file.getSize()/1024>5120){
            image.setMessage("超过大小，请选择小于5M的图片上传");
            return image;
        }
        this.logger.debug("文件大小   [{}]KB",file.getSize()/1024);


        String fileName=UUID.randomUUID().toString();
        this.logger.debug("生成图片ID  [{}]",fileName);

        //原图路径
        String path=isconfig.ip.getPath();
        File dest = new File(path + "/" + fileName);

        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            this.logger.debug("保存到本地成功： [{}]",dest);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            image.setMessage("无效地址，请重新上传");
            return image;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            image.setMessage("路径错误，请重新上传");
            return image;
        }

        float ratio=reduceService.getRadio(dest.toString());    //获取原图比例

        //裁剪图路径
        String cpath=isconfig.ip.getCpath();
        File crepath=new File(cpath+"/"+fileName);
        int width=isconfig.is.getWidth();
        int height=(int)(width/ratio);
        reduceService.reduceImg(dest.toString(),crepath.toString(),width,height,null);                        //裁剪图片
        this.logger.info("裁剪图存储成功");

        //缩略图路径
        String tpath=isconfig.ip.getTpath();
        File trepath=new File(tpath+"/"+fileName);
        int thumbwidth=isconfig.is.getThumbwidth();
        int thumbheight=(int)(thumbwidth/ratio);
        reduceService.reduceImg(dest.toString(),trepath.toString(),thumbwidth,thumbheight,null);              //缩略图片
        this.logger.info("缩略图存储成功");

        image=Write(fileName);                         //将图片ID写入数据库
        return image;
    }

    //将图片ID保存到数据库：图片ID 上传时间  浏览次数  用户token
    public  Image Write(String fileName){

        Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);

        this.imageService.create(0,fileName,goodsC_date,0,null);
        return new Image(1,true,fileName,"上传成功");
    }
}