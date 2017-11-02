package com.example.image.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author wangwq
 * @date 2017.9.26 11:51
 */


public class DBUtil {

    @Autowired
    private static SqlInfo si;

    // 定义数据库连接参数
//    public static final String DRIVER_CLASS_NAME = si.getDRIVER_CLASS_NAME();
//    public static final String URL = si.getURL();
//    public static final String USERNAME = si.getUSERNAME();
//    public static final String PASSWORD = si.getPASSWORD();

    public static final String DRIVER_CLASS_NAME ="com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/test";
    public static final String USERNAME ="root";
    public static final String PASSWORD = "";

    // 注册数据库驱动
    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            System.out.println("注册失败！");
            e.printStackTrace();
        }
    }
    // 获取连接
    public static Connection getConn() throws SQLException {
        System.out.println("连接成功");
        System.out.println(URL+" "+USERNAME+" "+PASSWORD);
        System.out.println("读取配置"+si.getURL());
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // 关闭连接
    public static void closeConn(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("关闭连接失败！");
                e.printStackTrace();
            }
        }
    }
    //测试
    public static void main(String[] args) throws SQLException {
        System.out.println(DBUtil.getConn());
    }

}