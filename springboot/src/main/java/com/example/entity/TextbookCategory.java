package com.example.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (TextbookCategory)表实体类
 *
 * @author makejava
 * @since 2024-01-20 19:40:38
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("textbook_category")
public class TextbookCategory  {
    //教材分类id
    @TableId
    private Integer categoryId;

    //教材名称
    private String categoryName;



}

