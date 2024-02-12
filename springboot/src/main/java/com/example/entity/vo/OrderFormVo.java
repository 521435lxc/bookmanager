package com.example.entity.vo;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

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

    //ISBN号
    @Alias("ISBN号")
    private String isbn;
    //教材名
    @Alias("教材名")
    private String textbookName;
    //申请数量
    @Alias("申请数量")
    private Integer applicationNumber;

    @Alias("使用系别")
    private String departmentName;

    @Alias("申请人")
    private String realName;

    //申请时间
    @Alias("申请时间")
    private Date applicationTime;

    //1 未审批 2 待教务处审批 3 通过 4 未通过
    @Alias("审核状态")
    @Value("通过")
    private String applicationStatus;


}

