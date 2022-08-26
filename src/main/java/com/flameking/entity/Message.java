package com.flameking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable {
    private Integer id;//主键唯一

    private Integer to_id;//发给哪个用户
    private String message;//消息内容
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;//时间
    private Integer type_id;//消息的类型
    private Integer status;//状态 未读、已读、删除
}
