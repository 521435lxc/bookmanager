package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.OrderForm;


/**
 * (OrderForm)表服务接口
 *
 * @author makejava
 * @since 2024-01-24 22:21:18
 */
public interface OrderFormService extends IService<OrderForm> {

    Result addOrderForm(OrderForm orderForm);

    Result selectAllOrderForm();
}

