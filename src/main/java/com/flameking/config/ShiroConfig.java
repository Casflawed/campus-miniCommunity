package com.flameking.config;




import com.flameking.filter.JWTFilter;
import com.flameking.shiro.CustomRealm;
import com.flameking.shiro.cache.RedisCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ShiroConfig:shiro 配置类,配置哪些拦截,哪些不拦截,哪些授权等等各种配置都在这里
 *
 */

/**
 * @description  Shiro配置类
 */
@Configuration
public class ShiroConfig {
    /**
     * 注入 securityManager
     */
    @Bean
    public SecurityManager securityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        //使用自定义的redis进行缓存
        customRealm.setCacheManager(new RedisCacheManager());                   //设置缓存管理器
        customRealm.setCachingEnabled(true);                                    //开启全局缓存
        customRealm.setAuthenticationCachingEnabled(true);                      //认证缓存
        customRealm.setAuthenticationCacheName("AuthenticationCache");
        customRealm.setAuthorizationCachingEnabled(true);                       //授权缓存
        customRealm.setAuthorizationCacheName("AuthorizationCache");

        //配置安全数据源，最终执行认证的就是Realm
        securityManager.setRealm(customRealm);
        return securityManager;
    }

    /**
     * 注册请求过滤器，包括shiro的默认过滤器和自定义的过滤器
     */
    @Bean
    public ShiroFilterFactoryBean factory(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        // 添加自定义过滤器并且取名为jwt
        filterMap.put("jwt", new JWTFilter());
        factoryBean.setFilters(filterMap);

        //设置securityManager组件
        factoryBean.setSecurityManager(securityManager);

        // 设置无权限时跳转的 url（登录路径和登录成功路径都在前端配置跳转规则）
        factoryBean.setUnauthorizedUrl("/unauthorized/无权限");

        //配置路径过滤规则，越是详细的过滤规则越该在前面注册
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        //anon代表匿名过滤器，表示不用登录就可直接访问，下面是swagger工具的接口
        filterRuleMap.put("/v2/api-docs","anon");
        filterRuleMap.put("/swagger-resources/configuration/ui","anon");
        filterRuleMap.put("/swagger-resources","anon");
        filterRuleMap.put("/swagger-resources/configuration/security","anon");
        filterRuleMap.put("/swagger-ui.html","anon");
        filterRuleMap.put("/webjars/**","anon");

        //下面配置了不用登录就能直接访问的业务接口
        filterRuleMap.put("/register/**","anon");
        filterRuleMap.put("/login", "anon");
        filterRuleMap.put("/findPassword","anon");
        filterRuleMap.put("/sendEmail","anon");
        filterRuleMap.put("/websocket","anon");
        filterRuleMap.put("/unauthorized/**", "anon");

        //最后再设置所有的请求都由我们的自定义过滤器进行过滤
        filterRuleMap.put("/**", "jwt");

        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }


    /**
     * 注解生效支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 注解生效支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * Spring接管shiro的生命周期
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
