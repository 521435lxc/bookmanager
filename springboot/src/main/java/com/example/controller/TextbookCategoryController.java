package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.TextbookCategory;
import com.example.service.TextbookCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (TextbookCategory)表控制层
 *
 * @author makejava
 * @since 2024-01-20 19:42:39
 */
@RestController
@RequestMapping("/category")
public class TextbookCategoryController  {
    /**
     * 服务对象
     */
    @Resource
    private TextbookCategoryService textbookCategoryService;

    @GetMapping("/selectAll")
    public Result selectAllCategory() {
        return textbookCategoryService.selectAllCategory();
    }

}

