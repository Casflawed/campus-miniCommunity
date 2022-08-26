package com.flameking.service;

import com.github.pagehelper.PageInfo;
import com.flameking.entity.ReportDeatil;
import com.flameking.entity.User;

import java.util.List;

public interface AdminService {
    User findById(Integer id);
    PageInfo findAll(Integer page, Integer size);

    boolean changeStatus(Integer id);

    List<ReportDeatil> findAllReport();

    void solve(Integer id);

    void deleteSolve(Integer id);
}
