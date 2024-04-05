package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Textbook;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/*
 功能 : 
 作者 : lxc
 日期 :2024/1/22 18:16
*/
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/queryTeacher")
    public Result queryAllTeacher(){
        // 查询所有的教师用户
        return userService.queryAllTeacher();

    }

    @GetMapping("/selectAll")
    public Result selectAllUser(){
        // 查询所有用户只有管理员有权限查看，所以不查管理员
        return userService.selectAllUser();

    }

//    分页查询用户
    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> page = userService.selectPage(user, pageNum, pageSize);
        return Result.success(page);
    }
    // 停用启用用户
    @PutMapping("/setStatus")
    public Result setUserStatus(@RequestBody User user){
        return userService.setUserStatus(user);
    }

    // 添加新用户
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    // 修改用户
    @PutMapping("/update")
    public Result updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }
    // 修改密码
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        return userService.updatePassword(account);
    }

    //===================test=================
    @PostMapping("/test")
    public Result test(@RequestBody String[] testData){
        int length = testData.length;
        for (String testDatum : testData) {
            System.out.println(testDatum);
        }
        System.out.println(length);
        return Result.success();
    }
}
