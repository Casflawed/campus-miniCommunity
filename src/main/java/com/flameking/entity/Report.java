package com.flameking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Report {
    private Integer id;
    private Integer from_uid;//举报人的ID
    private Integer to_id;//要举报的文章或者用户ID
    private String content;//举报内容
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private Integer type;//举报的类型 0用户 1文章
    private Integer status;//状态 1未处理 0已处理

}
