package com.example.javamiaosha.rabbitmq;

import com.example.generator.pojo.Order;
import com.example.generator.pojo.SeckillOrder;
import com.example.generator.pojo.User;
import com.example.javamiaosha.config.RabbitMqConfig;
import com.example.javamiaosha.dto.GoodsDto;
import com.example.javamiaosha.dto.RespBean;
import com.example.javamiaosha.dto.RespBeanEnum;
import com.example.javamiaosha.dto.SeckillMessage;
import com.example.javamiaosha.service.IGoodsService;
import com.example.javamiaosha.service.IOrderService;
import com.example.javamiaosha.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService iOrderService;

    @RabbitListener(queues = RabbitMqConfig.QUEUE)
    public void seckillReceive(String message){
        log.info("接收到的消息:"+message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        User user = seckillMessage.getUser();
        Long goodId = seckillMessage.getGoodId();
        //判断库存
        GoodsDto goodsDto = iGoodsService.findGoodsDtoByGoodsId(goodId);
        if(goodsDto.getStockCount()<1){
            return;
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodId);
        if(seckillOrder!=null){
            return;
        }
        iOrderService.seckill(user, goodsDto);

    }
}
