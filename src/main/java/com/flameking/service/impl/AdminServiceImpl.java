package com.flameking.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.flameking.entity.PostDetai;
import com.flameking.entity.Report;
import com.flameking.entity.ReportDeatil;
import com.flameking.entity.User;
import com.flameking.mapper.PostMapper;
import com.flameking.mapper.ReportMapper;
import com.flameking.mapper.UserMapper;
import com.flameking.service.AdminService;
import com.flameking.utils.ReplaceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    PostMapper postMapper;

    @Override
    public User findById(Integer id) {
        return userMapper.findUserById(id);
    }

    @Override
    public PageInfo findAll(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<User> list=userMapper.findAll();
        PageInfo pageInfo=new PageInfo(list);
        return pageInfo;
    }

    @Override
    public boolean changeStatus(Integer id) {
        User user= userMapper.findUserById(id);
        if(user==null)
            return false;
        if(user.getUsername().equals("admin"))
            return false;
        User up=new User();
        up.setId(user.getId());
        if ((user.getStatus()==1))
            up.setStatus(0);
        else up.setStatus(1);
        userMapper.updateUserInfo(up);
        return true;
    }

    @Override
    public List<ReportDeatil> findAllReport() {
        List<Report> list=reportMapper.findAll();
        List<ReportDeatil> reportDeatils=new ArrayList<>();
        User user=null;
        for(Report r:list){
            ReportDeatil report= new ReportDeatil();
            user=userMapper.findUserById(r.getFrom_uid());
            report.setUsername(ReplaceUtil.userReplace(user.getUsername()));
            if(r.getType()==0){
                user=userMapper.findUserById(r.getTo_id());
                report.setAddress(ReplaceUtil.userReplace(user.getUsername()));
            }
            else{
                PostDetai post= postMapper.findPostById(r.getTo_id());
                if(post==null)
                    report.setAddress("已被处理");
                else
                    report.setAddress(ReplaceUtil.PostReplace(post.getId(),post.getTitle()));
            }
            report.setContent(r.getContent());
            report.setTime(r.getTime());
            report.setId(r.getId());
            report.setTo_id(r.getTo_id());
            report.setType(r.getType());
            reportDeatils.add(report);
        }
        return reportDeatils;
    }

    @Override
    public void solve(Integer id) {
        reportMapper.updateStatus(id);
    }

    @Override
    public void deleteSolve(Integer id) {
        Report report = reportMapper.findById(id);
        if(report==null)return;
        Integer to_id = report.getTo_id();
        if(report.getType()==0){

            User user=new User();
            user.setId(to_id);
            user.setStatus(0);
            userMapper.updateUserInfo(user);
        }
        else{
            postMapper.changeStatus(to_id,0);
        }
        reportMapper.updateStatus(id);
    }

}
