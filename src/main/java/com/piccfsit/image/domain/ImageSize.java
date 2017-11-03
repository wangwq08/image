package com.piccfsit.image.domain;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imagesize")
public class ImageSize {

    private int width;
    private int thumbwidth;

    public void setWidth(int width){
        this.width=width;
    }
    public int  getWidth(){
        return width;
    }

    public void setThumbwidth(int thumbwidth){
        this.thumbwidth=thumbwidth;
    }
    public int  getThumbwidth(){
        return thumbwidth;
    }


}
