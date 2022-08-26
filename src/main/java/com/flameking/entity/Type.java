package com.flameking.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Type implements Serializable {
    private Integer id;
    private String name;
    private String descr;
    private Integer type_num;
}
