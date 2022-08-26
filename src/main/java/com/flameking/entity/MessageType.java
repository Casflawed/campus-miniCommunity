package com.flameking.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息的类型
 */
@Data
public class MessageType implements Serializable {
    private Integer id;
    private String name;
    private Integer value;
}
