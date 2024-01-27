package com.example.common.enums;

public enum RoleEnum {

    ADMIN(1),  // 管理员
    TEACHER(2), // 教师
    DEPARTMENT_MANAGER(3), // 系主任
    DEANS_OFFICE(4); // 教务处

    public Integer roleId;
    RoleEnum(Integer roleId) {
        this.roleId = roleId;
    }
}
