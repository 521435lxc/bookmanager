package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Result;
import com.example.entity.TextbookCategory;
import com.example.mapper.TextbookCategoryMapper;
import com.example.service.TextbookCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TextbookCategory)表服务实现类
 *
 * @author makejava
 * @since 2024-01-20 19:40:38
 */
@Service("textbookCategoryService")
public class TextbookCategoryServiceImpl extends ServiceImpl<TextbookCategoryMapper, TextbookCategory> implements TextbookCategoryService {

    @Resource
    private TextbookCategoryService textbookCategoryService;

    @Override
    public Result selectAllCategory() {
        List<TextbookCategory> list = textbookCategoryService.list();
        return Result.success(list);
    }
}
