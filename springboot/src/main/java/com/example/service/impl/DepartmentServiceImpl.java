package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Department;
import com.example.entity.Faculty;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.FacultyMapper;
import com.example.mapper.UserMapper;
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

    @Resource
    private UserMapper userMapper;

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

//        if (department == null){
//            return Result.error("400","请输入数据");
//        }
        // 首先校验系存不存在
        // 其次根据前端传过来的用户id改了改用户的角色为系主任
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getDepartmentName,department.getDepartmentName());
        List<Department> departmentList = departmentMapper.selectList(queryWrapper);
        if (departmentList.size() > 0){
            throw new CustomException(ResultCodeEnum.DEPARTMENT_EXIST);
        }
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getRealName,department.getDepartmentManagerName());
        User user = userMapper.selectOne(userWrapper);

        user.setRoleId(Constants.DEPARTMENT_MANAGER);

        int insert = departmentMapper.insert(department);
        if (insert > 0) {
            userMapper.updateById(user); //修改用户角色为系主任
            department.setDepartmentManagerId(user.getUserId());
            return Result.success();
        }
        return Result.error();
    }

    // 根据id删除系
    @Override
    public Result deleteDepartment(Integer departmentId) {
        // 设置该系主任账号状态为教师
        Department department = departmentMapper.selectById(departmentId);
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getRealName,department.getDepartmentManagerName());
        User user = userMapper.selectOne(userWrapper);
        user.setRoleId(Constants.TEACHER);
        userMapper.updateById(user);
        int i = departmentMapper.deleteById(departmentId);

        if (i > 0){

            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }


     // 编辑系的方法
    @Override
    public Result updateDepartment(Department department) {
        // 如果是改系名就直接改 如果换主任就要将原来的系主任设置成老师，如果有的话
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRealName,department.getDepartmentManagerName());
        User newManager = userMapper.selectOne(queryWrapper); // 将要被设置成系主任的用户

        LambdaQueryWrapper<Department> departmentWrapper = new LambdaQueryWrapper<>();
        departmentWrapper.eq(Department::getDepartmentId,department.getDepartmentId());
        Department dbDepartment = departmentMapper.selectOne(departmentWrapper); // 将要被修改的部门

        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getRealName,dbDepartment.getDepartmentManagerName());
        User oldManager = userMapper.selectOne(userWrapper); // 原来的系主任

        if (oldManager != null){
            // 如果将要修改的部门的系主任名字不等于空 就设置原有的为老师 2
            oldManager.setRoleId(Constants.TEACHER);
            userMapper.updateById(oldManager);// 老

        }

        // 更新 传过来的系主任这个角色的id改为3
        if (newManager != null)
        {
            newManager.setRoleId(Constants.DEPARTMENT_MANAGER);
            // 更新用户表
            userMapper.updateById(newManager); // 新
        }


        // 更新系表
        // 先查一下系名存不存在
        LambdaQueryWrapper<Department> lambdaWrapper = new LambdaQueryWrapper<>();
        lambdaWrapper.eq(Department:: getDepartmentName, department.getDepartmentName());
        Department one = departmentMapper.selectOne(lambdaWrapper);
        if (one != null){
            if (one.getDepartmentId() != department.getDepartmentId()){
                throw new CustomException(ResultCodeEnum.DEPARTMENT_EXIST);
            }
        }
        department.setDepartmentId(dbDepartment.getDepartmentId());
        department.setDepartmentManagerId(newManager.getUserId());
        int i = departmentMapper.updateById(department);
        if (i > 0){

            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

}
