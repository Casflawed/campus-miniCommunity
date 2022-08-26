package com.flameking.entity;

import lombok.Data;

@Data
public class ReportDeatil extends Report {
    private String username;//举报人的名字
    private String address;//举报的地址   用户地址或文章地址
}
