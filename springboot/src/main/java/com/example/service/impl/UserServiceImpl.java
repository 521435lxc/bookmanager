package com.example.service.impl;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.RoleMapper;
import com.example.mapper.UserMapper;
import com.example.mapper.ValidationMapper;
import com.example.service.UserService;
import com.example.service.ValidationService;
import com.example.utils.BeanCopyUtils;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
 功能 : 
 作者 : lxc
 日期 :2024/1/13 17:06
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private ValidationMapper validationMapper;
    @Resource
    private ValidationService validationService;

    @Value("${spring.mail.username}")
    private String from;

//    注入邮箱验证码的依赖
    @Autowired
    JavaMailSender javaMailSender;

    // 登录的需求 （拿到校验roleid）
    @Override
    public Account login(Account account) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,account.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user != null){
            String password = account.getPassword();
            if (!password.equals(user.getPassword())){ // 校验密码
                throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
            }
            if (!user.getEnableStatus().equals(Constants.AVAILABLE)){ // 是否启用
                throw new CustomException(ResultCodeEnum.USER_ACCOUNT_UNAVAILABLE);
            }

        }else {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }

        // 查一下这个人是不是系主任 然后把user里的departmentId的属性加上,到时候订单表里好查，只能看自己部门
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getDepartmentManagerId,user.getUserId());
        Department department = departmentMapper.selectOne(wrapper);

        // 查询用户的角色名称 用selectById要在类的主键上加上注解@TableId
        Role role = roleMapper.selectById(user.getRoleId());
        // 生成token
        String tokenData = user.getUserId() + "-" + role.getRoleName();
        String token = TokenUtils.createToken(tokenData, account.getPassword());
        account = BeanCopyUtils.copyBean(user, Account.class);
        account.setToken(token);
        account.setRoleName(role.getRoleName());
        // 如果能查到在赋值
        if (!ObjectUtil.isEmpty(department))
        {
            account.setDepartmentId(department.getDepartmentId());
        }
        return account;
    }

    @Override
    public User selectById(Integer userId) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId,userId);
        return userMapper.selectOne(queryWrapper);
    }


    // 注册新用户的方法 用户名 用户角色 密码
    @Override
    public Result register(Account account) {

        //根据用户名查询
        LambdaQueryWrapper<User> queryWrapperUser = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Role> queryWrapperRole = new LambdaQueryWrapper<>();
        queryWrapperUser.eq(User::getUsername,account.getUsername());
        queryWrapperRole.eq(Role::getRoleName,account.getRoleName());

        User dbuser = userMapper.selectOne(queryWrapperUser);

        if (dbuser == null){ // 如果查询不到就代表可以注册
        // 根据用户角色名查询用户角色id
            Role role = roleMapper.selectOne(queryWrapperRole);
            if (role.getRoleId() == Constants.DEPARTMENT_MANAGER){
        //   如果是系主任的注册要查一查这个系里面有没有department_manager，
        //   如果有提示该系系主任已存在，如果没有就在系表中插入一条记录
                LambdaQueryWrapper<Department> queryWrapperdepartment = new LambdaQueryWrapper<>();
                queryWrapperdepartment.eq(Department::getDepartmentName,account.getDepartmentName());
                Department dbdepartment = departmentMapper.selectOne(queryWrapperdepartment);
                if (!ObjectUtil.isEmpty(dbdepartment.getDepartmentManagerId())){
                        // 如果存在系主任id说明有系主任
                    throw new CustomException(ResultCodeEnum.DEPARTMENT_MANAGER_EXIST);
                }
                else {
                    // 如果没有向该系插入系主任的id 名字信息 确切的说是修改
                    Department department = new Department();  // 创建一个department对象
//                    还需要插入这条用户数据给user表
                    User managerUser = BeanCopyUtils.copyBean(account,User.class);
                    managerUser.setRoleId(role.getRoleId());
                    managerUser.setEnableStatus(Constants.AVAILABLE);
                    managerUser.setRegistrationDate(new Date());
                    int insert = userMapper.insert(managerUser);
                    if (insert > 0){
                        // 插入成功再想department表里面设置数据department_manager_id
                        // updateById 这个方法制定个id需要
                        department.setDepartmentId(dbdepartment.getDepartmentId());
                        department.setDepartmentManagerId(managerUser.getUserId());
                        if (managerUser.getRealName() != null){
                            department.setDepartmentManagerName(managerUser.getRealName());
                        }
                        departmentMapper.updateById(department);
                        return Result.success();
                    }else {
                        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
                    }

                }
            }
            User user = BeanCopyUtils.copyBean(account, User.class);
            // 将roleId设置到user里
            user.setRoleId(role.getRoleId());
            // 设置用户状态为可用
            user.setEnableStatus(Constants.AVAILABLE);
            // 设置一下注册的时间
            user.setRegistrationDate(new Date());
            int i = userMapper.insert(user);
            if (i > 0){
                return Result.success();
            }else {
                throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
            }
        }else { // 否则抛出异常用户已经存在
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
    }

    // 获取验证码
    @Override
    public Result sendCode(String email) {
        // 查询用户表中有没有这个email
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        User user = userMapper.selectOne(queryWrapper);

        if (!ObjectUtil.isEmpty(user)){ // 如果不等于就要生成验证码了
            LambdaQueryWrapper<Validation> validationQueryWrapper = new LambdaQueryWrapper<>();

            Date now = new Date();
            // 如果有已经存在没有过期验证码，提示不要继续发送
            validationQueryWrapper.eq(Validation::getEmail,email);
            validationQueryWrapper.gt(Validation::getTimeout, now);
            Validation dbValidation = validationMapper.selectOne(validationQueryWrapper);
            if (dbValidation != null) {
                throw new CustomException(ResultCodeEnum.CODE_EXIST);
            }
            String code = RandomUtil.randomNumbers(4); // 随机一个 4位长度的验证码
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom(from);  // 发送人
            message.setTo(email);
            message.setSentDate(now);
            message.setSubject("【长春光华学院】登录邮箱验证");
            message.setText("您本次登录的验证码是：" + code + "，有效期2分钟。请妥善保管，切勿泄露");
            javaMailSender.send(message);
            // 发送成功之后，把验证码存到数据库
            // 如果不是第一次发送验证码 ，那就需要先删除原有的
            LambdaQueryWrapper<Validation> validationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            validationLambdaQueryWrapper.eq(Validation::getEmail,email);
            validationMapper.delete(validationLambdaQueryWrapper);
            // 创建validation对象准备插入
                Validation validation = new Validation();
                validation.setCode(code);
                validation.setEmail(email);
                // 时间向后偏移2分钟
                DateTime timeout = DateUtil.offsetMinute(now, 2);
                validation.setTimeout(timeout);
                int i = validationMapper.insert(validation);
                if (i > 0){
                    return Result.success();
            }

        }
        else {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        return Result.error();
    }

    @Override
    public Account loginEmail(Account account) {
        // 根据邮箱查找这个用户的信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail,account.getEmail());
        User user = userMapper.selectOne(wrapper);
        // 发送验证码的时候已经验证过了 用户肯定存在 接下来根据邮箱和验证码
        LambdaQueryWrapper<Validation> validationWrapper = new LambdaQueryWrapper<>();
        validationWrapper.eq(Validation::getCode,account.getCode());
        validationWrapper.eq(Validation::getEmail,account.getEmail());
        Validation validation = validationMapper.selectOne(validationWrapper);
        if (validation != null){
            // 查到了这条数据证明输入对了 那就要给初始化account信息了

            // 查询用户的角色名称 用selectById要在类的主键上加上注解@TableId
            Role role = roleMapper.selectById(user.getRoleId());
            // 生成token
            String tokenData = user.getUserId() + "-" + role.getRoleName();
            String token = TokenUtils.createToken(tokenData, user.getPassword());

            // account.setRoleName(role.getRoleName());
            account = BeanCopyUtils.copyBean(user, Account.class);
            account.setToken(token);
            account.setRoleName(role.getRoleName());

        }else {
            // 验证码输入有误
            throw new CustomException(ResultCodeEnum.VALIDATION_ERROR);
        }
        return account;
    }

    // 查询教师的方法
    @Override
    public Result queryAllTeacher() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRoleId,Constants.TEACHER);
        List<User> teacherList = userMapper.selectList(queryWrapper);

        return Result.success(teacherList);
    }

    // 查询除管理员以外的用户信息
    @Override
    public Result selectAllUser() {
        List<User> list = userService.list();

        return Result.success(list);
    }

    // 分页查询用户
    @Override
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.selectAll(user);
        // 过滤掉管理员
        List<User> users = userList.stream().filter(
                u -> !u.getRoleId().equals(Constants.ADMIN)
        ).collect(Collectors.toList());

        return PageInfo.of(users);
    }


    // 设置用户的状态
    @Override
    public Result setUserStatus(User user) {
        int i = userMapper.updateById(user);
        if (i > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    // 添加用户
    @Override
    public Result addUser(User user) {
        // 查一查用户名有没有重复的
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser !=  null){
            //说明存在相同用户
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        // 设置用户状态
        user.setEnableStatus(Constants.AVAILABLE);
        // 设置注册时间
        user.setRegistrationDate(new Date());
        // 添加
        int insert = userMapper.insert(user);
        if (insert > 0){
            return Result.success();
        }
        return Result.error("500","怎么回事？");
    }


    @Override
    public Result updateUser(User user) {
//        直接修改这个用户传来的信息
        int i = userMapper.updateById(user);
        if (i > 0) {
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public Result updatePassword(Account account) {
        // 修改密码 把新密码赋值给密码字段
        // 查到当前账户的用户
        User user = userService.selectById(account.getUserId());
        user.setPassword(account.getNewPassword());
        // 更改这个用户
        int i = userMapper.updateById(user);
        if (i > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }
}
