package com.example.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Notice)表实体类
 *
 * @author makejava
 * @since 2024-01-15 20:23:51
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("notice")
public class Notice  {
    //公告id@TableId
    private Integer announcementId;

    //公告内容
    private String announcementTitle;
    //公告内容
    private String announcementText;
    //发布时间
    private Date announcementTime;
    //发布人id (用户id)
    private Integer announcerId;



}

