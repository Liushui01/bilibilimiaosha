package com.example.javamiaosha;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.generator.mapper.SeckillOrderMapper;
import com.example.generator.pojo.SeckillOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class JavamiaoshaApplicationTests {

    @Autowired
    SeckillOrderMapper seckillOrderMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("user","user",10, TimeUnit.SECONDS);
    }

}
