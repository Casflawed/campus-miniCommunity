package com.flameking.controller;


import com.flameking.dto.FindPwdSendEmailDto;
import com.flameking.dto.LoginDto;
import com.flameking.dto.RegisterDto;
import com.flameking.dto.UpdatePasswordDto;
import com.flameking.entity.ResultBean;
import com.flameking.entity.User;
import com.flameking.service.UserService;
import com.flameking.sysLog.Logweb;
import com.flameking.utils.ApplicationUtil;
import com.flameking.utils.IpUtil;
import com.flameking.utils.JWTUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户信息的修改
 * 注册登录
 */
@RestController
@Api(value = "UserController")
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private JWTUtil jwtUtil;

  @Logweb("用户注册")
  @ApiOperation(value = "用户注册", notes = "填写用户名，密码")
  @PostMapping("/register")
  public ResultBean register(@Valid @RequestBody RegisterDto registerDto) {
    Map<String, String> info = userService.register(registerDto);
    String msg = info.get("message");
    if (msg.equals("注册成功"))
      return ResultBean.success(msg);
    return ResultBean.fail(msg);
  }

  @Logweb("用户登录")
  @ApiOperation(value = "用户登录", notes = "登录--不进行拦截")
  @PostMapping("/login")
  public ResultBean login(@Valid @RequestBody LoginDto loginDto) {
    User user = userService.findUserByUserName(loginDto.getUsername());
    if (user == null) {
      return ResultBean.fail("用户名不存在");
    }
    if (user.getStatus() == 0) {
      return ResultBean.fail("用户被锁定");
    }
    if (!Objects.equals(ApplicationUtil.md5(loginDto.getPassword(), user.getSalt()),
            user.getPassword())) {
      return ResultBean.fail("密码错误");
    }
    user.setPassword("");
    user.setSalt("");
    //登录成功，给客户端凭证和用户信息
    return ResultBean.success("登录成功").add("token", jwtUtil.createToken(user.getId())).add("user", user);
  }

  @GetMapping("/getAvatar/{username}")
  public String getAvatar(@PathVariable String username) {
    User user = userService.findUserByUserName(username);
    return user.getAvatar();
  }
//因为是前后端分离，退出登录由前端来做就好了
//    @ApiOperation(value = "退出登录", notes = "需要登录才行放行")
//    @GetMapping("/logout")
//    public ResultBean logout(){
//        SecurityUtils.getSubject().logout();
//        return ResultBean.success("退出成功");
//    }


  @GetMapping("/findUserDetails/{username}")
  public ResultBean findUserDetails(@PathVariable String username) {
    return ResultBean.success().add("user", userService.findUserDetails(username));
  }

  @Logweb("修改用户名")
  @GetMapping("/updateUserName")
  public ResultBean updateUserName(@RequestParam(required = true) String userName) {
    if (userName != null && userName.length() >= 3 && userName.length() <= 20) {
      boolean b = userService.updateUserName(userName);
      if (b) {
        return ResultBean.success("修改成功");
      }
      return ResultBean.fail("用户名已存在");
    }
    return ResultBean.fail("格式错误");
  }

  @Logweb("修改性别")
  @GetMapping("/updateSex")
  public ResultBean updateSex(@RequestParam(required = true) Integer sex) {
    if (sex == 1 || sex == 2) {
      userService.updateSex(sex);
      return ResultBean.success("修改成功");
    }
    return ResultBean.success("输入有误");
  }

  @Logweb("修改个人简介")
  @GetMapping("/updateInfo")
  public ResultBean updateInfo(@RequestParam(required = true) String info) {
    userService.updateInfo(info);
    return ResultBean.success("修改成功");
  }

  @Logweb("修改密码")
  @PostMapping("/updatePassword")
  public ResultBean updatePassword(@RequestBody @Validated UpdatePasswordDto dto) {
    Map<String, String> map = userService.updatePassword(dto);
    String message = map.get("message");
    if (message.equals("success"))
      return ResultBean.success(message);
    return ResultBean.fail(message);
  }

  @Logweb("找回密码")
  @PostMapping("/findPassword")
  public ResultBean findPassword(@Validated @RequestBody FindPwdSendEmailDto findPwdSendEmailDto, HttpServletRequest request) throws MessagingException {
    String ipAddr = IpUtil.getIpAddr(request);
    Map<String, String> msg = userService.findPassword(findPwdSendEmailDto, ipAddr);
    if (msg.get("message").equals("success")) {
      return ResultBean.success("邮件已发送");
    } else
      return ResultBean.fail(msg.get("message"));
//        asyncService.hello();
//        return ResultBean.success();

  }


  @ApiOperation(value = "测试无权限", notes = "无权限跳转的接口")
  @GetMapping(path = "/unauthorized/{message}")
  public ResultBean unauthorized(@PathVariable String message) {
    return ResultBean.fail().add("info", message);
  }

  /**
   * 就是config配置类FilterExceConfig 认证异常的捕获
   *
   * @param request
   * @return
   */
  @Logweb("认证失败")
  @RequestMapping("/error/exthrow")
  public Map<String, Object> rethrow(HttpServletRequest request) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("code", "401");
    map.put("message", "token认证失败");
    return map;
  }
}
