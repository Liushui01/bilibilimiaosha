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
import com.example.javamiaosha.rabbitmq.MQSend;
import com.example.javamiaosha.service.IGoodsService;
import com.example.javamiaosha.service.IOrderService;
import com.example.javamiaosha.service.ISeckillOrderService;
import com.example.javamiaosha.utils.JsonUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //内存标记，标记秒杀商品是否还有库存
    private Map<Long,Boolean>  EmptyStockMap=new HashMap<>();

    @PostMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId){
        GoodsDto goodsDto = iGoodsService.findGoodsDtoByGoodsId(goodsId);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get("user:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记,减少Redis的访问
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        Long decrement = valueOperations.decrement("seckillGoods:" + goodsId);
        if(decrement<0){
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
    public RespBean getResult(User user,Long goodsId){
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("user:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return RespBean.success(seckillOrder.getOrderId());
        }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK,-1L);
        }
        return RespBean.success(0L);
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
