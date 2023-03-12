package com.example.javamiaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.generator.mapper.GoodsMapper;
import com.example.generator.mapper.OrderMapper;
import com.example.generator.mapper.SeckillGoodsMapper;
import com.example.generator.mapper.SeckillOrderMapper;
import com.example.generator.pojo.*;
import com.example.javamiaosha.dao.GoodsDtoDao;
import com.example.javamiaosha.dto.GoodsDto;
import com.example.javamiaosha.dto.OrderHtmlDto;
import com.example.javamiaosha.dto.RespBean;
import com.example.javamiaosha.dto.RespBeanEnum;
import com.example.javamiaosha.exception.GlobalException;
import com.example.javamiaosha.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    SeckillOrderMapper seckillOrderMapper;
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    GoodsDtoDao goodsDtoDao;
    @Autowired
    RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Order seckill(User user, GoodsDto goodsDto) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //修改秒杀库存
        int update = seckillGoodsMapper.update(null, new UpdateWrapper<SeckillGoods>().setSql("stock_count=stock_count-1")
                .eq("goods_id", goodsDto.getId()).gt("stock_count", 0));
        //修改商品总库存
        if(update>0){
            int update1 = goodsMapper.update(null, new UpdateWrapper<Goods>().setSql("goods_stock=goods_stock-1")
                .eq("id", goodsDto.getId()).gt("goods_stock",0));
            if(!(update1>0)){
                valueOperations.set("isStockEmpty:"+goodsDto.getId(),"0");
                return null;
            }
        }else {
            valueOperations.set("isStockEmpty:"+goodsDto.getId(),"0");
            return null;
        }
        //添加订单
        Order order = insertOrder(user, goodsDto);

        return order;
    }

    @Override
    public OrderHtmlDto detail(Long orderId) {
        if(orderId==null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsDto goodsDto = goodsDtoDao.findGoodsDtoByGoodsId(order.getGoodsId());
        OrderHtmlDto orderHtmlDto=new OrderHtmlDto(order,goodsDto);
        return orderHtmlDto;
    }

    public Order insertOrder(User user,GoodsDto goodsDto){
        //添加订单
        Order order=new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsDto.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsDto.getGoodsName());
        order.setGoodsCount(goodsDto.getGoodsStock());
        order.setGoodsPrice(goodsDto.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(LocalDateTime.now());
        orderMapper.insert(order);
        //添加秒杀订单
        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsDto.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderMapper.insert(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goodsDto.getId(),seckillOrder);
        return order;
    }
}
