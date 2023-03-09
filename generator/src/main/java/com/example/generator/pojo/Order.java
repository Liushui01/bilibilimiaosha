package com.example.generator.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-03-07
 */
@Getter
@Setter
@TableName("t_order")
@ApiModel(value = "Order对象", description = "")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("收获地址ID")
    private Long deliveryAddrId;

    @ApiModelProperty("商品名字")
    private String goodsName;

    @ApiModelProperty("商品数量")
    private Integer goodsCount;

    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;

    @ApiModelProperty("1 pc,2 android, 3 ios")
    private Integer orderChannel;

    @ApiModelProperty("订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退货，5已完成")
    private Integer status;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("订单创建时间")
    private LocalDateTime createDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty("支付时间")
    private LocalDateTime payDate;


}
