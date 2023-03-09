package com.example.javamiaosha.dao;

import com.example.javamiaosha.dto.GoodsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface GoodsDtoDao {

     List<GoodsDto> finGoodsDto();

     GoodsDto findGoodsDtoByGoodsId(Long goodsId);
}
