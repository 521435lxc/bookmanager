package com.example.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Faculty;
import com.example.entity.Textbook;
import com.example.exception.CustomException;
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
    @Resource
    private FacultyMapper facultyMapper;

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



    @Override
    public Result updateFaculty(Faculty faculty) {

        //编辑院
//        先校验院的名字再插入
        LambdaQueryWrapper<Faculty> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(Faculty::getFacultyName,faculty.getFacultyName());
        Faculty dbFaculty = facultyMapper.selectOne(Wrapper);
        if (dbFaculty != null){
            throw new CustomException(ResultCodeEnum.FACULTY_EXIST);
        }
        int i = facultyMapper.updateById(faculty);
        if (i > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    // 增加院的信息
    @Override
    public Result addFaculty(Faculty faculty) {
        if (faculty == null){
            return Result.error("400","请输入数据");
        }
        LambdaQueryWrapper<Faculty> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Faculty::getFacultyName,faculty.getFacultyName());
        Faculty dbFaculty = facultyMapper.selectOne(queryWrapper);
        if (dbFaculty != null){
            throw new CustomException(ResultCodeEnum.FACULTY_EXIST);
        }
        int i = facultyMapper.insert(faculty);
        if (i > 0){
            return Result.success();
        }

        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }
}
