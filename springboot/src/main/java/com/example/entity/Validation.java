package com.example.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Validation)表实体类
 *
 * @author makejava
 * @since 2024-01-18 18:50:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("validation")
public class Validation  {
    //验证码表id@TableId
    @TableId
    private Integer validationId;

    //邮箱
    private String email;
    //验证码

    private String code;
    //超时时间
    private Date timeout;



}

