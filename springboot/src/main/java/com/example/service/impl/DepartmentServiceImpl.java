package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Department;
import com.example.entity.Faculty;
import com.example.exception.CustomException;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.FacultyMapper;
import com.example.service.DepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Department)表服务实现类
 *
 * @author makejava
 * @since 2024-01-21 14:14:37
 */
@Service("departmentService")
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Resource
    private DepartmentService departmentService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private FacultyMapper facultyMapper;

    // 查询所有的系的信息
    @Override
    public Result selectAllDepartment() {
        List<Department> list = departmentService.list();
        return Result.success(list);
    }

    // 分页且根据院的id查询系的信息
    @Override
    public PageInfo<Department> selectPage(Department department, Integer pageNum, Integer pageSize, Integer facultyId) {
        PageHelper.startPage(pageNum, pageSize);
        // 查询院的名字
        Faculty faculty = facultyMapper.selectById(facultyId);
        String facultyName = faculty.getFacultyName();

        // 根据院的id查询系的信息
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getFacultyId,facultyId);
        // 追加院名的属性
        List<Department> departmentList = departmentMapper.selectList(queryWrapper);
        List<Department> collect =
                departmentList
                        .stream()
                        .map(
                            d ->
                            {
                                d.setFacultyName(facultyName);
                                return d;
                            }
                        )
                        .collect(Collectors.toList());
        return PageInfo.of(collect);
    }

    // 新增系
    @Override
    public Result addDepartment(Department department) {
        // 首先校验系存不存在
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getDepartmentName,department.getDepartmentName());
        List<Department> departmentList = departmentMapper.selectList(queryWrapper);
        if (departmentList.size() > 0){
            throw new CustomException(ResultCodeEnum.DEPARTMENT_EXIST);
        }
        int insert = departmentMapper.insert(department);
        if (insert > 0) {
            return Result.success();
        }
        return Result.error();
    }

    // 根据id删除系
    @Override
    public Result deleteDepartment(Integer departmentId) {
        int i = departmentMapper.deleteById(departmentId);
        if (i > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

}
