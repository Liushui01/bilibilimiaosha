package com.example.javamiaosha.interceptor;

import com.example.generator.pojo.User;
import com.example.javamiaosha.service.IUserService;
import com.example.javamiaosha.service.impl.UserServiceImpl;
import com.example.javamiaosha.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if(StringUtils.isEmpty(ticket)){
            request.getRequestDispatcher("/login/tologin").forward(request,response);
            return false;
        }
        User user = new UserServiceImpl().getUserByCookie(ticket,request,response);
        if(user==null){
            request.getRequestDispatcher("/login/tologin").forward(request,response);
            return false;
        }
        return true;
    }
}
