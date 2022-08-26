package com.flameking.service;

import com.flameking.entity.Type;

import java.util.List;

public interface TypeService {
    List<Type> findTypeList();

    List<Type> findTypeListNums();

    Integer findTypeIdByName(String name);
}
