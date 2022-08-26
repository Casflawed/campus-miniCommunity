package com.flameking.utils;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

public class ApplicationUtil {

  /**
   * 获取指定位数的随机数字符串
   * 用来生成salt值和随机验证码
   *
   * @param n
   * @return
   */
  public static String randomNumber(int n) {
    String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    int len = str.length();
    StringBuilder sb = new StringBuilder(n);
    for (int i = 1; i <= n; i++) {
      sb.append(str.charAt(new Random().nextInt(len)));
    }
    return sb.toString();
  }

  /**
   * md5加缪
   *
   * @param key
   * @param salt
   * @return 经过1024次md5加密迭代后的值
   */
  public static String md5(String key, String salt) {
    return new Md5Hash(key, salt, 1024).toHex();
  }

  public static void sendHtmlEmail(String toEmail, String fromEmail, String subject, String text) {
    JavaMailSenderImpl mailSender = SpringContextHolder.getBean(JavaMailSenderImpl.class);
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = null;
    try {
      helper = new MimeMessageHelper(message, true);
      //邮件标题
      helper.setSubject(subject);
      //邮件类型是html，内容是text
      helper.setText(text, true);
      //收件人
      helper.setTo(toEmail);
      //发送人
      helper.setFrom(fromEmail);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    mailSender.send(message);
  }

}

