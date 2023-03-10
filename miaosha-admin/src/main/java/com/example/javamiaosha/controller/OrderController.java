package com.example.javamiaosha.controller;

import com.example.generator.pojo.User;
import com.example.javamiaosha.dto.OrderHtmlDto;
import com.example.javamiaosha.dto.RespBean;
import com.example.javamiaosha.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-03-07
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    IOrderService iOrderService;
    @RequestMapping("/detail")
    public RespBean detail(User user,Long orderId){
        OrderHtmlDto orderHtmlDto = iOrderService.detail(orderId);
        orderHtmlDto.setUser(user);
        return RespBean.success(orderHtmlDto);
    }

}
