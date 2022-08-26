package com.flameking.mapper;


import com.flameking.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper {

    int register( @Param("username") String username, @Param("password") String password,
                  @Param("salt") String salt ,@Param("avatar") String avatar ,@Param("email")String email);

    int exists(String userName);

    User findUserByUserName(String username);

    List<User> findFollowerSimpleInfo(List<Integer> set);

    User findUserById(Integer id);
//
//    void updateUserName(@Param("id") Integer id,@Param("username") String username);
//
//    void updateSex(@Param("id") Integer id,@Param("sex") Integer sex);
//
//    void updateInfo(@Param("id") Integer id,@Param("info") String info);
//
//    void updateUser(User user);
//
//    void updateAvatar(@Param("id")Integer uid,@Param("avatar") String avatar);
    void updateUserInfo(User user);

    List<User> serachUser(String content);
    List<User> findAll();

}
