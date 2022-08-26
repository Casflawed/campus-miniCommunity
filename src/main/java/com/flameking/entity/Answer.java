package com.flameking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复的实体类封装
 */
@Data
public class Answer implements Serializable {
    private Integer id;//主键、唯一
    private Integer uid;//用户id，就是哪个用户回放的
    private String uname;//用户名
    private String avatar;//头像
    private String content;//内容
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;//时间
    private Integer comment_id;//回复的哪一条评论
    private Integer status;//回复的状态
}
