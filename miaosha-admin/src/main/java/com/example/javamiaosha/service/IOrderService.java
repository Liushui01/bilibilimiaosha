package com.example.javamiaosha.service;

import com.example.generator.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.generator.pojo.User;
import com.example.javamiaosha.dto.GoodsDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-07
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsDto goodsDto);
}
