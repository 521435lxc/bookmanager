package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-14 14:02:17
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}

