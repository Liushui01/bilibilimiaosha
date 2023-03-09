package com.example.javamiaosha;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.generator.mapper.SeckillOrderMapper;
import com.example.generator.pojo.SeckillOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavamiaoshaApplicationTests {

    @Autowired
    SeckillOrderMapper seckillOrderMapper;
    @Test
    void contextLoads() {
        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setOrderId(3L);
        seckillOrderMapper.update(null,new UpdateWrapper<SeckillOrder>().set("goods_id",7L)
                .eq("user_id",1L));
    }

}
