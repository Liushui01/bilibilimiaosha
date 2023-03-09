package com.example.javamiaosha.service;

import com.example.generator.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.javamiaosha.dto.LoginParam;
import com.example.javamiaosha.dto.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-06
 */
public interface IUserService extends IService<User> {

    RespBean dologin(LoginParam loginParam, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userticket,HttpServletRequest request,HttpServletResponse response);
}
