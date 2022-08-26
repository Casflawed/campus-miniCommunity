package com.flameking.mapper;

import com.flameking.entity.Message;
import com.flameking.entity.MessageType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    @Insert("insert into message(to_id,message,time,type_id) values(#{to_id},#{message},#{time},#{type_id})")
    void addMessage(Message message);

    @Select("select count(*) from message where to_id=#{uid} and status=1")
    Integer messageCount(Integer uid);


    @Select("select *,#{uid} as uid from msg_type")
    @Results(value = {
            @Result(id = true,column = "id",property = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "value",column = "{id=id,uid=uid}",one = @One(select = "com.flameking.mapper.MessageMapper.findTypeNum"))
    })
    List<MessageType> findMessageTypeNum(@Param("uid") Integer uid);

    @Select("select count(*) from message where to_id=#{uid} and type_id=#{id} and status=1")
    Integer findTypeNum(@Param("id") Integer id,@Param("uid") Integer uid);

    @Select("select * from message where type_id=#{id} and to_id=#{uid} and status!=0")
    List<Message> findMessageByTypeId(@Param("id") Integer id,@Param("uid") Integer uid);

    @Select("select * from message where id=#{id} and to_id=#{uid}")
    Message selectOneByIdAndUid(@Param("id") Integer id,@Param("uid") Integer uid);

    @Update("update message set status=#{status} where id=#{id} and to_id=#{uid}")
    void updateStatus(@Param("id") Integer id,@Param("uid") Integer uid,@Param("status") Integer status);
}
