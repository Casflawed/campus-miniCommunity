package com.flameking.service;

import com.github.pagehelper.PageInfo;
import com.flameking.entity.Post;
import com.flameking.entity.PostDetai;

import java.util.List;


public interface PostService {
    int addPost(Post post);



    PageInfo findAll(Integer page, Integer size);

    PostDetai findPostById(Integer id);

    PageInfo findTypeById(Integer id, Integer page, Integer size);

    void update( Post post);

    List<PostDetai> findPostByUserId(Integer id);

    boolean delete(Integer id);

    PostDetai findPostByIds(Integer id);

    List<PostDetai> findFollowingPost(List<Integer> uidList);

    List<PostDetai> serachPost(String content);

    int rePost(Integer id);

    Boolean changeStatus(Integer id);
}
