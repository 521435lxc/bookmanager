package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Textbook;

import java.util.List;


/**
 * (Textbook)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-19 21:40:43
 */
public interface TextbookMapper extends BaseMapper<Textbook> {

    // 查询方法
    List<Textbook> selectAll(Textbook textbook);
}

