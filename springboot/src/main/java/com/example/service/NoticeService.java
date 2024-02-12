package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.entity.Notice;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * (Notice)表服务接口
 *
 * @author makejava
 * @since 2024-01-15 20:19:12
 */
public interface NoticeService extends IService<Notice> {

    List<Notice> selectAllNoticeList();

    PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize);

    Result addNotice(Notice notice);

    Result updateNotice(Notice notice);

    Result deleteById(Integer announcementId);

    Result deleteBatchNotice(List<Integer> announcementId);
}

