package com.flameking.service.impl;

import com.flameking.service.EmailService;
import com.flameking.utils.ApplicationUtil;
import com.flameking.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {
  @Value("${community.protocol}")
  private String protocol;

  @Value("${community.domain}")
  private String domain;

  @Value("${server.servlet.context-path}")
  private String context;

  @Value("${spring.mail.username}")
  private String fromEmail;

  private String url = protocol + "://" + domain + context;

  @Lazy
  @Autowired
  RedisTemplate redisTemplate;


  /**
   * 发送注册验证码，因为十分耗时，采用了异步任务@Async
   *
   * @param toEmail
   */
  @Async("mySimpleAsync")
  public void sendRegisterEmail(String toEmail) {
    //随机生成6位数
    String code = ApplicationUtil.randomNumber(6);
    String subject = "注册验证码";
    // http://localhost:8888/retwisApi
    String text = "<html><body>你在<a href='" + url + "'>微社区</a>进行注册" +
            "验证码为<h2>" + code + "<h2><br>请在30分钟内完成注册<body></html>";

    ApplicationUtil.sendHtmlEmail(toEmail, fromEmail, subject, text);

    //设置验证码的有效时间
    String key = RedisKeyUtil.getEmailCodeKey(toEmail);
    redisTemplate.opsForValue().set(key, code, 60 * 10, TimeUnit.SECONDS);
  }

  /**
   * 重置密码
   *
   * @param email
   * @param psw
   */
  @Async("mySimpleAsync")
  public void sendPsw(String email, String psw) {
    String subject = "重置密码";
    String text = "<html><body>你在<a href='" + url + "'>微社区</a>进行重置密码" +
            "新密码为<h2>" + psw + "<h2><br>建议尽快登录修改密码<body></html>";

    ApplicationUtil.sendHtmlEmail(email, fromEmail, subject, text);
  }

}
