package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.OrderForm;
import com.example.service.OrderFormService;
import com.example.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (OrderForm)表控制层
 *
 * @author makejava
 * @since 2024-01-24 21:22:34
 */
@RestController
@RequestMapping("/orderForm")
public class OrderFormController {
    /**
     * 服务对象
     */
    @Resource
    private OrderFormService orderFormService;

    // 新增征订单
    @PostMapping("/addOrderForm")
    public Result addOrderForm(@RequestBody OrderForm orderForm){
        Account currentUser = TokenUtils.getCurrentUser();
        orderForm.setApplicantId(currentUser.getUserId());
        return orderFormService.addOrderForm(orderForm);
    }
    // 查询
    @GetMapping("/selectAll")
    public Result selectAllOrderForm(){

        return orderFormService.selectAllOrderForm();
    }


    // 分页查询


}

