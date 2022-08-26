package com.flameking.entity;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 别提到的
 * 艾特相关的
 */
@Data
public class Mention implements Serializable {
    private Integer id;//主键 唯一
    private Integer from_id;//来自哪个用户的ID提醒
    private String from_name;//用户名
    private String from_avatar;//头像

    private Integer to_id;//提醒哪个用户
    private String title;//标题
    private String descr;//描述
    private String content;//内容
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;//时间

    private Integer status;//状态
    private Integer type_id;//文章的类型 在哪篇文章提的
    private Integer pid;//文章id


}
