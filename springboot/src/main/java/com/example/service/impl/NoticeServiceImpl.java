package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.example.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Notice)表服务实现类
 *
 * @author makejava
 * @since 2024-01-15 20:19:42
 */
@Service("noticeService")
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private NoticeService noticeService;

    @Override
    public List<Notice> selectAllNoticeList() {
//        查询所有公告信息
        List<Notice> noticeList = noticeService.list();
        return noticeList;
    }
}
