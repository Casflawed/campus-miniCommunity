package com.flameking.service.impl;

import com.flameking.dto.ReportDto;
import com.flameking.entity.Report;
import com.flameking.mapper.ReportMapper;
import com.flameking.service.ReportService;
import com.flameking.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportMapper reportMapper;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean addReport(ReportDto reportDto) {
        Integer userId = jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        Report t = reportMapper.findReport(userId, reportDto.getTo_id(), reportDto.getType());
        if(t!=null)
            return false;
        Report report=new Report();
        report.setFrom_uid(userId);
        report.setTo_id(reportDto.getTo_id());
        report.setContent(reportDto.getContent());
        report.setType(reportDto.getType());
        report.setTime(new Date());
        reportMapper.addReport(report);
        return true;
    }
}
