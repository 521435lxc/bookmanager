package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Notice)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-15 20:21:23
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

}

