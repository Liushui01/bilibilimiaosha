package com.example.javamiaosha.service;

import com.example.generator.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.javamiaosha.dto.GoodsDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-07
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsDto> findGoodsDto();

    GoodsDto findGoodsDtoByGoodsId(Long goodsId);
}
