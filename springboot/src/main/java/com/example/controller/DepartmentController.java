package com.example.controller;

import com.example.common.Result;
import com.example.entity.Department;
import com.example.entity.Faculty;
import com.example.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Department)表控制层
 *
 * @author makejava
 * @since 2024-01-21 14:14:35
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    /**
     * 服务对象
     */
    @Resource
    private DepartmentService departmentService;

    //查询所有的系的信息
    @GetMapping("/selectAll")
    public Result selectAllDepartment(){
        return departmentService.selectAllDepartment();
    }

    // 根据学院id分页查询系的信息
    @GetMapping  ("/selectPage")
    public Result selectPage(Department department,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "1") Integer facultyId) {
        PageInfo<Department> page = departmentService.selectPage(department, pageNum, pageSize, facultyId);
        return Result.success(page);
    }
    // 新增系
    @PostMapping("/addDepartment")
    public Result addDepartment(@RequestBody Department department){

        return departmentService.addDepartment(department);
    }
    // 根据id删除系
    @DeleteMapping("/deleteById/{departmentId}")
    public Result deleteById(@PathVariable Integer departmentId){
        return departmentService.deleteDepartment(departmentId);

    }

    // 编辑系的信息
    @PutMapping("/updateDepartment")

    public Result updateDepartment(@RequestBody Department department){
        return departmentService.updateDepartment(department);

    }

}

