package com.flameking.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface EditPostMapper {
    @Insert("insert into edit_post values(#{id},#{content})")
    void insert(@Param("id") int id,@Param("content") String content);

    @Select("select content from edit_post where id=#{id}")
    String findContentById(int id);

    @Update("update edit_post set content=#{content} where id=#{id}")
    void update(@Param("id") int id,@Param("content") String content);
}
