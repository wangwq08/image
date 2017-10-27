package com.example.image.controller;

import com.example.image.domain.DBUtil;
import com.example.image.exception.MyException;
import com.example.image.reduce.Reduce;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 实现根据id读取图片
 * @author wangwq
 * @date 2017.9.26 11:51
 */

@RestController
public class GetController {

    public static String ypath="D:/test";
    public static String cpath="D:/image";
    public static String tpath="D:/thumb";

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)          //获取 裁剪图
//    @ResponseBody
    public byte[] ReadImage(@PathVariable("id") String id) throws IOException{
//        String path=RealPath(id);        //根据ID读取路径
        String path=cpath+"/"+id;
        FileInputStream fs = new FileInputStream(path);   //读取本地绝对路径
        View(id);
        System.out.println(fs);
        return IOUtils.toByteArray(fs);
    }

    @GetMapping(value = "/thumb/{id}", produces = MediaType.IMAGE_JPEG_VALUE)          //获取 缩略图
//    @ResponseBody
    public byte[] ReadThumbImage(@PathVariable("id") String id) throws IOException{
//        String path=RealPath(id);        //根据ID读取路径
        String  path=tpath+"/"+id;
        FileInputStream fs = new FileInputStream(path);   //读取本地绝对路径
        View(id);                  //记录访问次数
        System.out.println(fs);
        return IOUtils.toByteArray(fs);
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
