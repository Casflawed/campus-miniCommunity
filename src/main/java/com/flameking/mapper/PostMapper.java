package com.flameking.mapper;

import com.flameking.entity.Post;
import com.flameking.entity.PostDetai;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMapper {

    int addPost(Post post);

    List<PostDetai> findAll();

    PostDetai findPostById(Integer id);

    List<PostDetai> findTypeById(@Param("id") Integer id);

    void update(Post post);

    List<PostDetai> findPostByUserId(Integer id);

    List<PostDetai> findFollowingPost(List<Integer> list);

    List<PostDetai> serachPost(String content);

    int rePost(Post postById);

    void changeStatus(@Param("id") Integer id,@Param("status") int status);
}
