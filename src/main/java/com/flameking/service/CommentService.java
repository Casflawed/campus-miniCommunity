package com.flameking.service;

import com.flameking.entity.Answer;
import com.flameking.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment comment(Integer pid,String content);

    List<Comment> findCommentByPid(Integer id);

    Answer answer(Integer commentId,String content);


}
