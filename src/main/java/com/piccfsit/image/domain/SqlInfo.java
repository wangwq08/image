package com.piccfsit.image.domain;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "datasource")
public class SqlInfo {
    String DRIVER_CLASS_NAME;
    String URL;
    String USERNAME;
    String PASSWORD;

    public void setDRIVER_CLASS_NAME(String DRIVER_CLASS_NAME) {
        this.DRIVER_CLASS_NAME = DRIVER_CLASS_NAME;
    }
    public String getDRIVER_CLASS_NAME() {
        return DRIVER_CLASS_NAME;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    public String getURL() {
        return URL;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }
    public String getUSERNAME() {
        return USERNAME;
    }



}
