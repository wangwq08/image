package com.piccfsit.image.exception;

import com.piccfsit.image.domain.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value=MultipartException.class)
    @ResponseBody
    public Image handleError1(MultipartException e) throws Exception {

        Image image = new Image();
        image.setCode(0);
        image.setSuccess(false);
        image.setData(null);
        image.setMessage(e.getMessage());
        this.logger.debug("异常信息  [{}]",image.getMessage());
        return image;
    }


}