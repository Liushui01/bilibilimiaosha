package com.example.javamiaosha.rabbitmq;

import com.example.javamiaosha.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * rabbitmq信息发送类
 */
@Service
@Slf4j
public class MQSend {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送秒杀信息
     * @param message
     */
    public void sendSeckillMeaasge(String message){
        log.info("发送信息"+message);
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE,"seckill.message",message);
    }
}
