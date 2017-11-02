package com.example.image.service;

import java.sql.Timestamp;

public interface ImageService {

    /**
     * 插入图片
     * id  imageid uploadtime viewtime token
     */
    void create(int id, String imageid, Timestamp uploadtime,int viewtimes,String token);

    /**
     * 记录浏览次数
     * viewtimes 增加
     */
    void updateViewTimes(int viewtimes,String imageid);

    /**
     * 查询
     * 验证请求ID是否存在
     */
    int  getImage(String imageid);

}
