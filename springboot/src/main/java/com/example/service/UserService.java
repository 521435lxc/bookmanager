package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.User;

public interface UserService extends IService<User> {

    Account login(Account account);

    User selectById(Integer valueOf);

    Result register(Account account);

    Result sendCode(String email);

    Account loginEmail(Account account);
}
