package com.flameking.controller;

import com.flameking.dto.ReportDto;
import com.flameking.entity.ResultBean;
import com.flameking.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    @Autowired
    ReportService reportService;
    @PostMapping("/addReport")
    public ResultBean addReport(@RequestBody ReportDto dto){
        boolean b = reportService.addReport(dto);
        return b? ResultBean.success("举报成功！"): ResultBean.fail("你已经举报过了！");
    }
}
