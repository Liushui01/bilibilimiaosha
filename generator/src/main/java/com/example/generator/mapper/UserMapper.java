package com.example.generator.mapper;

import com.example.generator.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-03-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
