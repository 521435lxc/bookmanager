package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.OrderForm;

import java.util.List;


/**
 * (OrderForm)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-24 22:21:17
 */
public interface OrderFormMapper extends BaseMapper<OrderForm> {
    List<OrderForm> selectAll(OrderForm orderForm);
}

