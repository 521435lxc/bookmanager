package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.AuthAccess;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.OrderForm;
import com.example.exception.CustomException;
import com.example.mapper.OrderFormMapper;
import com.example.service.OrderFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * (OrderForm)表服务实现类
 *
 * @author makejava
 * @since 2024-01-24 22:21:19
 */
@Service("orderFormService")
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm> implements OrderFormService {

    @Autowired
    private OrderFormMapper orderFormMapper;
    @Resource
    private OrderFormService orderFormService;

    @Override
    public Result addOrderForm(OrderForm orderForm) {

        // 设置时间 和 征订单初始的状态 和是否取消的状态
        orderForm.setApplicationTime(new Date());
        orderForm.setApplicationStatus(Constants.NO_APPROVAL);
        orderForm.setCancelStatus(Constants.UNCANCEL);
        int insert = orderFormMapper.insert(orderForm);
        if (insert > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }


    // 查询所有的征订单数据
    @Override
    public Result selectAllOrderForm() {

        return null;
    }
}
