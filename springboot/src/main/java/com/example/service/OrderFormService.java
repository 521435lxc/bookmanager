package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.OrderForm;
import com.example.entity.vo.OrderFormVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * (OrderForm)表服务接口
 *
 * @author makejava
 * @since 2024-01-24 22:21:18
 */
public interface OrderFormService extends IService<OrderForm> {

    Result addOrderForm(OrderForm orderForm);

    Result selectAllOrderForm(OrderForm orderForm);

    PageInfo<OrderForm> selectPage(OrderForm orderForm, Integer pageNum, Integer pageSize);

    Result cancelOrderForm(OrderForm orderForm);

    Result approveOrder(OrderForm orderForm,Integer roleId);

    void exportBatchByIds(String orderFormIdList, HttpServletResponse response) throws IOException;

    List<OrderForm> selectAllByIds(List<Integer> ids);
}

