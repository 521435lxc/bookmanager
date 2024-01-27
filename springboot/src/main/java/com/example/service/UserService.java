package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Textbook;
import com.example.entity.User;
import com.github.pagehelper.PageInfo;

public interface UserService extends IService<User> {

    Account login(Account account);

    User selectById(Integer valueOf);

    Result register(Account account);

    Result sendCode(String email);

    Account loginEmail(Account account);

    Result queryAllTeacher();

    Result selectAllUser();

    PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize);

    Result setUserStatus(User user);

    Result addUser(User user);

    Result updateUser(User user);

    Result updatePassword(Account account);
}
