package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Notice;
import com.example.entity.Textbook;
import com.example.exception.CustomException;
import com.example.mapper.NoticeMapper;
import com.example.service.NoticeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
//  查询所有公告信息
        List<Notice> noticeList = noticeService.list();
        return noticeList;
    }

    // 分页查询公告的方法
    @Override
    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }

    @Override
    public Result addNotice(Notice notice) {

        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getAnnouncementTitle,notice.getAnnouncementTitle());
        Notice dbNotice = noticeMapper.selectOne(queryWrapper);
        if (dbNotice != null){
            throw new CustomException(ResultCodeEnum.NOTICE_EXIST);
        }
        else {
            notice.setAnnouncementTime(new Date());

            int insert = noticeMapper.insert(notice);
            if (insert > 0){
                return Result.success();
            }

        }
       return Result.error();
    }

    // 修改用户
    @Override
    public Result updateNotice(Notice notice) {

        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getAnnouncementTitle,notice.getAnnouncementTitle());
        Notice dbNotice = noticeMapper.selectOne(queryWrapper); // 数据库中的notice

        if (dbNotice != null && !dbNotice.getAnnouncementId().equals(notice.getAnnouncementId())){
            throw new CustomException(ResultCodeEnum.NOTICE_EXIST);
        }
        int i = noticeMapper.updateById(notice);
        if (i > 0) {
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    //根据id删除公告
    @Override
    public Result deleteById(Integer announcementId) {
        int i = noticeMapper.deleteById(announcementId);
        if(i > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public Result deleteBatchNotice(List<Integer> announcementId) {
        int i = noticeMapper.deleteBatchIds(announcementId);

        if (i > 0){
            return Result.success();
        }
        throw new CustomException(ResultCodeEnum.SYSTEM_ERROR);
    }

}
