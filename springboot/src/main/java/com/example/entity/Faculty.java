package com.example.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Faculty)表实体类
 *
 * @author makejava
 * @since 2024-01-21 13:29:11
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("faculty")
public class Faculty  {
    //学院id
    @TableId
    private Integer facultyId;

    //学院name
    private String facultyName;
    //学院图片
    private String facultyBadge;



}

