package com.example.javamiaosha.config;

import com.example.generator.pojo.User;
import com.example.javamiaosha.service.IUserService;
import com.example.javamiaosha.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    IUserService iUserService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz=parameter.getParameterType();
        return clazz== User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request=webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response=webRequest.getNativeResponse(HttpServletResponse.class);
        log.error("{}","来到了判断");
        String ticket=CookieUtil.getCookieValue(request,"userTicket");
        log.error("{}","验证模块里的cookie值为："+ticket);
        if(StringUtils.isEmpty(ticket)){
            log.error("{}","tick为空");
            return null;
        }
        User user=iUserService.getUserByCookie(ticket,request,response);
        if(user==null){
            log.error("{}","没有找到用户");
            return null;
        }
        return user;
    }
}
