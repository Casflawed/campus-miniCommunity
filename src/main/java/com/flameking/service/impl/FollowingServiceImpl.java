package com.flameking.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.flameking.entity.PostDetai;
import com.flameking.entity.User;
import com.flameking.service.FollowingService;
import com.flameking.service.PostService;
import com.flameking.service.ProfileService;
import com.flameking.utils.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class FollowingServiceImpl implements FollowingService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    PostService postService;
    @Autowired
    ProfileService profileService;
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 找出关注的人所有文章
     * @param page 页数
     * @param size 一个多少条
     * @return
     */
    @Override
    public PageInfo findFollowingPost(Integer page, Integer size) {
        //找出用户的关注的所有人
        Set<Integer> members = redisTemplate.opsForSet().members(jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()) + ":followings");

        if(members.size()==0)return new PageInfo();
        //然后再找文章
        List<Integer> idList=new ArrayList<>(members);
        //mybatis分页
        PageHelper.startPage(page,size);
        List<PostDetai> followingPost = postService.findFollowingPost(idList);
        PageInfo pageInfo=new PageInfo(followingPost);
        return pageInfo;
    }

    /**
     * 找出关注的所有人
     * @return
     */
    @Override
    public List<User> findFowings() {
       return profileService.findFollowings(jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
    }
}
