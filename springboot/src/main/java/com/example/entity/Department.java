package com.example.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Department)表实体类
 *
 * @author makejava
 * @since 2024-01-21 14:14:36
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("department")
public class Department  {
    //部门id
    @TableId
    private Integer departmentId;

    //部门名称
    private String departmentName;
    //学院id
    private Integer facultyId;
    //系负责人id（用户id）
    private Integer departmentManagerId;
    //系负责人姓名
    private String departmentManagerName;

//    // ===============
    @TableField(exist = false)
    private String facultyName;

}

