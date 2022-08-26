package com.flameking.service.impl;

import com.flameking.dto.FindPwdSendEmailDto;
import com.flameking.dto.RegisterDto;
import com.flameking.dto.UpdatePasswordDto;
import com.flameking.entity.LimitIp;
import com.flameking.entity.User;
import com.flameking.mapper.UserMapper;
import com.flameking.service.*;
import com.flameking.utils.IpLimitUtil;
import com.flameking.utils.JWTUtil;
import com.flameking.utils.ApplicationUtil;
import com.flameking.utils.RedisKeyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;


@Service
@Transactional
public class UserServiceImpl implements UserService {
  @Autowired
  UserMapper userMapper;
  @Autowired
  InteractionService interactionService;
  @Lazy
  @Autowired
  RedisTemplate redisTemplate;
  @Lazy
  @Autowired
  EmailService emailService;
  @Lazy
  @Autowired
  IpLimitUtil ipLimitUtil;
  @Autowired
  private JWTUtil jwtUtil;

  /**
   * 用户注册
   *
   * @param registerDto
   * @return
   */
  @Override
  public Map<String, String> register(RegisterDto registerDto) {
    Map<String, String> map = new HashMap<>();
    if (!registerDto.getPassword().equals(registerDto.getSecondPassword())) {
      map.put("message", "两次密码不一致");
      return map;
    }
    //获取验证码
    String key = RedisKeyUtil.getEmailCodeKey(registerDto.getEmail());
    String code = (String) redisTemplate.opsForValue().get(key);

    if (code == null || code.equals("")) {
      map.put("message", "验证码已过期");
      return map;
    }
    if (!code.equals(registerDto.getCode())) {
      map.put("message", "验证码不正确");
      return map;
    }
    if (exists(registerDto.getUsername()) != 0) {
      map.put("message", "用户已存在");
      return map;
    }
    //加密
    String salt = ApplicationUtil.randomNumber(5);
    String password = ApplicationUtil.md5(registerDto.getPassword(), salt);
    String avatar = "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
    //注册
    userMapper.register(registerDto.getUsername(), password, salt, avatar, registerDto.getEmail());
    //删除验证码
    redisTemplate.delete(key);

    map.put("message", "注册成功");
    return map;
  }

  /**
   * 查找用户名是否存在
   *
   * @param username
   * @return
   */
  @Override
  public int exists(String username) {
    return userMapper.exists(username);
  }

  /**
   * 通过用户名找出用户
   *
   * @param username
   * @return
   */
  @Override
  public User findUserByUserName(String username) {

    User userByUserName = userMapper.findUserByUserName(username);

    return userByUserName;
  }

  /**
   * 找出用户的详细信息  例如粉丝、关注、收藏数量
   * @param username
   * @return
   */
  @Override
  public User findUserDetails(String username) {
    User user = userMapper.findUserByUserName(username);
    if (user == null) return null;
    user.setPassword(null);
    user.setSalt(null);
    user.setProfile(interactionService.findUserProfile(user.getId()));
    return user;
  }

  /**
   * 找出粉丝的信息：用户id、用户名、头像这些
   *
   * @param list
   * @return
   */
  @Override
  public List<User> findFollowerSimpleInfo(List<Integer> list) {
    return userMapper.findFollowerSimpleInfo(list);
  }

  /**
   * 修改用户名
   *
   * @param userName
   * @return
   */
  @Override
  public boolean updateUserName(String userName) {
    int exists = userMapper.exists(userName);
    if (exists == 0) {//如果要修改的用户名没被使用
      Integer userId = jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
      User user = new User();
      user.setId(userId);
      user.setUsername(userName);
      userMapper.updateUserInfo(user);
      return true;
    }
    return false;
  }

  /**
   * 修改性别
   * @param sex
   */
  @Override
  public void updateSex(Integer sex) {
    Integer userId = jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
    User user = new User();
    user.setId(userId);
    user.setSex(sex);
    userMapper.updateUserInfo(user);
  }

  /**
   * 修改个人主页的简介
   *
   * @param info
   */
  @Override
  public void updateInfo(String info) {
    Integer userId = jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
    User user = new User();
    user.setId(userId);
    user.setPrivate_info(info);
    userMapper.updateUserInfo(user);
  }

  /**
   * 修改密码
   *
   * @param dto
   * @return
   */
  @Override
  public Map<String, String> updatePassword(UpdatePasswordDto dto) {
    User user = userMapper.findUserById(jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
    Map<String, String> map = new HashMap<>();
    if (user.getPassword().equals(ApplicationUtil.md5(dto.getLastPassword(), user.getSalt()))) {//旧密码相同
      if (dto.getNewPassword().equals(dto.getSencondPassword())) {                              //新密码和二次密码是否相同
        String salt = ApplicationUtil.randomNumber(5);                                       //新生成随机盐
        String newPassword = ApplicationUtil.md5(dto.getNewPassword(), salt);
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setSalt(salt);
        newUser.setPassword(newPassword);
        userMapper.updateUserInfo(newUser);
        map.put("message", "success");
      } else {
        map.put("message", "两次密码不一致");
      }
    } else map.put("message", "旧密码错误");
    return map;
  }

  /**
   * 找回密码
   *
   * @param dto
   * @param ip
   * @return
   */
  public Map<String, String> findPassword(FindPwdSendEmailDto dto, String ip) {
    //限流
    ArrayList<LimitIp> ipArrayList = new ArrayList<>();
    ipArrayList.add(new LimitIp(ip + "findPsw", 60 * 60, 5, "一小时"));
    ipArrayList.add(new LimitIp(ip + "findPsw:count", 60, 1, "一分钟"));
    Map<String, String> msg = ipLimitUtil.ipContro(ipArrayList);
    if (!msg.get("message").equals("success"))
      return msg;

    Map<String, String> map = new HashMap<>();
    User user = userMapper.findUserByUserName(dto.getUsername());
    if (user == null) {
      map.put("message", "用户不存在");
      return map;
    }
    if (!user.getEmail().equals(dto.getEmail())) {
      map.put("message", "输入邮箱与该用户绑定邮箱不一致");
      return map;
    }
    String newPsw = ApplicationUtil.randomNumber(8);
    //发送邮件
    emailService.sendPsw(dto.getEmail(), newPsw);
    String newSalt = ApplicationUtil.randomNumber(6);
    newPsw = new Md5Hash(newPsw, newSalt, 1024).toHex();
    User newUser = new User();
    newUser.setId(user.getId());
    newUser.setSalt(newSalt);
    newUser.setPassword(newPsw);

    //修改密码
    userMapper.updateUserInfo(newUser);
    map.put("message", "success");
    return map;
  }


}
