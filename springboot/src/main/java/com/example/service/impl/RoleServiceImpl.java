package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.RoleMapper;
import com.example.service.RoleService;
import org.springframework.stereotype.Service;
import com.example.entity.Role;

/**
 * (Role)表服务实现类
 *
 * @author makejava
 * @since 2024-01-14 14:02:21
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


}
