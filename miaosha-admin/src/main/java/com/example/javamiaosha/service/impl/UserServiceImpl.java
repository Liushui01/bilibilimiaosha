package com.example.javamiaosha.service.impl;


import com.example.generator.pojo.User;
import com.example.generator.mapper.UserMapper;
import com.example.javamiaosha.dto.LoginParam;
import com.example.javamiaosha.dto.RespBean;
import com.example.javamiaosha.dto.RespBeanEnum;
import com.example.javamiaosha.exception.GlobalException;
import com.example.javamiaosha.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.javamiaosha.utils.CookieUtil;
import com.example.javamiaosha.utils.MD5Utils;
import com.example.javamiaosha.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-06
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean dologin(LoginParam loginParam, HttpServletRequest request, HttpServletResponse response) {
        String mobile=loginParam.getMobile();
        String password=loginParam.getPassword();
        User user = userMapper.selectById(mobile);
        if(user==null){
            throw new GlobalException(RespBeanEnum.USER_NULL);
        }

        if(!MD5Utils.fromPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket= UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket,user,1800, TimeUnit.SECONDS);
        log.error("{}","cookie的值为："+ticket);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);

    }

    @Override
    public User getUserByCookie(String ticket,HttpServletRequest request,HttpServletResponse response) {
        log.error("{}","getUser方法里的cookie为："+ticket);
        User user = (User) redisTemplate.opsForValue().get("user:"+ticket);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",ticket);
        }
        return user;
    }
}
