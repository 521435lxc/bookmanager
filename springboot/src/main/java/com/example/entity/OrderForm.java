package com.example.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class OrderForm  {
    //征订单id@TableId
    private Integer orderFormId;

    //ISBN号
    private String isbn;
    //教材名
    private String textbookName;
    //申请数量
    private Integer applicationNumber;
    //使用系别（系表）
    private Integer departmentId;
    //申请时间
    private Date applicationTime;
    //申请人id （用户表）
    private Integer applicantId;
    //1 未审批 2 待教务处审批 3 通过 4 未通过
    private String applicationStatus;
    //审批回复
    private String replyMessage;
    
    private String applicationIllustration;
    // 是否取消 (默认为1 未取消  2 是取消)
    private String cancelStatus;

    // 追加的属性 使用系别名称 和 申请人
    @TableField(exist = false)
    private String departmentName;
    @TableField(exist = false)
    private String realName;

}

