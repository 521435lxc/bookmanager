package com.example.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (OrderForm)表实体类
 *
 * @author makejava
 * @since 2024-01-24 22:42:06
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_form")
public class OrderFormVo {
    //征订单id
    @TableId
    private Integer orderFormId;
    //ISBN号
    private String isbn;
    //教材名
    private String textbookName;
    //申请数量
    private Integer applicationNumber;
    //申请时间
    private Date applicationTime;
    //申请人id （用户表）
    private Integer applicantId;
    //1 未审批 2 待教务处审批 3 通过 4 未通过
    private String applicationStatus;

    private String applicationIllustration;
    // 追加的属性 使用系别名称 和 申请人
    @TableField(exist = false)
    private String departmentName;
    @TableField(exist = false)
    private String realName;

}

