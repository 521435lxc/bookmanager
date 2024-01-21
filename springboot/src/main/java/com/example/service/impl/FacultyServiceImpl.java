package com.example.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Result;
import com.example.entity.Faculty;
import com.example.entity.Textbook;
import com.example.mapper.FacultyMapper;
import com.example.service.FacultyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Faculty)表服务实现类
 *
 * @author makejava
 * @since 2024-01-21 13:29:11
 */
@Service("facultyService")
public class FacultyServiceImpl extends ServiceImpl<FacultyMapper, Faculty> implements FacultyService {

    @Resource
    private FacultyService facultyService;
    // 查询所有的学院信息

    @Override
    public Result selectAllFacutlty() {
        List<Faculty> facultyList = facultyService.list();
        return Result.success(facultyList);
    }

    // 分页查询
    @Override
    public PageInfo<Faculty> selectPage(Faculty faculty, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Faculty> list = facultyService.list();
        return PageInfo.of(list);
    }
}
