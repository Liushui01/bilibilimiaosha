package com.example.javamiaosha.service.impl;

import com.example.generator.pojo.Goods;
import com.example.generator.mapper.GoodsMapper;
import com.example.javamiaosha.dao.GoodsDtoDao;
import com.example.javamiaosha.dto.GoodsDto;
import com.example.javamiaosha.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-07
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    GoodsDtoDao goodsDtoDao;

    /**
     * 获取秒杀商品列表
     * @return
     */
    @Override
    public List<GoodsDto> findGoodsDto() {
        List<GoodsDto> goodsList = goodsDtoDao.finGoodsDto();
        return goodsList;
    }

    @Override
    public GoodsDto findGoodsDtoByGoodsId(Long goodsId) {
        return goodsDtoDao.findGoodsDtoByGoodsId(goodsId);
    }
}
