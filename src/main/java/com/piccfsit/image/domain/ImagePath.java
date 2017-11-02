package com.piccfsit.image.domain;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imagepath")
public class ImagePath {

    private String path;
    private String cpath;
    private String tpath;
    private String tarPath;


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
    public  String getTarPath(){
        return tarPath;
    }
}
