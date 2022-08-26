package com.flameking.service.impl;

import com.flameking.entity.Type;
import com.flameking.mapper.TypeMapper;
import com.flameking.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    TypeMapper typeMapper;

    /**
     * 找出文章的所有类型
     * @return
     */
    @Override
    public List<Type> findTypeList() {
        return typeMapper.findTypeList();
    }

    /**
     * 找出文章类型的数量
     * @return
     */
    @Override
    public List<Type> findTypeListNums() {
        return typeMapper.findTypeListNums();
    }

    /**
     * 通过文章类型名字找出类型的id
     * @param name
     * @return
     */
    @Override
    public Integer findTypeIdByName(String name) {
        return typeMapper.findTypeIdByName(name);
    }
}
