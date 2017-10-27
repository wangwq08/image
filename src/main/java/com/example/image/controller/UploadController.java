package com.example.image.controller;

import com.example.image.domain.DBUtil;
import com.example.image.domain.Image;
import com.example.image.reduce.Reduce;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 实现图片的上传及保存
 * @author wangwq
 * @date 2017.9.26 11:51
 */

@Controller
public class UploadController {

    /**
     * 实现文件上传
     * @return code：1  success:true  data:图片ID  message:上传成功
     */

    public static int  width=1024;     //裁剪图宽度设置
    public static int height;

    public static int thumbwidth=100;  //缩略图宽度设置
    public static int thumbheight;

    @RequestMapping("upload")
    @ResponseBody
    public Image upload(@RequestParam("filename") MultipartFile file) throws IOException{



        // /返回信息
        Image image = new Image();
        image.setCode(0);
        image.setSuccess(false);
        image.setData(null);
        image.setMessage("上传失败，请重新上传");

        if (file.isEmpty()) {
            image.setMessage("图片为空，请重新上传");
            return image;
        }

        if(!file.getContentType().substring(0,5).equals("image"))         //判断是不是图片文件
        {
            image.setMessage("不是图片文件，请重新上传");
            return image;
        }

        if(file.getSize()/1024>5120){                                    //判断是否超过最大值5M
            image.setMessage("超过大小，请选择小于5M的图片上传");
            return image;
        }

        System.out.println(file.getSize());
        System.out.println(file.getSize()/1024);


        //图片名称
        String fileName = file.getOriginalFilename();

        String temp[] = fileName.replaceAll("\\\\","/").split("/");   //路径名和文件名分割，获取文件名
        if (temp.length > 1) {
            fileName = temp[temp.length - 1];
        }

        String newFilename = UUID.randomUUID().toString();
        fileName=newFilename;
        System.out.println("重命名"+ fileName);
        System.out.println("图片ID"+ newFilename);

        //保存路径
        String path = "D:/Test";
        File dest = new File(path + "/" + fileName);
//        String imagepath = repath.toString();  //数据库存储路径
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            System.out.println("保存到本地成功：" + dest);
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
//
//        String cpath="D:/image";
//
//        File repath=new File(path+"/"+fileName);
//        Reduce reduce =new Reduce();
//
//        float ratio=reduce.getRadio(dest.toString());    //获取原图比例
//        System.out.println("获取原图比例" + ratio);
//        height=(int)(width/ratio);
//        System.out.println("裁剪后的尺寸 "+width +height);
//
//        reduce.reduceImg(dest.toString(),repath.toString(),width,height,null);                      //裁剪图片 reduce.ratio为原图比例
//        String imagepath = repath.toString();  //数据库存储路径
//        image= Write(fileName,newFilename,imagepath);
        Reduce reduce=new Reduce();
        float ratio=reduce.getRadio(dest.toString());    //获取原图比例

        //裁剪图路径
        String cpath="D:/image";
        File crepath=new File(cpath+"/"+fileName);
        height=(int)(width/ratio);

        reduce.reduceImg(dest.toString(),crepath.toString(),width,height,null);
        String imagepath=crepath.toString();      //数据库存储路径
        image=Write1(fileName,newFilename,imagepath);
        System.out.println("裁剪图存储成功");



        //缩略图路径
        String tpath = "D:/thumb";
        File trepath=new File(tpath+"/"+fileName);
        String tnewFilename=newFilename+"1";
        thumbheight=(int)(thumbwidth/ratio);

        reduce.reduceImg(dest.toString(),trepath.toString(),thumbwidth,thumbheight,null);              //缩略图片
        String timagepath = trepath.toString();  //数据库存储路径
        //Write(fileName,tnewFilename,timagepath);
        System.out.println("缩略图存储成功");

        return image;
    }

    //将图片路径保存到数据库中
    public static Image Write(String fileName,String newFilename,String imagepath){

        //将图片存到数据库中
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConn();
            String sql = "insert into image (id,filename,newfilename,path)values(?,?,?,?) ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, fileName);
            ps.setString(3, newFilename);
            ps.setString(4, imagepath);
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("插入成功！！");
            } else {
                System.out.println("插入失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        Image image =new Image();
        image.setCode(1);
        image.setSuccess(true);
        image.setData(newFilename);
        image.setMessage("上传成功！");
        return image;
    }

    //将图片ID保存到数据库：图片ID 上传时间  浏览次数  用户token
    public static Image Write1(String fileName,String newFilename,String imagepath){

        //将图片存到数据库中

        Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);


        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConn();
            String sql = "insert into imageinfo (id,imageid,uploadtime,viewtimes,token)values(?,?,?,?,?) ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, fileName);
            ps.setTimestamp(3, goodsC_date);
            ps.setInt(4, 0);
            ps.setString(5,null);
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("插入成功！！");
            } else {
                System.out.println("插入失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        Image image =new Image();
        image.setCode(1);
        image.setSuccess(true);
        image.setData(newFilename);
        image.setMessage("上传成功！");
        return image;
    }
}