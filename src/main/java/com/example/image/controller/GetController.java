package com.example.image.controller;

import com.example.image.domain.DBUtil;
import com.example.image.exception.MyException;
import com.example.image.reduce.DownLoad;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 实现图片获取：原图、裁剪图、缩略图 文件打包
 * @author wangwq
 * @date 2017.9.26 11:51
 */

@RestController
public class GetController {

    public static String ypath="D:/test";        //原图存放路径
    public static String cpath="D:/image";       //裁剪图存放路径
    public static String tpath="D:/thumb";       //缩略图路径

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)          //获取 裁剪图

    public byte[] ReadImage(@PathVariable("id") String id) throws IOException{
//        String path=RealPath(id);        //根据ID读取路径
        String path=cpath+"/"+id;
        FileInputStream fs = new FileInputStream(path);   //读取本地绝对路径
        View(id);
        System.out.println(fs);
        return IOUtils.toByteArray(fs);
    }

    @GetMapping(value = "/thumb/{id}", produces = MediaType.IMAGE_JPEG_VALUE)          //获取 缩略图

    public byte[] ReadThumbImage(@PathVariable("id") String id) throws IOException{
//        String path=RealPath(id);        //根据ID读取路径
        String  path=tpath+"/"+id;
        FileInputStream fs = new FileInputStream(path);   //读取本地绝对路径
        View(id);                  //记录访问次数
        System.out.println(fs);
        return IOUtils.toByteArray(fs);
    }

    @GetMapping(value = "/zipimage/{ids}", produces ="File/zip")           //图片打包 返回流

    public byte[] ReadZipImage(@PathVariable("ids") String ids) throws IOException{
        DownLoad dl = new DownLoad();       //调用打包压缩
        String tarPath="D:/test1.0.zip";    //打包输出地址
        ArrayList List = new ArrayList();   //需要打包的文件源地址

        String strArray[]=null;
        strArray=ids.split(",");            //ids转化为数组

        for(int i=0;i<strArray.length;i++)         //需要打包文件的地址
        {
            List.add(cpath+"/"+ strArray[i]);
        }
        String[] idpath=(String[])List.toArray(new String[0]);

        System.out.println("开始打包");
        dl.downLoadZIP(tarPath,idpath);
        System.out.println("打包完成");

        FileInputStream fs = new FileInputStream(tarPath);   //读取本地绝对路径

        return IOUtils.toByteArray(fs);
    }

    @PostMapping(value = "zip")                                            //打包图片，返回压缩包images.zip
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

    //数据库查询获取真实路径
    private String RealPath(String newfilename){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        String GetPath=null;
        try{
            conn= DBUtil.getConn();
            String sql="select path from image where newfilename=?";       //通过ID找到图片路径
            ps=conn.prepareStatement(sql);
            ps.setString(1,newfilename);
            rs=ps.executeQuery();   //执行查询
            if(rs.next()){
                GetPath=rs.getString("path");
            }
            System.out.println("查询成功");
            System.out.println(GetPath);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return GetPath;
    }

    //更新数据库访问次数
    private void View(String imageid){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        int times=0;
        try{
            conn= DBUtil.getConn();
            String sql="select viewtimes from imageinfo where imageid=?";       //查询确定ID存在且获得访问次数
            ps=conn.prepareStatement(sql);
            ps.setString(1,imageid);
            rs=ps.executeQuery();   //执行查询
            if(rs.next()){
                times=rs.getInt("viewtimes");
            }
            System.out.println("查询成功");
            System.out.println(times);

            times++;
            String sql2 = "update imageinfo set viewtimes=? where imageid ='"+imageid+"';";
            ps = conn.prepareStatement(sql2);
            ps.setInt(1,times);
            ps.executeUpdate();                   //执行更新

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
