package com.example.controller;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.entity.Textbook;
import com.example.service.NoticeService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
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

    // 分页查询公告列表的方法
    // 分页查询
    @GetMapping  ("/selectPage")
    public Result selectPage(Notice notice,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Notice> page = noticeService.selectPage(notice, pageNum, pageSize);
        return Result.success(page);
    }

    // 增加公告信息
    @PostMapping("/add")
    public Result addNotice(@RequestBody Notice notice){
        // 拿到当前的使用用户，就是创建人
        Account currentUser = TokenUtils.getCurrentUser();
        notice.setAnnouncerId(currentUser.getUserId());
        return noticeService.addNotice(notice);
    }

    // 修改公告信息
    @PutMapping("/update")
    public Result updateNotice(@RequestBody Notice notice){
        return noticeService.updateNotice(notice);
    }
    // 删除公告信息
    @DeleteMapping("/delete/{announcementId}")
    public Result deleteById(@PathVariable Integer announcementId){
        return noticeService.deleteById(announcementId);
    }
    // 批量删除公告信息

    @DeleteMapping("/deleteBatch")
    public Result deleteBatchTextbook(@RequestBody List<Integer> announcementId){
        return noticeService.deleteBatchNotice(announcementId);
    }


}