package com.example.controller;
import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;
    // 查询公告列表的方法
    @GetMapping("/selectAll")
    public Result selectAllNotice(){

        List<Notice> notices = noticeService.selectAllNoticeList();
        return Result.success(notices);
    }

}