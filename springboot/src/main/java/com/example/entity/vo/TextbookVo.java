package com.example.entity.vo;


import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("textbook")
public class TextbookVo {
    //教材id
    @TableId
    private Integer textbookId;
    //ISBN号
    @Alias("ISBN号")
    private String isbn;
    //教材名称
    @Alias("教材名称")
    private String textbookName;

    //出版社
    @Alias("出版社")
    private String press;
    //作者
    @Alias("作者")
    private String author;
    //版本
    @Alias("版本")
    private String version;

    //状态 1可征订 0 不可征订
    private String orderStatus;


}

