package com.example.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (ExcelDto)表实体类
 *
 * @author makejava
 * @since 2024-03-20 20:50:31
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("excel_dto")
// 设置表头行高度
@HeadRowHeight(20)
// 设置内容行高度
@ContentRowHeight(10)
public class ExcelDto  {

    @ExcelProperty("成员昵称")
    private String username;

    @ExcelProperty("成员编号")
    private String planeCode;

    @ExcelIgnore
    private String password;

    @ExcelProperty("性别")
    private Integer sex;

}

