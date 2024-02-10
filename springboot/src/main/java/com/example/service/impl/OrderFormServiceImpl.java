package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.AuthAccess;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Department;
import com.example.entity.OrderForm;
import com.example.entity.Textbook;
import com.example.entity.vo.OrderFormVo;
import com.example.exception.CustomException;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.OrderFormMapper;
import com.example.mapper.TextbookMapper;
import com.example.service.OrderFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.deploy.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (OrderForm)表服务实现类
 *
 * @author makejava
 * @since 2024-01-24 22:21:19
 */
@Service("orderFormService")
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm> implements OrderFormService {

    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private OrderFormService orderFormService;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private TextbookMapper textbookMapper;

    @Override
    public Result addOrderForm(OrderForm orderForm) {

        // 设置时间 和 征订单初始的状态 和是否取消的状态
        orderForm.setApplicationTime(new Date());
        orderForm.setApplicationStatus(Constants.NO_APPROVAL);
        orderForm.setCancelStatus(Constants.UNCANCEL);
        // 如果说前端过来的数据没有departmentId 那么也没有isbn 需要加上,但是会有departmentName
        if (orderForm.getDepartmentId() == null){
            // 根据系名查一下departmentId
            LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Department::getDepartmentName,orderForm.getDepartmentName());
            Department department = departmentMapper.selectOne(queryWrapper);
            orderForm.setDepartmentId(department.getDepartmentId());
            // 根据书名找一下ISBN
            LambdaQueryWrapper<Textbook> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Textbook::getTextbookName,orderForm.getTextbookName());
            Textbook textbook = textbookMapper.selectOne(wrapper);
            orderForm.setIsbn(textbook.getIsbn());
        }
        int insert = orderFormMapper.insert(orderForm);
        if (insert > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }


    // 查询所有的征订单数据
    @Override
    public Result selectAllOrderForm(OrderForm orderForm) {
        List<OrderForm> orderFormList = orderFormMapper.selectAll(orderForm);
        return Result.success(orderFormList);
    }

    // 分页查询
    @Override
    public PageInfo<OrderForm> selectPage(OrderForm orderForm, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<OrderForm> orderForms = orderFormMapper.selectAll(orderForm);
        return PageInfo.of(orderForms);
    }

    // 取消已经申请的征订单
    @Override
    public Result cancelOrderForm(OrderForm orderForm) {
        int i = orderFormMapper.updateById(orderForm);
        if (i > 0){
           return Result.success();
        }

        throw  new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    // 处理审批订单的逻辑
    @Override
    public Result approveOrder(OrderForm orderForm, Integer roleId) {
        // 先判断通过和不通过
        if (orderForm.getApplicationStatus().equals(Constants.FAILDE)){
            // 不通过直接修改该条申请为不通过
            int i = orderFormMapper.updateById(orderForm);
            if (i > 0) {
                return Result.success();
            }
        }
        //  通过的情况下是谁通过
        else if (orderForm.getApplicationStatus().equals(Constants.APPROVED)){
            if (roleId.equals(Constants.DEPARTMENT_MANAGER)){
                // 如果是系主任就改成待教务处审批
                orderForm.setApplicationStatus(Constants.PENDING_APPROVAL_OFFICE);
            }
            // 如果不是就直接通过了
            int i = orderFormMapper.updateById(orderForm);
            if (i > 0) {
                return Result.success();
            }
        }
        // 显示系统异常
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    // 批量导出征订单的方法
    @Override
    public void exportBatchByIds(String orderFormIdList, HttpServletResponse response) throws IOException {

        ExcelWriter writer = ExcelUtil.getWriter(true);

        List<OrderForm> list;
        LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(orderFormIdList)) {
            List<Integer> ids = Arrays.stream(orderFormIdList.split(",")).map(Integer::valueOf).collect(Collectors.toList());
//            queryWrapper.in("order_form_id", ids);
            queryWrapper.in(OrderForm::getOrderFormId,ids);
        }
        list = orderFormService.list(queryWrapper);   // 查询出当前User表的所有数据
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("征订单表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();

        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public List<OrderForm> selectAllByIds(List<Integer> ids) {
        List<OrderForm> orderForms = orderFormMapper.selectAllByIds(ids);
        return orderForms;
    }
}
