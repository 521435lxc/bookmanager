package com.example.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Textbook)表实体类
 *
 * @author makejava
 * @since 2024-01-19 21:40:44
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("textbook")
public class Textbook  {
    //教材id
    @TableId
    private Integer textbookId;

    //图书封面
    private String bookCover;
    //ISBN号
    private String isbn;
    //教材名称
    private String textbookName;
    //教材分类（分类表）
    private Integer categoryId;
    //出版社
    private String press;
    //作者
    private String author;
    //版本
    private String version;

    //状态 1可征订 0 不可征订
    private String orderStatus;

    // =============  追加属性
    @TableField(exist = false)
    private String categoryName;

}

