package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色用户父类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Integer userId;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 角色标识 */
    private Integer roleId;

    private String roleName;
    /** 头像 */
    private String avatar;

    private String token;
    // 真实姓名
    private String realName;

    //电话
    private String phoneNumber;

    // 为了防止系主任
    private Integer departmentId;
    private String departmentName;

    private String newPassword;

    // 邮箱登录需要用到的
    private String email;
    private String code;
}
