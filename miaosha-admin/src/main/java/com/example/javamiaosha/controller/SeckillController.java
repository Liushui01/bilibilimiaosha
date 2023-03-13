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
import com.example.javamiaosha.dto.SeckillMessage;
import com.example.javamiaosha.exception.GlobalException;
import com.example.javamiaosha.rabbitmq.MQSend;
import com.example.javamiaosha.service.IGoodsService;
import com.example.javamiaosha.service.IOrderService;
import com.example.javamiaosha.service.ISeckillOrderService;
import com.example.javamiaosha.utils.JsonUtil;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private ISeckillOrderService iSeckillOrderService;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSend mqSend;
    @Autowired
    private RedisScript<Long> redisScript;

    //内存标记，标记秒杀商品是否还有库存
    private Map<Long,Boolean>  EmptyStockMap=new HashMap<>();

    @PostMapping("/{path}/doSeckill")
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId, @PathVariable String path){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //校验请求的path参数
        boolean check=iOrderService.checkPath(user,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get("order:"+user.getId()+":"+goodsId);
        if(seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记,减少Redis的访问
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
//        Long decrement = valueOperations.decrement("seckillGoods:" + goodsId);
        //通过lua脚本预减库存
        Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId),
                Collections.EMPTY_LIST);
        System.out.println(stock);
        if(stock<0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage=new SeckillMessage(user,goodsId);
        mqSend.sendSeckillMeaasge(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping(value = "/result")
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(seckillOrder!=null){
            return RespBean.success(seckillOrder.getOrderId());
        }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK,-1L);
        }
        return RespBean.success(0L);
    }

    /**
     * 获取秒杀接口地址
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping("/path")
    @ResponseBody
    public RespBean path(User user,Long goodsId,String captcha){
        String str=iOrderService.createPath(user,goodsId);
        //校验验证码
        boolean check= iOrderService.checkCaptcha(user,goodsId,captcha);
        if(!check){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        //
        return RespBean.success(str);
    }

    @GetMapping("/captcha")
    public void captcha(User user, Long goodsId, HttpServletResponse response){
        if(goodsId<0){
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        // 设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码，将结果放入redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text(),
                300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败",e.getMessage());
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsDto> goodsDto = iGoodsService.findGoodsDto();
        if(CollectionUtils.isEmpty(goodsDto)){
            return;
        }
        goodsDto.forEach(goodsDto1 -> {
            redisTemplate.opsForValue().set("seckillGoods:"+goodsDto1.getId(),goodsDto1.getStockCount());
            EmptyStockMap.put(goodsDto1.getId(),false);
        });
    }
}
