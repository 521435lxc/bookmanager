package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/*
 功能 : 
 作者 : lxc
 日期 :2024/1/13 16:45
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    //主键
    @TableId
    private Integer userId;
    //角色id (角色表连接)
    private Integer roleId;
    //用户名
    private String username;
    //密码
    private String password;
    //真实姓名
    private String realName;
    //头像
    private String avatar;
    //电子邮箱
    private String email;
    //电话
    private String phoneNumber;
    //注册时间
    private Date registrationDate;
    //1启用，0未启用
    private String enableStatus;

    // ========================
    @TableField(exist = false)
    private String roleName;
    // 邮箱登录需要用到的
//    @TableField(exist = false)
//    private String code;
}
