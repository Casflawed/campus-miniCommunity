package com.flameking.mapper;

import com.flameking.entity.Mention;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentionMapper {
    @Insert("insert into mention(from_id,to_id,title,descr,content,time,type_id,pid) value(#{mention.from_id},#{mention.to_id},#{mention.title},#{mention.descr},#{mention.content},#{mention.time},#{mention.type_id},#{mention.pid})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void addMention(@Param("mention") Mention mention);

    @Select("select * from mention where to_id=#{id} and status=1 order by time desc")
    List<Mention> findMentionsByUid(Integer id);


    @Select("select * from mention where id=#{id}")
    Mention findMentionById(Integer id);

    @Update("update mention set status=0 where id=#{to_id}")
    void deleteMention(Integer to_id);
}
