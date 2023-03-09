package com.example.javamiaosha.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.example.generator.pojo.Goods;
import com.example.generator.pojo.Order;
import com.example.generator.pojo.SeckillOrder;
import com.example.generator.pojo.User;
import com.example.javamiaosha.dto.GoodsDto;
import com.example.javamiaosha.dto.RespBeanEnum;
import com.example.javamiaosha.service.IGoodsService;
import com.example.javamiaosha.service.IOrderService;
import com.example.javamiaosha.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    IGoodsService iGoodsService;
    @Autowired
    ISeckillOrderService iSeckillOrderService;
    @Autowired
    IOrderService iOrderService;

    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user,Long goodsId){
        model.addAttribute("user",user);
        GoodsDto goodsDto = iGoodsService.findGoodsDtoByGoodsId(goodsId);
        if(goodsDto.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        SeckillOrder seckillOrder = iSeckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).
                eq("goods_id", goodsId));
        if(seckillOrder!=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        Order order= iOrderService.seckill(user,goodsDto);
        model.addAttribute("order",order);
        model.addAttribute("goods",goodsDto);
        return "orderDetail";
    }
}
