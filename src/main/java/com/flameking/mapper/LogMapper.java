package com.flameking.mapper;

import com.flameking.entity.SysLogger;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMapper {
    @Insert("insert into sysLogger(descr,start_time,ip,uid,url,request_type,class_method,params,response,exce_time,log_type)" +
            "values(#{descr},#{start_time},#{ip},#{uid},#{url},#{request_type},#{class_method},#{params},#{response},#{exce_time},#{log_type})")
    void insert(SysLogger sysLogger);
}
