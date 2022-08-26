package com.flameking.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {


    //用户id
    private Integer id;
    //用户名
    private String username;
    //密码
    private String password;
    //加密的盐值
    private String salt;
    //邮箱
    private String email;
    //头像
    private String avatar;
    //简介
    private String private_info;
    //性别 0未知 1男 2女
    private Integer sex;
    //状态
    private Integer status;

    private Profile profile;
}
