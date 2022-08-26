package com.flameking.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章
 */
@Data
public class Post implements Serializable {
    private Integer id;//主键
    private String title;//标题
    private String descr;//描述
    private String content;//内容
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;//发布时间
    private Integer uid;//用户id
    private Integer status;//状态  删除or没删除
    private Integer type_id;//文章类型
    private Integer author;//作者 1代表原创 0代表转发
}
