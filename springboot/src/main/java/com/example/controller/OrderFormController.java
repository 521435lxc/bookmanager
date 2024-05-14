package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.OrderForm;
import com.example.entity.vo.OrderFormVo;
import com.example.mapper.OrderFormMapper;
import com.example.service.OrderFormService;
import com.example.utils.BeanCopyUtils;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import java.net.URLEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @Resource
    private OrderFormMapper orderFormMapper;

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
    // 审批
    @PostMapping("/approveOrder")
    public Result approveOrder(@RequestBody OrderForm orderForm,@RequestParam Integer roleId){
        return  orderFormService.approveOrder(orderForm,roleId);
    }

    // 批量导出征订单
    @GetMapping("/exportBatch")
    public void exportBatch(@RequestParam String orderFormIdList, HttpServletResponse response) throws IOException {

        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<OrderForm> list = null;
        List<OrderFormVo> OrderFormVoList = null;
        if (StrUtil.isNotBlank(orderFormIdList)) {
            List<Integer> ids = Arrays.stream(orderFormIdList.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            list = orderFormService.selectAllByIds(ids);
            OrderFormVoList = BeanCopyUtils.copyBeanList(list, OrderFormVo.class);
            OrderFormVoList.stream().forEach(item->item.setApplicationStatus("通过"));
        }

        // 稍微设置一下时间
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        writer.write(OrderFormVoList, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("征订单_" + now, "UTF-8") + ".xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();

    }

    @GetMapping("/exportOne")
    public void exportOne(@RequestParam int orderFormId, HttpServletResponse response) throws IOException {

        ExcelWriter writer = ExcelUtil.getWriter(true);

        List<Integer> idList = new ArrayList<>();
        idList.add(orderFormId);
        List<OrderForm> orderForm = orderFormService.selectAllByIds(idList);

        List<OrderFormVo> OrderFormVoList = BeanCopyUtils.copyBeanList(orderForm, OrderFormVo.class);
        OrderFormVoList.stream().forEach(item->item.setApplicationStatus("通过"));

        writer.write(OrderFormVoList, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("征订单", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();

        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();

    }


}

