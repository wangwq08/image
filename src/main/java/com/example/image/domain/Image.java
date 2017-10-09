package com.example.image.domain;

/**
 * @author wangwq
 * @date 2017.9.26 11:51
 */
public class Image {
    int code;   //1 为正常
    boolean success; //标志是否正确
    String data;  //图片ID
    String message; //提示信息

    public void setCode(int code){
        this.code=code;
    }
    public int getCode(){
        return code;
    }

    public void setSuccess(boolean success){
        this.success=success;
    }
    public boolean getSuccess(){
        return success;
    }

    public void setData(String data){
        this.data=data;
    }
    public String getData(){
        return data;
    }

    public void setMessage(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }


}
