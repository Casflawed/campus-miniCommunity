package com.flameking.service;

import com.github.pagehelper.PageInfo;
import com.flameking.entity.User;

import java.util.List;

public interface FollowingService {
    PageInfo findFollowingPost(Integer page, Integer size);

    List<User> findFowings();
}
