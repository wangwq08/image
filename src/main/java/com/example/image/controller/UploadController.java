package com.example.image.controller;

import com.example.image.domain.DBUtil;
import com.example.image.domain.Image;
import com.example.image.reduce.Reduce;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    @RequestMapping("upload")
    @ResponseBody
    public Image upload(@RequestParam("fileName") MultipartFile file) throws IOException{

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

        //图片名称
        String fileName = file.getOriginalFilename();

        String temp[] = fileName.replaceAll("\\\\","/").split("/");   //路径名和文件名分割，获取文件名
        if (temp.length > 1) {
            fileName = temp[temp.length - 1];
        }
        System.out.println(fileName);
        String newFilename = UUID.randomUUID().toString();
        System.out.println(newFilename);

        //保存路径
        String path = "D:/image";
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
        File repath=new File(path+"/"+"re"+fileName);
        Reduce reduce =new Reduce();
        reduce.reduceImg(dest.toString(),repath.toString(),600,400,0.5f);              //压缩图片
        String imagepath = repath.toString();  //数据库存储路径
        image= Write(fileName,newFilename,imagepath);
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
}