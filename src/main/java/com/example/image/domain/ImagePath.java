package com.example.image.domain;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "imagepath")
@Component
public class ImagePath {

    private String path;
    private String cpath;
    private String tpath;
    private static String tarPath;


    public void setPath(String path){
        this.path=path;
    }
    public String getPath(){
        return path;
    }

    public void setCpath(String cpath){
        this.cpath=cpath;
    }
    public String getCpath(){
        return cpath;
    }

    public void setTpath(String tpath){
        this.tpath=tpath;
    }
    public String getTpath(){
        return tpath;
    }

    public void setTarPath(String tarPath){
        this.tarPath=tarPath;
    }
    public static String getTarPath(){
        return tarPath;
    }
}
