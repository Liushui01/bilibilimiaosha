package com.example.javamiaosha.exception;

import com.example.javamiaosha.dto.RespBean;
import com.example.javamiaosha.dto.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public RespBean bindExceptionHandler(Exception e){
        if(e instanceof BindException){
            BindException ex=(BindException) e;
            RespBean respBean=RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage(respBean.getMessage()+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
    @ExceptionHandler(GlobalException.class)
    public RespBean globalExceptionHandler(Exception e){
        if(e instanceof GlobalException){
            GlobalException ex=(GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }

}
