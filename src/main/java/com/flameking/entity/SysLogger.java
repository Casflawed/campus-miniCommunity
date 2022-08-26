package com.flameking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 */
@Data
public class SysLogger implements Serializable {
    private Integer id;//主键 自增
    private String descr;//描述
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start_time;//访问的起始时间
    private String ip;//ip地址
    private Integer uid;//用户id
    private String url;//访问的url
    private String request_type;//请求类型  get post put delete这些
    private String class_method;//访问哪个方法
    private Object params;//请求参数
    private Object response;//响应结果
    private Integer exce_time;//访问的结束时间
    private Integer log_type;//日志的类型  正常 or 异常
}
