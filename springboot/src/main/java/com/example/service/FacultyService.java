package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.Faculty;
import com.example.entity.Textbook;
import com.github.pagehelper.PageInfo;


/**
 * (Faculty)表服务接口
 *
 * @author makejava
 * @since 2024-01-21 13:29:11
 */
public interface FacultyService extends IService<Faculty> {

    Result selectAllFacutlty();

    PageInfo<Faculty> selectPage(Faculty faculty, Integer pageNum, Integer pageSize);

    Result updateFaculty(Faculty faculty);

    Result addFaculty(Faculty faculty);
}

