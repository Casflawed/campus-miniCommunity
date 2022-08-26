package com.flameking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论的实体类
 */
@Data
public class Comment implements Serializable {
    private Integer id;//主键唯一
    private Integer uid;//用户id，就是哪个用户评论的
    private String uname;//用户名
    private String avatar;//头像
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;//评论时间
    private String content;//评论内容
    private Integer pid;//评论的文章id
    private Integer status;//状态
    private List<Answer> answerList;//记录这条评论的所有回复
}
