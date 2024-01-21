package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.TextbookCategory;


/**
 * (TextbookCategory)表服务接口
 *
 * @author makejava
 * @since 2024-01-20 19:40:38
 */
public interface TextbookCategoryService extends IService<TextbookCategory> {

    Result selectAllCategory();
}

