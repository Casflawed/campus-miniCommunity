package com.flameking.mapper;

import com.flameking.entity.Answer;
import com.flameking.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    @Insert("insert into comment(uid,uname,avatar,time,content,pid) values(#{uid},#{uname},#{avatar},#{time},#{content},#{pid})")
    void addComment(Comment comment);

    @Select("select * from comment where pid=#{id} order by time desc")
    List<Comment> findCommentByPid(Integer id);

    @Insert("insert into answer(uid,uname,avatar,content,time,comment_id) values (#{uid},#{uname},#{avatar},#{content},#{time},#{comment_id})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void answer(Answer answer);

    @Select("select * from answer where comment_id=#{id} order by time desc")
    List<Answer> findAnswerByCommentId(int id);

    @Select("select * from comment where id=#{commentId}")
    Comment findByCommentId(Integer commentId);
}
