package com.example.image.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void create(int id, String imageid, Timestamp uploadtime, int viewtimes, String token){
        jdbcTemplate.update("insert into imageinfo(id,imageid,uploadtime,viewtimes,token) values(?,?,?,?,?)",id,imageid,uploadtime,viewtimes,token);
    }

    @Override
    public void updateViewTimes(int viewtimes,String imageid){
        jdbcTemplate.update("update imageinfo set viewtimes='\"+viewtimes+\"' where imageid ='\"+imageid+\"';");
    }

    @Override
    public int getImage(String imageid){
        return jdbcTemplate.update("select viewtimes from imageinfo where imageid ='\"+imageid+\"';");
    }


}
