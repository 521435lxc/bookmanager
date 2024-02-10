package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.OrderForm;
import com.example.entity.Textbook;
import com.example.entity.vo.OrderFormVo;
import com.example.service.OrderFormService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import com.sun.deploy.net.URLEncoder;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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
    public Result selectAllOrderForm(OrderForm orderForm){
        return orderFormService.selectAllOrderForm(orderForm);
    }

    // 分页查询
    @GetMapping("/selectPage")
    public Result selectPage(OrderForm orderForm,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<OrderForm> page = orderFormService.selectPage(orderForm, pageNum, pageSize);
        return Result.success(page);
    }

    //  是否取消状态
    @PutMapping("/cancelStatus")
    public Result cancelOrderForm(@RequestBody OrderForm orderForm){

        return orderFormService.cancelOrderForm(orderForm);
    }
    @PostMapping("/approveOrder")
    public Result approveOrder(@RequestBody OrderForm orderForm,@RequestParam Integer roleId){
        return  orderFormService.approveOrder(orderForm,roleId);
    }

    // 批量导出征订单
    @GetMapping("/exportBatch")
    public void exportBatch(@RequestParam String orderFormIdList, HttpServletResponse response) throws IOException {

        ExcelWriter writer = ExcelUtil.getWriter(true);

        List<OrderForm> list = null;
        LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(orderFormIdList)) {
            List<Integer> ids = Arrays.stream(orderFormIdList.split(",")).map(Integer::valueOf).collect(Collectors.toList());
//            queryWrapper.in(OrderForm::getOrderFormId, ids);
             list = orderFormService.selectAllByIds(ids);
        }
//        list = orderFormService.list(queryWrapper);  
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("征订单表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();

        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();


    }


}

