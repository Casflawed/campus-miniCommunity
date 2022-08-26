package com.flameking.mapper;

import com.flameking.entity.Report;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReportMapper {
    @Insert("insert into report(from_uid,to_id,content,time,type) " +
            "value(#{from_uid},#{to_id},#{content},#{time},#{type})")
    void addReport(Report report);

    @Select("select * from report where from_uid=#{from_uid} and to_id=#{to_id} and type=#{type}")
    Report findReport(@Param("from_uid") int from_uid,@Param("to_id") int to_id,@Param("type") int type);

    @Select("select * from report where status=1 order by time desc")
    List<Report> findAll();

    @Update("update report set status=0 where id=#{id}")
    void updateStatus(Integer id);

    @Select("select * from report where id=#{id}")
    Report findById(Integer id);
}
