package com.example.javamiaosha.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.example.generator.pojo.Goods;
import com.example.generator.pojo.Order;
import com.example.generator.pojo.SeckillOrder;
import com.example.generator.pojo.User;
import com.example.javamiaosha.dto.GoodsDto;
import com.example.javamiaosha.dto.RespBean;
import com.example.javamiaosha.dto.RespBeanEnum;
import com.example.javamiaosha.service.IGoodsService;
import com.example.javamiaosha.service.IOrderService;
import com.example.javamiaosha.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    IGoodsService iGoodsService;
    @Autowired
    ISeckillOrderService iSeckillOrderService;
    @Autowired
    IOrderService iOrderService;

    @PostMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId){
        GoodsDto goodsDto = iGoodsService.findGoodsDtoByGoodsId(goodsId);
        if(goodsDto.getStockCount()<1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillOrder seckillOrder = iSeckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).
                eq("goods_id", goodsId));
        if(seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order= iOrderService.seckill(user,goodsDto);
        return RespBean.success(order);
    }
}
