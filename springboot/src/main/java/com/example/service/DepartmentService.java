package com.example.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.Department;
import com.example.entity.Faculty;
import com.github.pagehelper.PageInfo;

/**
 * (Department)表服务接口
 *
 * @author makejava
 * @since 2024-01-21 14:14:37
 */
public interface DepartmentService extends IService<Department> {

    Result selectAllDepartment();

    PageInfo<Department> selectPage(Department department, Integer pageNum, Integer pageSize, Integer facultyId);

    Result addDepartment(Department department);

    Result deleteDepartment(Integer departmentId);
}

