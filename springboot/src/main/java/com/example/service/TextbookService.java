package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.Textbook;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 * (Textbook)表服务接口
 *
 * @author makejava
 * @since 2024-01-19 21:40:44
 */
public interface TextbookService extends IService<Textbook> {

    List<Textbook> selectAllTextbook(Textbook textbook);

    PageInfo<Textbook> selectPage(Textbook textbook, Integer pageNum, Integer pageSize);

    Result addTextbook(Textbook textbook);

    Result deleteTextbook(Integer textbookId);

    Result deleteBatchTextbook(List<Integer> textbookIds);

    Result updateTextbook(Textbook textbook);
}

