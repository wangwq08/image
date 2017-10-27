package com.example.image.exception;

import com.example.image.domain.Image;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public Image jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
        Image image = new Image();
        image.setCode(0);
        image.setSuccess(false);
        image.setData(null);
        image.setMessage("超过图片最大限制，请重新上传");
        return image;
    }
}
