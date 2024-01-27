package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.AuthAccess;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private UserService userService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /**
     * 登录
     */
    //  用户名登录
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                ) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        account = userService.login(account);
        return Result.success(account);
    }
    // 邮箱登录
    @AuthAccess
    @PostMapping("/loginEmail")
    public Result loginEmail(@RequestBody Account account) {
        account = userService.loginEmail(account);
        return Result.success(account);
    }


    // 获取验证码的接口
    @AuthAccess
    @PostMapping("/email/{email}")
    public Result sendEmail(@PathVariable String email) {
        if (ObjectUtil.isEmpty(email)){
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        userService.sendCode(email);
        return Result.success();
    }



    /**
     * 注册 1 查找是否存在重复用户 2 给用户一个初始的状态默认为可用
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        Result result = userService.register(account);
        return result;
    }

    // 修改密码
//    @PutMapping("/updatePassword")
//    public Result updatePassword(@RequestBody Account account) {
//        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())) {
//            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
//        }
//
//        return Result.success();
//    }

}
