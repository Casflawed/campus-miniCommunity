package com.flameking.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
  private static ApplicationContext applicationContext = null;

  /**
   * 取得存储在静态变量中的 ApplicationContext.
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name) {
    return (T) applicationContext.getBean(name);
  }

  /**
   * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
   */
  public static <T> T getBean(Class<T> requiredType) {
    return applicationContext.getBean(requiredType);
  }

  /**
   * 清除 SpringContextHolder 中的 ApplicationContext 为 Null.
   */
  public static void clearHolder() {
    log.info("清除SpringContextHolder中的ApplicationContext:{}", applicationContext);
    applicationContext = null;
  }

  /**
   * 实现 DisposableBean 接口, 在 Context 关闭时清理静态变量.
   */
  @Override
  public void destroy() {
    SpringContextHolder.clearHolder();
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    SpringContextHolder.applicationContext = applicationContext;
  }

  public static <T> Map<String, T> getBeansOfType(Class<T> classz) {
    return applicationContext.getBeansOfType(classz);
  }
}
