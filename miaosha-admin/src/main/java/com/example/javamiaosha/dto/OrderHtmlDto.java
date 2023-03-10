package com.example.javamiaosha.dto;

import com.example.generator.pojo.Order;
import com.example.generator.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderHtmlDto {

    private User user;
    private Order order;
    private GoodsDto goodsDto;
}
