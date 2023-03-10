package com.example.javamiaosha.dto;

import com.example.generator.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetailHtmlDto {

    private User user;
    private GoodsDto goodsDto;
    private int secKillStatus;
    private int remainSeconds;

}
