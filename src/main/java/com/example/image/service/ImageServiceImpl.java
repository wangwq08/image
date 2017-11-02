package com.example.image.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
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
    public void updateViewTimes(String imageid){
        jdbcTemplate.update("update imageinfo set viewtimes=viewtimes+1 where imageid = ?", imageid);
    }

}
