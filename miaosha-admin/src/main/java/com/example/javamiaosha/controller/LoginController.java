package com.example.javamiaosha.controller;

import com.example.javamiaosha.dto.LoginParam;
import com.example.javamiaosha.service.IUserService;
import com.example.javamiaosha.dto.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * 跳转到登录页面
     */
    @Autowired
    IUserService iUserService;
    @GetMapping("/tologin")
    public String tologin(){
        return "login";
    }

    /**
     * 登录功能
     * @param loginParam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean dologin(@Valid LoginParam loginParam, HttpServletRequest request, HttpServletResponse response){
        log.info("{}", loginParam);
        return iUserService.dologin(loginParam,request,response);
    }
}
